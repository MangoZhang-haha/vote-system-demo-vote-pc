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
@TableName(value = "`owner`")
public class Owner implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 业主id，是业主oid为0
     */
    @TableField(value = "oid")
    private Long oid;

    /**
     * 人脸识别标识
     */
    @TableField(value = "face_id")
    private String faceId;

    /**
     * 姓名
     */
    @TableField(value = "owner_name")
    private String ownerName;

    /**
     * 0 女 1 男
     */
    @TableField(value = "gender")
    private String gender;

    /**
     * 手机号
     */
    @TableField(value = "mobilephone")
    private String mobilephone;

    /**
     * 家庭电话
     */
    @TableField(value = "telephone")
    private byte[] telephone;

    /**
     * 户口类型
     */
    @TableField(value = "account_type")
    private Integer accountType;

    /**
     * 多个卡号以 "|" 分割
     */
    @TableField(value = "bank_cards")
    private String bankCards;

    /**
     * 最近登陆时间
     */
    @TableField(value = "lastest_login_time")
    private Date lastestLoginTime;

    /**
     * 登陆次数
     */
    @TableField(value = "login_times")
    private Long loginTimes;

    /**
     * QQ号
     */
    @TableField(value = "qq_number")
    private String qqNumber;

    /**
     * 微信号
     */
    @TableField(value = "wechat_number")
    private String wechatNumber;

    /**
     * 邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * 身份证号
     */
    @TableField(value = "id_number")
    private String idNumber;

    /**
     * 角色类型（1 业主 2 业主委员）
     */
    @TableField(value = "role_type")
    private Integer roleType;

    /**
     * 民族
     */
    @TableField(value = "nation")
    private String nation;

    /**
     * 政治面貌
     */
    @TableField(value = "politic_countenance")
    private String politicCountenance;

    /**
     * 户籍所在地
     */
    @TableField(value = "registered_residence")
    private String registeredResidence;

    /**
     * 头像地址
     */
    @TableField(value = "avatar_url")
    private String avatarUrl;

    @TableField(value = "deleted")
    private Boolean deleted;

    @TableField(value = "gmt_create")
    private Date gmtCreate;

    @TableField(value = "gmt_modified")
    private Date gmtModified;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_OID = "oid";

    public static final String COL_FACE_ID = "face_id";

    public static final String COL_OWNER_NAME = "owner_name";

    public static final String COL_GENDER = "gender";

    public static final String COL_MOBILEPHONE = "mobilephone";

    public static final String COL_TELEPHONE = "telephone";

    public static final String COL_ACCOUNT_TYPE = "account_type";

    public static final String COL_BANK_CARDS = "bank_cards";

    public static final String COL_LASTEST_LOGIN_TIME = "lastest_login_time";

    public static final String COL_LOGIN_TIMES = "login_times";

    public static final String COL_QQ_NUMBER = "qq_number";

    public static final String COL_WECHAT_NUMBER = "wechat_number";

    public static final String COL_EMAIL = "email";

    public static final String COL_ID_NUMBER = "id_number";

    public static final String COL_ROLE_TYPE = "role_type";

    public static final String COL_NATION = "nation";

    public static final String COL_POLITIC_COUNTENANCE = "politic_countenance";

    public static final String COL_REGISTERED_RESIDENCE = "registered_residence";

    public static final String COL_AVATAR_URL = "avatar_url";

    public static final String COL_DELETED = "deleted";

    public static final String COL_GMT_CREATE = "gmt_create";

    public static final String COL_GMT_MODIFIED = "gmt_modified";
}