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
@TableName("time_control")
public class TimeControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 事由 如会议  会议
     */
    @TableField("things_name")
    private String thingsName;
    @TableField("start_time")
    private Timestamp startTime;
    @TableField("end_time")
    private Timestamp endTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getThingsName() {
        return thingsName;
    }

    public void setThingsName(String thingsName) {
        this.thingsName = thingsName;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public static final String ID = "id";

    public static final String THINGS_NAME = "things_name";

    public static final String START_TIME = "start_time";

    public static final String END_TIME = "end_time";

    @Override
    public String toString() {
        return "TimeControl{" +
        ", id=" + id +
        ", thingsName=" + thingsName +
        ", startTime=" + startTime +
        ", endTime=" + endTime +
        "}";
    }
}
