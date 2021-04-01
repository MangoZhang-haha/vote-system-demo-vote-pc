package flybear.hziee.app.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "vote_ev")
public class VoteEv implements Serializable {
    public static final String COL_APPLY_STATIS = "apply_statis";
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 投票项目id
     */
    @TableField(value = "vote_id")
    private Long voteId;

    /**
     * V+id（id不满9位补零）
     */
    @TableField(value = "application_id")
    private String applicationId;

    /**
     * 审核状态0 审核中 1 已通过 2 未通过
     */
    @TableField(value = "apply_status")
    private Integer applyStatus;

    @TableField(value = "operator_id")
    private Long operatorId;

    /**
     * 逻辑删除状态
     */
    @TableField(value = "deleted")
    private Boolean deleted;

    /**
     * 创建时间
     */
    @TableField(value = "gmt_create", fill = FieldFill.INSERT)
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @TableField(value = "gmt_modified", fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    @TableField(exist = false)
    private String title;

    @TableField(exist = false)
    private Date startTime;

    @TableField(exist = false)
    private Date endTime;

    @TableField(exist = false)
    private String voteTypeName;

    @TableField(exist = false)
    private String operatorName;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_VOTE_ID = "vote_id";

    public static final String COL_APPLICATION_ID = "application_id";

    public static final String COL_APPLY_STATUS = "apply_status";

    public static final String COL_OPERATOR_ID = "operator_id";

    public static final String COL_DELETED = "deleted";

    public static final String COL_GMT_CREATE = "gmt_create";

    public static final String COL_GMT_MODIFIED = "gmt_modified";
}