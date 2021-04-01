package flybear.hziee.app.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import flybear.hziee.app.entity.Vote;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Mango
 * @Date: 2021/3/24 15:56:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * 投票 页面
 */
public class VoteTotalInfo implements Serializable {

    private static final long serialVersionUID = 5006111713418688980L;

    private Vote vote;

    private List<CandidateInfo> candidateInfos;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CandidateInfo {

        private Long candidateTableID;

        private String candidateName;

        private String picUrl;

        private Integer voteNum;
    }
}
