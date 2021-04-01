package flybear.hziee.app.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Mango
 * @Date: 2021/3/20 16:01:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
/**
 * 业主委员会 页面（主页）
 */
public class OwnerInfo implements Serializable {

    private static final long serialVersionUID = -5401824181897550363L;

    private Long communityID;

    private String communityName;

    private Long buildingID;

    private String buildingName;

    private Integer unit;

    private String unitName;

    private Long houseID;

    private String houseNumber;

    private String addrDetail;

    private String ownerID;

    private String ownerName;

    private Integer roleType;

    private String faceID;

    private Boolean whetherAuth;

    private String avatarUrl;
}
