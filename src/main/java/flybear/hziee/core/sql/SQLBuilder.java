package flybear.hziee.core.sql;

import com.baomidou.mybatisplus.annotation.TableName;
import flybear.hziee.core.util.Fn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.*;
import java.util.Map.Entry;

/**
 * sql查询语句生成器，用于简化复杂查询，采用连贯操作方式
 * ;mysql驱动
 * 使用示例:
 * SQLBuilder(User.class).fields("name","sex","dept_name").join(Dept.class,"User.did=Dept.id").where(HashMap).order("name","desc").page(1,10).selectSql();
 * 说明：
 * where的用法：
 * Map<String,Object> map = new HashMap<String,Object>();
 * map.put("name","Simon"); //变成name='Simon'
 * map.put("name",new String[]{"like","%Sim%"});
 * where(map,"AND");
 * where(map,"OR"); //where可以多次使用
 *
 * @author Simon
 */
@Slf4j
public class SQLBuilder {
    @Resource
    private ApplicationContext applicationContext;

    private String pojoName;
    private String tableName;
    private String fieldString;
    private String whereString;
    private String pageString;
    private String orderString;
    private String joinString;
    private String groupString;
    private List<String> tableFields = new ArrayList<>();
    private String lastSql;

    private SQLBuilder() {
    }

    private SQLBuilder(Class<?> poClass) {
        setTableName(parseTableName(poClass));
        setPojoName(poClass.getSimpleName());
        //将类的属性变成字段名，方式是驼峰转下划线
        Field[] fields = poClass.getDeclaredFields();
        for (Field field : fields) {
            tableFields.add(Fn.underscoreName(field.getName()));
        }
    }

    public static SQLBuilder getSQLBuilder(Class<?> poClass) {
        return new SQLBuilder(poClass);
    }

    /**
     * 设置查询字段,需写在连贯操作的第一位<br/>
     * 支持字符串形式fields("id,user_name") -> "select id,user_name AS userName" <br/>
     * 支持数组参数fields("id","user_name") -> 效果跟上一句一样<br/>
     * 说明:1.下划线命名的字段返回时会变成驼峰法，跟类的属性名一致<br/>
     * 2.包含函数的字段，请自行AS，设置别名
     *
     * @param fieldsArr 查询字段数组
     * @return SQLBuilder
     */
    public SQLBuilder fields(String... fieldsArr) {
        StringBuilder fs = new StringBuilder();
        if (fieldsArr.length == 0 || "*".equals(fieldsArr[0])) {
            fieldsArr = tableFields.toArray(new String[0]);
        }
        // fields("id,name")单个参数形式
        if (fieldsArr.length == 1) {
            // 把字符串分割成数组
            fieldsArr = fieldsArr[0].split(",");
        }
        for (String f : fieldsArr) {
            String fLower = f.toLowerCase();
            // 若不是应用函数、不包含AS，则处理把字段名处理成驼峰名
            if (!(fLower.contains("(") || fLower.contains(")") || fLower.contains(" as "))) {
                fs.append(f).append(" AS ").append(Fn.camelName(f)).append(",");
            } else {
                fs.append(f).append(",");
            }
        }
        // 删除最后的逗号
        fs = new StringBuilder(fs.substring(0, fs.length() - 1));
        setFieldString(fs.toString());
        return this;
    }

    /**
     * 设置where条件,where可以多次使用
     *
     * @param where     条件组装成map,可以用pojo做example再转成map
     * @param logicType 条件结合方式,可选值AND或OR
     * @return SQLBuilder
     */
    public SQLBuilder where(Map<String, Object> where, String... logicType) {
        if (where != null && !where.isEmpty()) {
            if (logicType.length == 0) {
                logicType = new String[]{"AND"};
            }
            if (getWhereString() == null) {
                setWhereString("WHERE " + parseWhereMap(where, logicType[0]));
            } else {
                setWhereString(getWhereString() + logicType[0] + " " + parseWhereMap(where, logicType[0]));
            }
        }
        return this;
    }

    /**
     * 设置where条件
     *
     * @param where sql字符串字面量,如"user=simon"
     * @return SQLBuilder
     */
    public SQLBuilder where(String where, String... logicType) {
        if (where != null && !where.trim().isEmpty()) {
            if (logicType.length == 0) {
                logicType = new String[]{"AND"};
            }
            where = where.trim();
            if (getWhereString() == null) {
                setWhereString("WHERE " + where + " ");
            } else {
                setWhereString(getWhereString() + logicType[0] + " " + where + " ");
            }
        }
        return this;
    }

    /**
     * 设置group by
     *
     * @param field 多个字段用逗号隔开
     * @return SQLBuilder
     */
    public SQLBuilder group(String field) {
        if (!StringUtils.isEmpty(field)) {
            setGroupString("GROUP BY " + field + " ");
        }
        return this;
    }

    /**
     * 设置排序
     *
     * @param orderField 排序字段
     * @param orderType  排序类型
     * @return SQLBuilder
     */
    public SQLBuilder order(String orderField, String orderType) {
        orderType = orderType.toUpperCase();
        if (!("ASC".equals(orderType) || "DESC".equals(orderType))) {
            orderType = "ASC";
        }
        if (getOrderString() == null) {
            setOrderString("ORDER BY " + orderField + " " + orderType + " ");
        } else {
            setOrderString(getOrderString() + "," + orderField + " " + orderType + " ");
        }
        return this;
    }

    /**
     * 设置分页
     *
     * @param page 当前页码
     * @param rows 每页显示数量
     * @return SQLBuilder
     */
    public SQLBuilder page(Integer page, Integer rows) {
        if (page != null && rows != null) {
            setPageString("LIMIT " + (page - 1) * rows + "," + rows + " ");
        }
        return this;
    }


    /**
     * 解析jqui的分页和排序参数
     */
    public SQLBuilder parseUIPageAndOrder(Map<String, String> pageMap) {
        // page,rows,sort,order是前段控件传过来的固定参数
        if (pageMap.get("page") != null && pageMap.get("rows") != null) {
            int page = Integer.parseInt(pageMap.get("page"));
            int rows = Integer.parseInt(pageMap.get("rows"));
            setPageString("LIMIT " + (page - 1) * rows + "," + rows + " ");
        }
        if (pageMap.get("sort") != null && pageMap.get("order") != null) {
            String orderField = pageMap.get("sort");
            String orderType = pageMap.get("order").toUpperCase();
            setOrderString("ORDER BY " + orderField + " " + orderType + " ");
        }
        return this;
    }

    /**
     * 设置多表连接
     *
     * @param anotherPoClass  需要连接表对应的pojo名称
     * @param joinFieldString 连接条件  注 : 有同表连接是第2个表开始别名变成,原别名+"_"+i (i从0开始)
     * @return SQLBuilder
     */
    public SQLBuilder join(Class<?> anotherPoClass, String joinFieldString) {
        String pojoNameString = getPojoName();
        String simpleName = anotherPoClass.getSimpleName();
        String anotherName = simpleName;
        // 判断是否于原类同名
        if (anotherName.equals(pojoNameString)) {
            anotherName = simpleName + "_" + 0;
        }
        for (int i = 0; getJoinString() != null && getJoinString().contains(" AS " + anotherName); i++) {
            anotherName = simpleName + "_" + i;
        }
        String sql = "JOIN " + parseTableName(anotherPoClass)
                + " AS " + anotherName + " ON " + joinFieldString + " ";
        // 拼接多个join
        setJoinString(nullToBlank(getJoinString()) + sql);
        return this;
    }

    /**
     * 设置多表连接
     *
     * @param anotherPoClass  需要连接表对应的pojo名称
     * @param joinFieldString 连接条件  注 : 有同表连接是第2个表开始别名变成,原别名+"_"+i (i从0开始)
     * @param joinType        连接类型,可选值LEFT,RIGHT,FULL
     * @return SQLBuilder
     */
    public SQLBuilder join(Class<?> anotherPoClass, String joinFieldString, String joinType) {
        String pojoNameString = getPojoName();
        String simpleName = anotherPoClass.getSimpleName();
        String anotherName = simpleName;
        // 判断是否于原类同名
        if (anotherName.equals(pojoNameString)) {
            anotherName = simpleName + "_" + 0;
        }
        for (int i = 0; getJoinString() != null && getJoinString().contains(" AS " + anotherName); i++) {
            anotherName = simpleName + "_" + i;
        }
        String sql = joinType + " JOIN " + parseTableName(anotherPoClass)
                + " AS " + anotherName + " ON " + joinFieldString + " ";
        // 拼接多个join
        setJoinString(nullToBlank(getJoinString()) + sql);
        return this;
    }

    /**
     * 组装insert语句,也可以用于更新,但id字段必须带有值
     */
    public String insertSql(Row entityRow) {
        StringBuilder fieldString = new StringBuilder();
        StringBuilder valuesString = new StringBuilder("(");
        Set<Entry<String, Object>> keys = entityRow.entrySet();
        for (Entry<String, Object> entry : keys) {
            String key = entry.getKey();
            fieldString.append(key).append(",");
            Object value = entry.getValue();
            if (value != null) {
                valuesString.append("\'").append(value).append("\'").append(",");
            } else {
                valuesString.append(",");
            }
        }
        fieldString = new StringBuilder(cutString(fieldString.toString()));
        valuesString = new StringBuilder(cutString(valuesString.toString()) + ")");
        String sql = "INSERT INTO " + getTableName() + " (" + fieldString + ")";
        sql += " VALUES " + valuesString + "";
        log.info(sql);
        return sql;
    }

    /**
     * 组装批量insert语句,也可以用于更新,但id字段必须带有值
     *
     * @return insert语句
     */
    public String insertSql(List<Row> entityList) {
        StringBuilder fieldString = new StringBuilder();
        StringBuilder valuesString = new StringBuilder();
        Row entityRow = entityList.get(0);
        Set<String> keysSet = entityRow.keySet();
        for (String key : keysSet) {
            fieldString.append(key).append(",");
        }
        // 删除最后的逗号
        fieldString = new StringBuilder(cutString(fieldString.toString()));
        for (Map<String, Object> map : entityList) {
            StringBuilder val = new StringBuilder("(");
            for (String key : keysSet) {
                // value
                Object value = map.get(key);
                if (value != null) {
                    val.append("\'").append(value).append("\'").append(",");
                } else {
                    val.append(",");
                }
            }
            val = new StringBuilder(cutString(val.toString()) + ")");
            valuesString.append(val).append(",");
        }
        // 删除最后的逗号
        valuesString = new StringBuilder(cutString(valuesString.toString()));
        String sql = "INSERT INTO " + getTableName() + " (" + fieldString + ")";
        sql += " VALUES " + valuesString + "";
        log.info(sql);
        return sql;
    }

    /**
     * 组装update语句
     * 设置更新的id和字段,先将要更新的字段组装成一个Row,需要和where连用确保更新正确
     * 建议使用Fn 中转化实体为Row的方法
     */
    public String updateSql(Row entityRow) {
        StringBuilder updateString = new StringBuilder();
        Set<Entry<String, Object>> keys = entityRow.entrySet();
        for (Entry<String, Object> entry : keys) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (!StringUtils.isEmpty(value)) {
                updateString.append(key).append("=\'").append(value.toString()).append("\'").append(",");
            }
        }
        // 删除最后的逗号
        updateString = new StringBuilder(cutString(updateString.toString()));
        String sql = "UPDATE " + getTableName() + " SET ";
        sql += updateString + " ";
        resetQuery();
        log.info(sql);
        return sql;
    }

    /**
     * 组装delete语句,和where连用可以实现批量删除
     */
    public String deleteSql() {
        String sql = "DELETE FROM " + getTableName() + " ";
        sql += nullToBlank(getWhereString());
        resetQuery();
        log.info(sql);
        return sql;
    }

    /**
     * 组装select的sql语句
     */
    public String selectSql() {
        if (getFieldString() == null) {
            throw new NullPointerException("Must invoke method field first!");
        }

        String sql = "SELECT ";
        sql += getFieldString() + " ";
        sql += "FROM " + getTableName() + " AS " + getPojoName() + " ";
        sql += nullToBlank(getJoinString());
        sql += nullToBlank(getWhereString());
        sql += nullToBlank(getGroupString());
        sql += nullToBlank(getOrderString());
        sql += nullToBlank(getPageString());
        // 重置查询条件
        resetQuery();
        // 记录最后一条sql语句
        this.setLastSql(sql);
        log.info(sql);
        return sql;
    }

    public String cutString(String str) {
        str = str.substring(0, str.length() - 1);
        return str;
    }

    /**
     * 解析查询条件
     *
     * @param whereMap  要解析的HashMap
     * @param logicType 条件结合类型,AND或OR
     * @return 条件字符串: key1 = value1 AND key
     */
    public String parseWhereMap(Map<String, Object> whereMap, String logicType) {
        logicType = logicType.toLowerCase();
        if (!("and".equals(logicType) || "or".equals(logicType))) {
            logicType = "and";
        }
        StringBuilder whereString = new StringBuilder();
        for (String k : whereMap.keySet()) {
            Object v = whereMap.get(k);
            if ("_string".equals(k)) {
                // 允许混合直接字符串查询
                whereString.append(v.toString()).append(" ").append(logicType).append(" ");
            } else {
                // whereMap.put("name",new String[]{"like","%Simon%"})
                if (v.getClass().isArray() && ((String[]) v).length == 2) {
                    whereString.append(k).append(" ").append(((String[]) v)[0]).append(" \'").append(((String[]) v)[1]).append("\' ").append(logicType).append(" ");
                } else {
                    whereString.append(k).append("=\'").append(v.toString()).append("\' ").append(logicType).append(" ");
                }
            }
        }
        whereString = new StringBuilder(whereString.toString().trim());
        whereString = new StringBuilder(Fn.rtrim(whereString.toString(), logicType));
        return whereString.toString();
    }

    /**
     * 解析表名,实体类有table注解则读取注解，没有则按“配置的前缀+类名驼峰转下划线”来解析
     */
    private String parseTableName(Class<?> poClass) {
        TableName table = poClass.getAnnotation(TableName.class);
        String tableName;
        // 实体类不存在table注解则自动按规则解析表名
        if (table == null) {
            String prefix = applicationContext.getEnvironment().getProperty("mybatis-plus.global-config.table-prefix");
            tableName = Fn.nullToEmpty(prefix) + Fn.underscoreName(poClass.getSimpleName());
        } else {
            // 实体类直接读取table注解
            tableName = table.value();
        }
        return tableName;
    }

    /**
     * 重置查询条件
     */
    public void resetQuery() {
        setFieldString(null);
        setWhereString(null);
        setOrderString(null);
        setPageString(null);
        setJoinString(null);
        setGroupString(null);
    }

    /**
     * 把null变成""
     */
    private String nullToBlank(String str) {
        if (str == null) {
            str = "";
        }
        return str;
    }


    /**
     * @return the pojoName
     */
    public String getPojoName() {
        return pojoName;
    }

    /**
     * @param pojoName the pojoName to set
     */
    public void setPojoName(String pojoName) {
        this.pojoName = pojoName;
    }

    /**
     * @return the tableName
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * @param tableName the tableName to set
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * @return the fieldString
     */
    public String getFieldString() {
        return fieldString;
    }

    /**
     * @param fieldString the fieldString to set
     */
    public void setFieldString(String fieldString) {
        this.fieldString = fieldString;
    }

    /**
     * @return the whereString
     */
    public String getWhereString() {
        return whereString;
    }

    /**
     * @param whereString the whereString to set
     */
    public void setWhereString(String whereString) {
        this.whereString = whereString;
    }

    /**
     * @return the pageString
     */
    public String getPageString() {
        return pageString;
    }

    /**
     * @param pageString the pageString to set
     */
    public void setPageString(String pageString) {
        this.pageString = pageString;
    }

    /**
     * @return the orderString
     */
    public String getOrderString() {
        return orderString;
    }

    /**
     * @param orderString the orderString to set
     */
    public void setOrderString(String orderString) {
        this.orderString = orderString;
    }

    /**
     * @return the joinString
     */
    public String getJoinString() {
        return joinString;
    }

    /**
     * @param joinString the joinString to set
     */
    public void setJoinString(String joinString) {
        this.joinString = joinString;
    }

    /**
     * @return the lastSql
     */
    public String getLastSql() {
        return lastSql;
    }

    /**
     * @param lastSql the lastSql to set
     */
    public void setLastSql(String lastSql) {
        this.lastSql = lastSql;
    }

    /**
     * @return the groupString
     */
    public String getGroupString() {
        return groupString;
    }

    /**
     * @param groupString the groupString to set
     */
    public void setGroupString(String groupString) {
        this.groupString = groupString;
    }

    /**
     * @return the tableFields
     */
    public List<String> getTableFields() {
        return tableFields;
    }

    /**
     * @param tableFields the tableFields to set
     */
    public void setTableFields(List<String> tableFields) {
        this.tableFields = tableFields;
    }

}
