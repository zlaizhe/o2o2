package com.imooc.o2o.entity;

import java.util.Date;

//头条
public class HeadLine {
    private Long lineId;//id
    private String lineName;//头条名
    private String lineLink;//头条链接
    private String lineImg;//头条图片
    private Integer priority;//头条优先级
    private Integer enableStatus;//状态，0：不可用，1：可用
    private Date createTime;//创建时间
    private Date lastEditTime;//最后更新时间

    public Long getLineId() {
        return lineId;
    }

    public void setLineId(Long lineId) {
        this.lineId = lineId;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getLineLink() {
        return lineLink;
    }

    public void setLineLink(String lineLink) {
        this.lineLink = lineLink;
    }

    public String getLineImg() {
        return lineImg;
    }

    public void setLineImg(String lineImg) {
        this.lineImg = lineImg;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getEnableStatus() {
        return enableStatus;
    }

    public void setEnableStatus(Integer enableStatus) {
        this.enableStatus = enableStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastEditTime() {
        return lastEditTime;
    }

    public void setLastEditTime(Date lastEditTime) {
        this.lastEditTime = lastEditTime;
    }

    @Override
    public String toString() {
        return "HeadLine{" +
                "lineId=" + lineId +
                ", lineName='" + lineName + '\'' +
                ", lineLink='" + lineLink + '\'' +
                ", lineImg='" + lineImg + '\'' +
                ", priority=" + priority +
                ", enableStatus=" + enableStatus +
                ", createTime=" + createTime +
                ", lastEditTime=" + lastEditTime +
                '}';
    }
}
