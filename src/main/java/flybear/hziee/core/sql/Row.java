package flybear.hziee.core.sql;

import java.util.HashMap;

/**
 * 数据元组
 *
 * @author Simon
 */
public class Row extends HashMap<String, Object> {

    private static final long serialVersionUID = 129701856673425221L;

    @Override
    public Object get(Object key) {
        if (super.get(key) != null) {
            String oType = super.get(key).getClass().getSimpleName();
            if ("String[]".equals(oType)) {
                return ((String[]) super.get(key))[0];
            } else if ("byte[]".equals(oType)) {
                return (new String((byte[]) super.get(key)));
            }
        }
        return super.get(key);
    }
    public Integer getInt(Object key) {
        if (get(key) != null) {
            return Integer.valueOf(get(key).toString());
        } else {
            return null;
        }
    }
    public Long getLong(Object key) {
        if (get(key) != null) {
            return Long.valueOf(get(key).toString());
        } else {
            return null;
        }

    }
    public Short getShort(Object key) {
        if (get(key) != null) {
            return Short.valueOf(get(key).toString());
        } else {
            return null;
        }
    }
    public Byte getByte(Object key) {
        if (get(key) != null) {
            return Byte.valueOf(get(key).toString());
        } else {
            return null;
        }

    }
    public Float getFloat(Object key) {
        if (get(key) != null) {
            return Float.valueOf(get(key).toString());
        } else {
            return null;
        }
    }
    public Double getDouble(Object key) {
        if (get(key) != null) {
            return Double.valueOf(get(key).toString());
        } else {
            return null;
        }
    }
    public String getString(Object key) {
        if (get(key) != null) {
            return String.valueOf(get(key).toString());
        } else {
            return null;
        }
    }
}
