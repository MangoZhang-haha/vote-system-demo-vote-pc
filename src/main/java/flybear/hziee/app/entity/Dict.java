package flybear.hziee.app.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 字典表
 *
 * @author flybear
 * @since 2020/1/1 18:35
 */
@Data
public class Dict {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 标识
     */
    @Length(max = 50, message = "字典标识不能超过20字")
    private String code;

    /**
     * 标签
     */
    @Length(max = 50, message = "字典标签不能超过50字")
    private String label;

    /**
     * 值
     */
    @Length(max = 150, message = "字典值不能超过150字")
    private String value;

    /**
     * 可编辑
     */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    @NotNull(message = "是否可编辑不能为空")
    private Boolean editable;

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
