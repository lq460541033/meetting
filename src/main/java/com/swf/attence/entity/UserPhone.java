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
@TableName("user_phone")
public class UserPhone implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    /**
     * 用户ID
     */
    private String userid;
    /**
     * 用户姓名
     */
    private String username;
    /**
     * 用户的手机号码
     */
    @TableField("user_phone")
    private String userPhone;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public static final String ID = "id";

    public static final String USERID = "userid";

    public static final String USERNAME = "username";

    public static final String USER_PHONE = "user_phone";

    @Override
    public String toString() {
        return "UserPhone{" +
        ", id=" + id +
        ", userid=" + userid +
        ", username=" + username +
        ", userPhone=" + userPhone +
        "}";
    }
}
