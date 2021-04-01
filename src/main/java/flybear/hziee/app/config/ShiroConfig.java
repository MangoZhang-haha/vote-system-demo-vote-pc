package flybear.hziee.app.config;

import flybear.hziee.app.filter.JWTFilter;
import flybear.hziee.app.shiro.realm.ShiroDbRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro配置类
 *
 * @author flybear
 * @since 2019/12/19 23:34:24
 */
@Configuration
public class ShiroConfig {

    @Value("${file-path.url}")
    public String FILE_URL;

    /**
     * 过滤器及映射路径的配置
     *
     * @param securityManager WebSecurityManager
     * @return ShiroFilterFactoryBean
     */
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(WebSecurityManager securityManager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();

        factoryBean.setSecurityManager(securityManager);
        factoryBean.setLoginUrl("/public/unauthorized");
        factoryBean.setUnauthorizedUrl("/public/noAccess");

        // 资源白名单
        String[] anonUri = {
                "/",
                "/public/login",
                "/public/unauthorized/**",
                FILE_URL + "/**"
        };
        Map<String, String> map = new LinkedHashMap<>(anonUri.length);
        for (String uri : anonUri) {
            map.put(uri, "anon");
        }
        factoryBean.setFilterChainDefinitionMap(map);

        Map<String, Filter> filterMap = new LinkedHashMap<>(1);
        // 设置自定义的JWT过滤器
        filterMap.put("jwt", new JWTFilter());
        factoryBean.setFilters(filterMap);
        // 所有请求通过JWT Filter
        map.put("/**", "jwt");
        return factoryBean;
    }

    /**
     * 安全管理模块，所有的manager在此配置
     *
     * @param shiroDbRealm ShiroDbRealm
     * @return WebSecurityManager
     */
    @Bean
    public WebSecurityManager securityManager(ShiroDbRealm shiroDbRealm) {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(shiroDbRealm);
        return manager;
    }

    /**
     * 凭证匹配器
     *
     * @return HashedCredentialsMatcher
     */
    private HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        // 散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        // 散列的次数，比如散列两次，相当于 md5(md5(""));
        hashedCredentialsMatcher.setHashIterations(2);
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        return hashedCredentialsMatcher;
    }

    /**
     * 自定义realm
     *
     * @return ShiroDbRealm
     */
    @Bean
    public ShiroDbRealm shiroDbRealm() {
        ShiroDbRealm shiroDbRealm = new ShiroDbRealm();
        shiroDbRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return shiroDbRealm;
    }

    /**
     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator)和AuthorizationAttributeSourceAdvisor)即可实现此功能
     *
     * @return DefaultAdvisorAutoProxyCreator
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(ShiroDbRealm shiroDbRealm) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager(shiroDbRealm));
        return authorizationAttributeSourceAdvisor;
    }
}
