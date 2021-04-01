package flybear.hziee.app.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "vote")
public class Vote implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 创建人id
     */
    @TableField(value = "creator_id")
    private Long creatorId;

    /**
     * 标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 介绍
     */
    @TableField(value = "introduce")
    private String introduce;

    /**
     * 投票限制id
     */
    @TableField(value = "vote_limit_id")
    private Long voteLimitId;

    /**
     * 投票类型id
     */
    @TableField(value = "vote_type_id")
    private Long voteTypeId;

    /**
     * 开始时间
     */
    @TableField(value = "start_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 结束时间
     */
    @TableField(value = "end_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 是否匿名(0 不匿名 1 匿名)
     */
    @TableField(value = "whether_anonym")
    private Boolean whetherAnonym;

    /**
     * 是否可以委托亲友投票
     */
    @TableField(value = "whether_replace_by_relatives")
    private Boolean whetherReplaceByRelatives;

    /**
     * 是否是草稿
     */
    @TableField(value = "whether_draft")
    private Boolean whetherDraft;

    /**
     * 被通知的业主的id（以 "," 分割）
     */
    @TableField(value = "owner_noticed_ids")
    private String ownerNoticedIds;

    /**
     * 投票是否截止
     */
    @TableField(exist = false)
    private Boolean endOrNot;

    /**
     * 投票的数量
     */
    @TableField(exist = false)
    private Integer voteNumbers;

    /**
     * 访问量
     */
    @TableField(value = "visit_num")
    private Long visitNum;

    @TableField(value = "deleted")
    private Boolean deleted;

    @TableField(value = "gmt_create", fill = FieldFill.INSERT)
    private Date gmtCreate;

    @TableField(value = "gmt_modified", fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_CREATOR_ID = "creator_id";

    public static final String COL_TITLE = "title";

    public static final String COL_INTRODUCE = "introduce";

    public static final String COL_VOTE_LIMIT_ID = "vote_limit_id";

    public static final String COL_VOTE_TYPE_ID = "vote_type_id";

    public static final String COL_START_TIME = "start_time";

    public static final String COL_END_TIME = "end_time";

    public static final String COL_WHETHER_ANONYM = "whether_anonym";

    public static final String COL_WHETHER_REPLACE_BY_RELATIVES = "whether_replace_by_relatives";

    public static final String COL_WHETHER_DRAFT = "whether_draft";

    public static final String COL_OWNER_NOTICED_IDS = "owner_noticed_ids";

    public static final String COL_VISIT_NUM = "visit_num";

    public static final String COL_DELETED = "deleted";

    public static final String COL_GMT_CREATE = "gmt_create";

    public static final String COL_GMT_MODIFIED = "gmt_modified";
}