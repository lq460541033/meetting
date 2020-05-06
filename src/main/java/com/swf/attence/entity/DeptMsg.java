package com.swf.attence.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
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
@TableName("dept_msg")
public class DeptMsg implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 部门id
     */
    private String deptid;
    /**
     * 部门名称
     */
    private String deptname;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeptid() {
        return deptid;
    }

    public void setDeptid(String deptid) {
        this.deptid = deptid;
    }

    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }

    public static final String ID = "id";

    public static final String DEPTID = "deptid";

    public static final String DEPTNAME = "deptname";

    @Override
    public String toString() {
        return "DeptMsg{" +
        ", id=" + id +
        ", deptid=" + deptid +
        ", deptname=" + deptname +
        "}";
    }
}
