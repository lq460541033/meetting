package com.swf.attence.entity;

import com.baomidou.mybatisplus.enums.IdType;

import java.sql.Timestamp;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author auto-genergator
 * @since 2018-12-30
 */
@TableName("state_control")
public class StateControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 情况id
     */
    private Integer failid;
    /**
     * 情况名称
     */
    @TableField("state_name")
    private String stateName;
    /**
     * 开始时间
     */
    @TableField("begin_time")
    private Timestamp beginTime;
    /**
     * 结束时间
     */
    @TableField("close_time")
    private Timestamp closeTime;
    /**
     * 简单描述
     */
    private String description;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFailid() {
        return failid;
    }

    public void setFailid(Integer failid) {
        this.failid = failid;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Timestamp beginTime) {
        this.beginTime = beginTime;
    }

    public Date getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Timestamp closeTime) {
        this.closeTime = closeTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static final String ID = "id";

    public static final String FAILID = "failid";

    public static final String STATE_NAME = "state_name";

    public static final String BEGIN_TIME = "begin_time";

    public static final String CLOSE_TIME = "close_time";

    public static final String DESCRIPTION = "description";

    @Override
    public String toString() {
        return "StateControl{" +
        ", id=" + id +
        ", failid=" + failid +
        ", stateName=" + stateName +
        ", beginTime=" + beginTime +
        ", closeTime=" + closeTime +
        ", description=" + description +
        "}";
    }
}
