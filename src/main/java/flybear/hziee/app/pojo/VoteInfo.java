package flybear.hziee.app.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Mango
 * @Date: 2021/3/24 14:24:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
/**
 * 投票详情 copy 页面
 */
public class VoteInfo implements Serializable {

    private static final long serialVersionUID = 4687000498031651614L;

    private Long voteID;

    private String title;

    private String introduce;

    /**
     * 是否匿名(0 不匿名 1 匿名)
     */
    private Boolean whetherAnonym;

    @JsonFormat(pattern = "yyy-MM-dd HH:mm:ss")
    private Date startTime;

    @JsonFormat(pattern = "yyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 投票候选人数量
     */
    private Integer candidateNum;

    /**
     * 总的投票数量
     */
    private Integer totalVoteNum;

    /**
     * 访问数量
     */
    private Long visitNum;

    /**
     * 排名靠前的人数
     */
    private List<Top> tops;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Top implements Serializable {

        private Long candidateTableID;

        private String candidateName;

        private Integer voteNum;
    }
}
