package com.swf.attence.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import java.util.Objects;

/**
 * <p>
 * 
 * </p>
 *
 * @author auto-genergator123
 * @since 2019-02-28
 */
@TableName("user_msg")
public class UserMsg implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户工号 至少8位 
     */
    private String userid;
    /**
     * 用户姓名
     */
    private String username;
    /**
     * 用户性别 0 女 1 男
     */
    private Integer gender;
    /**
     * 用户部门id 外键连接 部门表
     */
    private String deptid;
    /**
     * 用户照片存放路径
     */
    private String userpic;
    /**
     * 用户个人密码
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String userpassword;

    @TableField(exist = false)
    private DeptMsg dept;

    public DeptMsg getDept() {
        return dept;
    }

    public void setDept(DeptMsg dept) {
        this.dept = dept;
    }


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

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getDeptid() {
        return deptid;
    }

    public void setDeptid(String deptid) {
        this.deptid = deptid;
    }

    public String getUserpic() {
        return userpic;
    }

    public void setUserpic(String userpic) {
        this.userpic = userpic;
    }

    public String getUserpassword() {
        return userpassword;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }

    public static final String ID = "id";

    public static final String USERID = "userid";

    public static final String USERNAME = "username";

    public static final String GENDER = "gender";

    public static final String DEPTID = "deptid";

    public static final String USERPIC = "userpic";

    public static final String USERPASSWORD = "userpassword";

    @Override
    public String toString() {
        return "UserMsg{" +
        ", id=" + id +
        ", userid=" + userid +
        ", username=" + username +
        ", gender=" + gender +
        ", deptid=" + deptid +
        ", userpic=" + userpic +
        ", userpassword=" + userpassword +
        "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserMsg userMsg = (UserMsg) o;
        return Objects.equals(id, userMsg.id) &&
                Objects.equals(userid, userMsg.userid) &&
                Objects.equals(username, userMsg.username) &&
                Objects.equals(gender, userMsg.gender) &&
                Objects.equals(deptid, userMsg.deptid) &&
                Objects.equals(userpic, userMsg.userpic) &&
                Objects.equals(userpassword, userMsg.userpassword) &&
                Objects.equals(dept, userMsg.dept);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, userid, username, gender, deptid, userpic, userpassword, dept);
    }
}
