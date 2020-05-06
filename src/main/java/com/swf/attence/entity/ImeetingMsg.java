package com.swf.attence.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author auto-genergator123
 * @since 2019-03-31
 */
@TableName("imeeting_msg")
public class ImeetingMsg implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    /**
     * 报警时间
     */
    @TableField("imeeting_time")
    private String imeetingTime;
    /**
     * 报警来源：摄像头ip
     */
    @TableField("imeeting_cameraid")
    private String imeetingCameraid;
    /**
     * 用户工号 
     */
    @TableField("imeeting_userid")
    private String imeetingUserid;
    /**
     * 用户姓名
     */
    @TableField("imeeting_username")
    private String imeetingUsername;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImeetingTime() {
        return imeetingTime;
    }

    public void setImeetingTime(String imeetingTime) {
        this.imeetingTime = imeetingTime;
    }

    public String getImeetingCameraid() {
        return imeetingCameraid;
    }

    public void setImeetingCameraid(String imeetingCameraid) {
        this.imeetingCameraid = imeetingCameraid;
    }

    public String getImeetingUserid() {
        return imeetingUserid;
    }

    public void setImeetingUserid(String imeetingUserid) {
        this.imeetingUserid = imeetingUserid;
    }

    public String getImeetingUsername() {
        return imeetingUsername;
    }

    public void setImeetingUsername(String imeetingUsername) {
        this.imeetingUsername = imeetingUsername;
    }

    public static final String ID = "id";

    public static final String IMEETING_TIME = "imeeting_time";

    public static final String IMEETING_CAMERAID = "imeeting_cameraid";

    public static final String IMEETING_USERID = "imeeting_userid";

    public static final String IMEETING_USERNAME = "imeeting_username";

    @Override
    public String toString() {
        return "ImeetingMsg{" +
        ", id=" + id +
        ", imeetingTime=" + imeetingTime +
        ", imeetingCameraid=" + imeetingCameraid +
        ", imeetingUserid=" + imeetingUserid +
        ", imeetingUsername=" + imeetingUsername +
        "}";
    }
}
