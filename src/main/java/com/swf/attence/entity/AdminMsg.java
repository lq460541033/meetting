package com.swf.attence.entity;

import com.baomidou.mybatisplus.enums.IdType;
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
@TableName("admin_msg")
public class AdminMsg implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 管理员账号
     */
    private String adminid;
    /**
     * 管理员密码
     */
    private String password;
    /**
     * 管理员身份属性
     */
    @TableField("admin_identity")
    private String adminIdentity;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAdminid() {
        return adminid;
    }

    public void setAdminid(String adminid) {
        this.adminid = adminid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAdminIdentity() {
        return adminIdentity;
    }

    public void setAdminIdentity(String adminIdentity) {
        this.adminIdentity = adminIdentity;
    }

    public static final String ID = "id";

    public static final String ADMINID = "adminid";

    public static final String PASSWORD = "password";

    public static final String ADMIN_IDENTITY = "admin_identity";

    @Override
    public String toString() {
        return "AdminMsg{" +
        ", id=" + id +
        ", adminid=" + adminid +
        ", password=" + password +
        ", adminIdentity=" + adminIdentity +
        "}";
    }
}
