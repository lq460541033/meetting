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
 * @since 2019-03-01
 */
@TableName("leave_msg")
public class LeaveMsg implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    /**
     * 用户工号
     */
    private String username;
    /**
     * 请假开始时间
     */
    @TableField("fail_start")
    private String failStart;
    /**
     * 请假结束时间
     */
    @TableField("fail_end")
    private String failEnd;
    /**
     * 请假原因
     */
    private String description;
    /**
     * 请假是否通过，1 通过 0未通过
     */
    private Integer access;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFailStart() {
        return failStart;
    }

    public void setFailStart(String failStart) {
        this.failStart = failStart;
    }

    public String getFailEnd() {
        return failEnd;
    }

    public void setFailEnd(String failEnd) {
        this.failEnd = failEnd;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAccess() {
        return access;
    }

    public void setAccess(Integer access) {
        this.access = access;
    }

    public static final String ID = "id";

    public static final String USERNAME = "username";

    public static final String FAIL_START = "fail_start";

    public static final String FAIL_END = "fail_end";

    public static final String DESCRIPTION = "description";

    public static final String ACCESS = "access";

    @Override
    public String toString() {
        return "LeaveMsg{" +
        ", id=" + id +
        ", username=" + username +
        ", failStart=" + failStart +
        ", failEnd=" + failEnd +
        ", description=" + description +
        ", access=" + access +
        "}";
    }
}
