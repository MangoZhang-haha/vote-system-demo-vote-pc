package flybear.hziee.app.controller;

import flybear.hziee.app.anno.Log;
import flybear.hziee.app.entity.User;
import flybear.hziee.app.enums.LogModuleEnum;
import flybear.hziee.app.shiro.token.PhoneCodeToken;
import flybear.hziee.app.entity.Result;
import flybear.hziee.app.enums.ResultEnum;
import flybear.hziee.app.util.JWTUtil;
import flybear.hziee.app.util.LogUtil;
import flybear.hziee.app.util.ResultUtil;
import flybear.hziee.app.util.ValidUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;

/**
 * 公共控制器
 *
 * @author flybear
 * @since 2019/12/20 0:06
 */
@RequestMapping("public")
@RestController
public class PublicController {

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @param type 登录类型（0：用户名登录；1：手机登录）
     * @return Token
     */
    @PostMapping("login")
    public Result<String> login(String username, String password, @RequestParam(defaultValue = "0") byte type) {
        Subject subject = SecurityUtils.getSubject();
        try {
            if (type == 0) {
                ValidUtil.isNotEmpty(username, "用户名不能为空");
                ValidUtil.isNotEmpty(password, "密码不能为空");
                subject.login(new UsernamePasswordToken(username, password));
            } else {
                ValidUtil.isNotEmpty(username, "手机号不能为空");
                ValidUtil.isNotEmpty(password, "验证码不能为空");
                subject.login(new PhoneCodeToken(username, password));
            }
        } catch (Exception e) {
            return ResultUtil.error(e.getMessage());
        }
        LogUtil.log(
                LogUtil.getIpAddress(),
                (User) subject.getPrincipal(),
                "flybear.hziee.app.controller.PublicController.login()",
                LogModuleEnum.LOGIN,
                "登录"
        );
        return ResultUtil.success(JWTUtil.createToken(((User) subject.getPrincipal()).getUsername()));
    }

    @Log(value = "登出", module = LogModuleEnum.LOGOUT)
    @PostMapping("logout")
    public Result logout() {
        SecurityUtils.getSubject().logout();
        return ResultUtil.success();
    }
    @RequestMapping("unauthorized")
    public Result<String> unauthorized() {
        return ResultUtil.error(ResultEnum.UN_AUTH);
    }
    @RequestMapping("unauthorized/{msg}")
    public Result<String> unauthorized(@PathVariable("msg") String msg) {
        return ResultUtil.error(msg);
    }
    @RequestMapping("noAccess")
    public Result noAccess() {
        return ResultUtil.error(ResultEnum.NO_ACCESS);
    }
}
