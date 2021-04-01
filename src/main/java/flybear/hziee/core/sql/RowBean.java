package flybear.hziee.core.sql;

import flybear.hziee.core.util.Fn;
import org.springframework.cglib.beans.BeanGenerator;
import org.springframework.cglib.beans.BeanMap;

import java.util.Set;

/**
 * 根据数据库结果集动态创建bean对象
 *
 * @author Simon
 */
public class RowBean {
    /**
     * 实体Object
     */
    public Object object = null;

    /**
     * 属性map
     */
    public BeanMap beanMap = null;

    public RowBean() {
        super();
    }

    public RowBean(Row row) {
        this.object = generateBean(row);
        this.beanMap = BeanMap.create(this.object);
        //初始化值
        setInitValue(row);
    }

    /**
     * 自动设值
     */
    public void setInitValue(Row row) {
        Set<String> keySet = row.keySet();
        for (String key : keySet) {
            setValue(Fn.camelName(key), row.get(key));
        }
    }

    /**
     * 给bean属性赋值
     */
    public void setValue(String property, Object value) {
        beanMap.put(property, value);
    }

    /**
     * 通过属性名得到属性值
     */
    public Object getValue(String property) {
        return beanMap.get(property);
    }

    /**
     * 得到该实体bean对象
     */
    public Object getObject() {
        return this.object;
    }

    private Object generateBean(Row row) {
        BeanGenerator generator = new BeanGenerator();
        Set<String> keySet = row.keySet();
        for (String key : keySet) {
            try {
                generator.addProperty(Fn.camelName(key), Class.forName("java.lang.Object"));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return generator.create();
    }

    /**
     * 静态工厂
     */
    public static Object getBean(Row row) {
        RowBean bean = new RowBean(row);
        return bean.getObject();
    }
}
