package flybear.hziee.app.anno;

import flybear.hziee.app.enums.LogModuleEnum;

import java.lang.annotation.*;

/**
 * 控制器日志注解
 *
 * @author qiancj
 * @since 2020/1/8 14:52
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    String value() default "";

    boolean needRecord() default false;

    LogModuleEnum module() default LogModuleEnum.DEFALUT;
}
