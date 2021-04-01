package flybear.hziee.app.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import flybear.hziee.app.consts.PatternConst;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.security.Principal;
import java.util.Date;
import java.util.Set;

/**
 * 用户表
 *
 * @author flybear
 * @since 2019/12/19 23:34:24
 */
@Data
public class User implements Principal {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    @NotEmpty(message = "用户名不能为空")
    @Length(max = 20, message = "用户名不能超过20字")
    private String username;

    /**
     * 密码
     */
    @JSONField(serialize = false)
    @NotEmpty(message = "密码不能为空")
    private String password;

    /**
     * 盐值
     */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    @JSONField(serialize = false)
    private String salty;

    /**
     * 真实姓名
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    @Length(max = 7, message = "真实姓名不能超过7字")
    @Pattern(regexp = PatternConst.REAL_NAME + "|", message = "请输入正确的中文姓名")
    private String realName;

    /**
     * 头像地址
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String avatar;

    /**
     * 手机号
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    @Pattern(regexp = PatternConst.PHONE + "|", message = "手机号码不正确")
    private String phone;

    /**
     * 启用
     */
    @NotNull(message = "是否启用不能为空")
    private Boolean active;

    /**
     * 已删除
     */
    @TableLogic
    private Boolean deleted;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date gmtModified;

    /**
     * 角色列表
     */
    @TableField(exist = false)
    private Set<Role> roles;

    /**
     * 菜单列表
     */
    @TableField(exist = false)
    private Set<Menu> menus;

    @Override
    @JSONField(serialize = false)
    public String getName() {
        return this.username;
    }
}
