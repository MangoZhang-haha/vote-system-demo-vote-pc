package flybear.hziee.core.util;

import flybear.hziee.core.sql.Row;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 通用函数
 *
 * @author Simon
 */
public class Fn {
    /**
     * 端掉字符串右侧的N多指定字符
     */
    public static String rtrim(String str, String needle) {
        int end = str.length();
        while (end != 0 && needle.indexOf(str.charAt(end - 1)) != -1) {
            end--;
        }
        if (end == 0) {
            return "";
        }
        return str.substring(0, end);
    }

    /**
     * null值变成空字符
     */
    public static String nullToEmpty(String str) {
        return str == null ? "" : str;
    }

    /**
     * 命名由下划线小写变驼峰法
     *
     * @param name             待转换的下划线名称
     * @param isFirstUpperCase 首字母是否需要大写，默认小写
     * @return return null if name is null or empty
     */
    public static String camelName(String name, Boolean... isFirstUpperCase) {
        if (name == null || name.isEmpty()) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        if (isFirstUpperCase.length != 0 && isFirstUpperCase[0]) {
            // 将第一个字符处理成大写
            result.append(name.substring(0, 1).toUpperCase());
        } else {
            // 将第一个字符保持不变
            result.append(name, 0, 1);
        }

        // 循环处理其余字符
        for (int i = 1; i < name.length(); i++) {
            String s = name.substring(i, i + 1);
            // 遇到下划线则舍弃，并把后一个字符变大写
            if ("_".equals(s)) {
                i++;
                String nextChar = name.substring(i, i + 1);
                result.append(nextChar.toUpperCase());
            } else {
                // 其他字符直接追加上
                result.append(s);
            }

        }
        return result.toString();
    }

    /**
     * 命名由驼峰法变下划线小写
     *
     * @return return "" if name is null or empty
     */
    public static String underscoreName(String name) {
        if (name == null || name.isEmpty()) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        // 将第一个字符处理成小写
        result.append(name.substring(0, 1).toLowerCase());
        // 循环处理其余字符
        for (int i = 1; i < name.length(); i++) {
            String s = name.substring(i, i + 1);
            // 在大写字母前添加下划线
            if (Arrays.asList(letters).contains(s)) {
                result.append("_");
            }
            // 其他字符直接转成小写
            result.append(s.toLowerCase());
        }
        return result.toString();
    }

    /**
     * 将一个 Model 对象转化为一个 Row 将Model中的驼峰命名自动转成Row的小写下划线
     *
     * @param model 要转化的Model 对象
     * @return 转化出来的 Row 对象
     */
    public static Row modelToRow(Object model) {
        if (model == null) {
            return null;
        }
        Class<?> type = model.getClass();
        Row returnRow = new Row();
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(type);
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        if (beanInfo == null) {
            return null;
        }
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor descriptor : propertyDescriptors) {
            String propertyName = descriptor.getName();
            // 驼峰转成下划线
            String rowKey = Fn.underscoreName(propertyName);
            if (!"class".equals(propertyName)) {
                Method readMethod = descriptor.getReadMethod();
                Object result = null;
                try {
                    result = readMethod.invoke(model);
                } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                // 下划线
                returnRow.put(rowKey, result);
            }
        }
        return returnRow;
    }

    /**
     * 将一个 Row 对象转化为一个 Model对象 将Row的小写下划线自动转成Model中的驼峰命名
     *
     * @param type 要转化的类型
     * @param row  包含属性值的Row
     * @return 转化出来的 Model 对象
     */
    public static Object rowToModel(Class<?> type, Row row) {
        if (row == null) {
            return null;
        }
        BeanInfo modelInfo = null;
        try {
            modelInfo = Introspector.getBeanInfo(type);
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        // 获取类属性
        Object obj = null;
        try {
            obj = type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        if (modelInfo == null) {
            return null;
        }
        // 创建 JavaBean 对象
        // 给 JavaBean 对象的属性赋值
        PropertyDescriptor[] propertyDescriptors = modelInfo.getPropertyDescriptors();
        for (PropertyDescriptor descriptor : propertyDescriptors) {
            // 驼峰转成下划线
            String rowKey = descriptor.getName();
            if (row.containsKey(rowKey)) {
                // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
                Object value = row.get(rowKey);

                Object[] args = new Object[1];
                args[0] = value;

                try {
                    Method method = descriptor.getWriteMethod();
                    // 修复MyBatis返回类型跟数据库的不匹配
                    String fieldType = method.getParameterTypes()[0].getSimpleName();
                    if ("Integer".equals(fieldType)) {
                        args[0] = Integer.valueOf(args[0].toString());
                    } else if ("Short".equals(fieldType)) {
                        args[0] = Short.valueOf(args[0].toString());
                    } else if ("Byte".equals(fieldType)) {
                        args[0] = Byte.valueOf(args[0].toString());
                    }
                    method.invoke(obj, args);
                } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return obj;
    }
}
