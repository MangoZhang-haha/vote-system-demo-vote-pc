package flybear.hziee.app.consts;

/**
 * 正则表达式常量类
 *
 * @author qiancj
 * @since 2020/1/10 22:12
 */
public class PatternConst {
    /**
     * 用户密码：由6~14位数字或大小写字母组成
     */
    public static final String PASSWORD = "^[a-zA-Z0-9]{6,14}$";
    /**
     * 真实姓名：中文姓名
     */
    public static final String REAL_NAME = "^[\\u4E00-\\u9FA5]{2,7}$";
    /**
     * 手机验证码：4位数字
     */
    public static final String VCODE = "^[0-9]{4}$";
    /**
     * 手机号
     */
    public static final String PHONE = "^1[3456789][0-9]{9}$";

    /**
     * 角色标识：只能由英文和数字组成
     */
    public static final String ROLE = "^[a-zA-Z0-9]*$";
    /**
     * 权限标识：只能由英文、数字和冒号组成
     */
    public static final String PERMISSION = "^[a-zA-Z0-9:]*$";
}
