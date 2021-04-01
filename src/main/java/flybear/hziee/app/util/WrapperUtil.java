package flybear.hziee.app.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * mybatis-plus wrapper 常用类
 *
 * @author flybear
 * @since 2020/1/6 20:22
 */
public class WrapperUtil {

    /**
     * 自定义排序方法
     *
     * @param wrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @param sorts   排序字符串
     */
    public static <T> void orderBy(QueryWrapper<T> wrapper, String sorts) {
        String[] sortArr = sorts.split(",");
        for (String sort : sortArr) {
            if (sort.length() != 1) {
                if (sort.endsWith("-")) {
                    wrapper.orderByDesc(sort.substring(0, sort.length() - 1));
                } else {
                    wrapper.orderByAsc(sort);
                }
            }
        }
    }

    public static <T> void like(QueryWrapper<T> wrapper, @NotNull Object obj, String[] include) {
        List<String> includeList = Arrays.stream(include).map(StringUtils::camelToUnderline).collect(Collectors.toList());
        Class cls = obj.getClass();
        Field[] sourceFields = cls.getDeclaredFields();
        for (Field field : sourceFields) {
            field.setAccessible(true);
            String column = StringUtils.camelToUnderline(field.getName());
            if (includeList.contains(column)) {
                try {
                    Object value = field.get(obj);
                    if (value == null) {
                        continue;
                    }
                    if (StringUtils.isNotBlank(value.toString())) {
                        wrapper.like(column, value);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
