package flybear.hziee.core.mybatis;

import flybear.hziee.core.sql.Row;
import flybear.hziee.core.sql.SQLBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Simon
 */
@Service
public class SqlRunner {

    @Resource
    private SqlMapper sqlMapper;

    public List<Row> select(String sql, Object... params) {
        return sqlMapper.select(createSOM(sql, params));
    }

    public Row find(String sql, Object... params) {
        return sqlMapper.find(createSOM(sql, params));
    }

    public Integer count(String sql, Object... params) {
        return sqlMapper.count(createSOM(sql, params));
    }

    public Object getValue(String sql, Object... params) {
        return sqlMapper.getValue(createSOM(sql, params));
    }

    /**
     * 自定义单个属性查询
     */
    public List<Row> selectByProperty(String field, String value, Class<?> className) {
        SQLBuilder sqlBuilder = SQLBuilder.getSQLBuilder(className);
        Map<String, Object> whereMap = new HashMap<String, Object>();
        whereMap.put(field, value);
        String sql = sqlBuilder.fields("*").where(whereMap).selectSql();
        return sqlMapper.select(createSOM(sql));
    }

    /**
     * 自定义单个属性查询是否存在
     */
    public Integer count(String field, String value, Integer id, Class<?> className) {
        if (StringUtils.isEmpty(field) || StringUtils.isEmpty(value)) {
            return 0;
        }
        SQLBuilder sqlBuilder = SQLBuilder.getSQLBuilder(className);
        Map<String, Object> whereMap = new HashMap<>(2);
        whereMap.put(field, value);
        if (id != null && id > 0) {
            whereMap.put("id", new String[]{"<>", id.toString()});
        }
        String sql = sqlBuilder.fields("count(*)").where(whereMap).selectSql();
        return sqlMapper.count(createSOM(sql));
    }

    /**
     * insert方法
     */
    public Integer insert(String sql, Object... params) {
        return sqlMapper.insert(createSOM(sql, params));
    }

    /**
     * update方法
     */
    public Integer update(String sql, Object... params) {
        return sqlMapper.update(createSOM(sql, params));
    }

    /**
     * delete方法
     */
    public Integer delete(String sql, Object... params) {
        return sqlMapper.delete(createSOM(sql, params));
    }

    /**
     * 组装参数
     */
    private SOMap createSOM(String sql, Object... params) {
        SOMap somap = new SOMap();
        somap.put("sql", sql);
        for (int i = 0; i < params.length; i++) {
            somap.put(Integer.toString(i), params[i]);
        }
        return somap;
    }


    //====================快捷获取 - start==================

    /**
     * 获取int值
     */
    public Integer getInt(String sql, Object... params) {
        return Integer.valueOf(getValue(sql, params).toString());
    }

    /**
     * 获取long值
     */
    public Long getLong(String sql, Object... params) {
        return Long.valueOf(getValue(sql, params).toString());
    }

    /**
     * 获取short值
     */
    public Short getShort(String sql, Object... params) {
        return Short.valueOf(getValue(sql, params).toString());
    }

    /**
     * 获取byte值
     */
    public Byte getByte(String sql, Object... params) {
        return Byte.valueOf(getValue(sql, params).toString());
    }

    /**
     * 获取float值
     */
    public Float getFloat(String sql, Object... params) {
        return Float.valueOf(getValue(sql, params).toString());
    }

    /**
     * 获取double值
     */
    public Double getDouble(String sql, Object... params) {
        return Double.valueOf(getValue(sql, params).toString());
    }

    /**
     * 获取整数值
     */
    public String getString(String sql, Object... params) {
        return getValue(sql, params).toString();
    }
    //====================快捷获取 - end==================

}
