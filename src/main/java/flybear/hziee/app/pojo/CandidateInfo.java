package flybear.hziee.app.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author Mango
 * @Date: 2021/3/24 15:48:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * 选手详情 页面
 */
public class CandidateInfo implements Serializable {

    private static final long serialVersionUID = -1535500190414435012L;

    private Long candidateTableID;

    private String candidateName;

    private String introduce;

    private List<String> picUrls;

    private Integer voteNum;
}
