package flybear.hziee.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName(value = "vote_system_demo.vote_candidate")
public class VoteCandidate implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 投票id
     */
    @TableField(value = "vote_id")
    private Long voteId;

    /**
     * 被选举人姓名
     */
    @TableField(value = "candidate_name")
    private String candidateName;

    /**
     * 介绍
     */
    @TableField(value = "introduce")
    private String introduce;

    /**
     * 被选举人相关的图片url(以 '&-!&' 分割)
     */
    @TableField(value = "pic_urls")
    private String picUrls;

    @TableField(value = "deleted")
    private Boolean deleted;

    @TableField(value = "gmt_create")
    private Date gmtCreate;

    @TableField(value = "gmt_modified")
    private Date gmtModified;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_VOTE_ID = "vote_id";

    public static final String COL_CANDIDATE_NAME = "candidate_name";

    public static final String COL_INTRODUCE = "introduce";

    public static final String COL_PIC_URLS = "pic_urls";

    public static final String COL_DELETED = "deleted";

    public static final String COL_GMT_CREATE = "gmt_create";

    public static final String COL_GMT_MODIFIED = "gmt_modified";
}