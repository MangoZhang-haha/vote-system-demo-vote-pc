package flybear.hziee.app.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import flybear.hziee.app.consts.PatternConst;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.util.Date;

/**
 * 菜单表
 *
 * @author flybear
 * @since 2019/12/20 20:52
 */
@Data
@EqualsAndHashCode(of = "id")
public class Menu {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 父ID
     */
    private Long pid;

    /**
     * 名称
     */
    @NotEmpty(message = "菜单名称不能为空")
    @Length(max = 20, message = "菜单名称不能超过20字")
    private String name;

    /**
     * 请求地址
     */
    @Length(max = 200, message = "请求地址不能超过200字")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String url;

    /**
     * 组件名称
     */
    @Length(max = 200, message = "组件名称不能超过200字")
    private String component;

    /**
     * 图标
     */
    @Length(max = 200, message = "图标不能超过200字")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String icon;

    /**
     * 排序值
     */
    @NotNull(message = "排序值不能为空")
    @Max(value = 9999, message = "排序值不能大于9999")
    @Min(value = 0, message = "排序值不能小于0")
    private Integer sort;

    /**
     * 描述
     */
    @Length(max = 200, message = "描述不能超过200字")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String description;

    /**
     * 权限标识
     */
    @Length(max = 200, message = "权限标识不能超过200字")
    @Pattern(regexp = PatternConst.PERMISSION, message = "权限标识只能由英文、数字和冒号组成")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String perms;

    /**
     * 类型（0：目录；1：页面；2：按钮）
     */
    @NotNull(message = "菜单类型不能为空")
    private Integer type;

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

}
