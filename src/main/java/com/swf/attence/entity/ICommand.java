package com.swf.attence.entity;


import java.io.Serializable;
import java.util.Objects;

/**
 * @author : white.hou
 * @description : 报警表的实体类
 * @date: 2019/1/29_11:25
 */

public class ICommand implements Serializable {

    private static final long serialVersionUID = 2939567226722940553L;
    private String id;

    private String icommandTime;

    private String icommandCameraid;

    private String icommandUserid;

    private String icommandUsername;

    public ICommand() {

    }

    public ICommand(String icommandTime, String icommandCameraid, String icommandUserid, String icommandUsername) {
        this.icommandTime = icommandTime;
        this.icommandCameraid = icommandCameraid;
        this.icommandUserid = icommandUserid;
        this.icommandUsername = icommandUsername;
    }

    @Override
    public String toString() {
        return "ICommand{" +
                "id=" + id +
                ", icommandTime=" + icommandTime +
                ", icommandCameraid='" + icommandCameraid + '\'' +
                ", icommandUserid='" + icommandUserid + '\'' +
                ", icommandUsername='" + icommandUsername + '\'' +
                '}';
    }

    public ICommand(String id, String icommandTime, String icommandCameraid, String icommandUserid, String icommandUsername) {
        this.id = id;
        this.icommandTime = icommandTime;
        this.icommandCameraid = icommandCameraid;
        this.icommandUserid = icommandUserid;
        this.icommandUsername = icommandUsername;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIcommandTime() {
        return icommandTime;
    }

    public void setIcommandTime(String icommandTime) {
        this.icommandTime = icommandTime;
    }

    public String getIcommandCameraid() {
        return icommandCameraid;
    }

    public void setIcommandCameraid(String icommandCameraid) {
        this.icommandCameraid = icommandCameraid;
    }

    public String getIcommandUserid() {
        return icommandUserid;
    }

    public void setIcommandUserid(String icommandUserid) {
        this.icommandUserid = icommandUserid;
    }

    public String getIcommandUsername() {
        return icommandUsername;
    }

    public void setIcommandUsername(String icommandUsername) {
        this.icommandUsername = icommandUsername;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ICommand iCommand = (ICommand) o;
        return Objects.equals(id, iCommand.id) &&
                Objects.equals(icommandTime, iCommand.icommandTime) &&
                Objects.equals(icommandCameraid, iCommand.icommandCameraid) &&
                Objects.equals(icommandUserid, iCommand.icommandUserid) &&
                Objects.equals(icommandUsername, iCommand.icommandUsername);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, icommandTime, icommandCameraid, icommandUserid, icommandUsername);
    }
}