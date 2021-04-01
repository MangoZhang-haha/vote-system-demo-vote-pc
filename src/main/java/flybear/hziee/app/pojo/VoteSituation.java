package flybear.hziee.app.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Mango
 * @Date: 2021/3/24 15:46:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * 已投情况 页面
 */
public class VoteSituation implements Serializable {

    private static final long serialVersionUID = 71841684237304666L;

    private Long ownerID;

    private String ownerName;

    private String avatarUrl;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date voteTime;

    private String candidateName;
}
