package flybear.hziee.app.util;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * 注意：如果涉及到使用运行环境的方法，请在对应类上添加 @DependsOn("appUtils") 注解，确保容器先加载AppUtils类
 *
 * @author qiancj
 * @since 2020-06-30 13:02
 */
@Component("appUtils")
public class AppUtils {

    /**
     * 应用上下文
     */
    private static ApplicationContext context;

    @Resource
    public void setApplicationContext(ApplicationContext c) {
        context = c;
    }

    /**
     * 是否是开发环境
     *
     * @return 是否是开发环境
     */
    public static boolean isDevEnvironment() {
        return Arrays.asList(context.getEnvironment().getActiveProfiles()).contains("dev");
    }

    /**
     * 是否是测试环境
     *
     * @return 是否是测试环境
     */
    public static boolean isTestEnvironment() {
        return Arrays.asList(context.getEnvironment().getActiveProfiles()).contains("test");
    }

    /**
     * 是否是部署环境
     *
     * @return 是否是部署环境
     */
    public static boolean isProdEnvironment() {
        return Arrays.asList(context.getEnvironment().getActiveProfiles()).contains("prod");
    }

    /**
     * 获取配置参数
     *
     * @param property 配置参数名
     * @return 配置参数值
     */
    public static String getProperty(String property) {
        return context.getEnvironment().getProperty(property);
    }

    /**
     * 获取bean对象
     *
     * @param requiredType Class
     * @param <T>          T
     * @return bean对象
     */
    public static <T> T getBean(Class<T> requiredType) {
        return context.getBean(requiredType);
    }

    /**
     * 获取bean对象
     *
     * @param name         bean name
     * @param requiredType Class
     * @param <T>          T
     * @return bean对象
     */
    public static <T> T getBean(String name, Class<T> requiredType) {
        return context.getBean(requiredType);
    }

}
