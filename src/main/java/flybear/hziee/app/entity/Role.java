package flybear.hziee.app.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import flybear.hziee.app.consts.PatternConst;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.Set;

/**
 * 角色表
 *
 * @author flybear
 * @since 2019/12/19 23:34:24
 */
@Data
@EqualsAndHashCode(of = "id")
public class Role {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    @NotEmpty(message = "角色名称不能为空")
    @Length(max = 20, message = "角色名称不能超过20字")
    private String name;

    /**
     * 描述
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    @Length(max = 200, message = "角色描述不能超过200字")
    private String description;

    /**
     * 角色标识
     */
    @NotEmpty(message = "角色标识不能为空")
    @Length(max = 200, message = "角色标识不能超过200字")
    @Pattern(regexp = PatternConst.ROLE, message = "角色标识只能由英文和数字组成")
    private String role;

    /**
     * 启用
     */
    @NotNull(message = "是否启用不能为空")
    private Boolean active;

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
     * 菜单列表
     */
    @TableField(exist = false)
    private Set<Menu> menus;

}
