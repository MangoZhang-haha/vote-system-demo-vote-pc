package flybear.hziee.app.shiro.realm;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import flybear.hziee.app.entity.Menu;
import flybear.hziee.app.entity.Role;
import flybear.hziee.app.entity.User;
import flybear.hziee.app.entity.dto.UserInfo;
import flybear.hziee.app.enums.ResultEnum;
import flybear.hziee.app.service.UserService;
import flybear.hziee.app.shiro.token.JWTToken;
import flybear.hziee.app.shiro.token.PhoneCodeToken;
import flybear.hziee.app.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * @author flybear
 * @since 2019/12/19 23:34:24
 */
@Slf4j
public class ShiroDbRealm extends AuthorizingRealm {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 测试用验证码
     */
    String vCode = "1234";

    /**
     * 权限配置
     *
     * @param principals PrincipalCollection
     * @return AuthorizationInfo
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        User user = (User) principals.getPrimaryPrincipal();
//        log.info("----------------------------->" + user);
        for (Role role : user.getRoles()) {
            if (role.getRole() != null) {
                if (!StringUtils.isEmpty(role.getRole())) {
                    authorizationInfo.addRole(role.getRole());
                }
            }
        }
        for (Menu menu : user.getMenus()) {
            if (menu.getPerms() != null) {
                if (!StringUtils.isEmpty(menu.getPerms())) {
                    authorizationInfo.addStringPermission(menu.getPerms());
                }
            }
        }

//        log.info("用户" + user.getUsername() + "具有的角色：" + authorizationInfo.getRoles());
//        log.info("用户" + user.getUsername() + "具有的权限：" + authorizationInfo.getStringPermissions());

        return authorizationInfo;
    }

    /**
     * 获取身份验证相关信息
     *
     * @param token AuthenticationToken
     * @return AuthenticationInfo
     * @throws AuthenticationException AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        User user;
        Md5Hash md5Hash;
        String principal = token.getPrincipal().toString();
        String credentials = token.getCredentials().toString();

        UserService userService = (UserService) applicationContext.getBean("userService");


        if (token instanceof JWTToken) {
            if (principal == null || !JWTUtil.verify(credentials, JWTUtil.getUsername(principal))) {
                throw new AuthenticationException(ResultEnum.TOKEN_ERROR.getMsg());
            }
            user = Optional.ofNullable(userService.getOne(
                    Wrappers.<User>lambdaQuery().eq(User::getUsername, JWTUtil.getUsername(principal))
            )).orElseThrow(() -> new UnknownAccountException(ResultEnum.NOT_EXIST_USERNAME.getMsg()));
        } else if (token instanceof PhoneCodeToken) {
            // 通过手机登录
            user = Optional.ofNullable(userService.getOne(
                    Wrappers.<User>lambdaQuery().eq(User::getPhone, principal)
            )).orElseThrow(() -> new UnknownAccountException(ResultEnum.NOT_EXIST_PHONE.getMsg()));
            if (!vCode.equals(credentials)) {
                throw new IncorrectCredentialsException(ResultEnum.ERROR_VCODE.getMsg());
            }
        } else if (token instanceof UsernamePasswordToken) {
            // 通过用户名登录
            user = Optional.ofNullable(userService.getOne(
                    Wrappers.<User>lambdaQuery().eq(User::getUsername, principal)
            )).orElseThrow(() -> new UnknownAccountException(ResultEnum.NOT_EXIST_USERNAME.getMsg()));
            md5Hash = new Md5Hash(new Md5Hash(token.getCredentials(), user.getSalty()), user.getSalty());
            if (!user.getPassword().equals(md5Hash.toString())) {
                throw new IncorrectCredentialsException(ResultEnum.ERROR_PASSWORD.getMsg());
            }
        } else {
            throw new RuntimeException("不存在该Token类：" + token.getClass());
        }

        // 验证账户是否可用
        if (!user.getActive()) {
            throw new LockedAccountException(ResultEnum.ACCOUNT_LOCKED.getMsg());
        }

        UserInfo userInfo = userService.getUserInfo(user.getId());
        user.setRoles(userInfo.getRoles());
        user.setMenus(userInfo.getMenus());

        // 获得认证信息
        return new SimpleAuthenticationInfo(
                user,
                new Md5Hash(new Md5Hash(token.getCredentials(), user.getSalty())),
                ByteSource.Util.bytes(user.getSalty()),
                getName());
    }


    /**
     * 必须重写此方法，不然会报错
     *
     * @param token AuthenticationToken
     * @return 是否支持此Token类
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken
                || token instanceof PhoneCodeToken
                || token instanceof UsernamePasswordToken;
    }


}
