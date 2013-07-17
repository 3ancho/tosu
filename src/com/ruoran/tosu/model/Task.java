package com.ruoran.tosu.model;

import java.util.Date;

public class Task {
    
    public static final int ACTIVE = 0;
    public static final int IDLE = 1;
    public static final int DONE = 2;

    private String key;
    private String content;
    private Integer timeUnitPlaned;
    private Integer timeUnitUsed;
    private Date finishDate;
    private Date creationDate;
    private Date startDate;
    private int secLeft;
    private int secPassed;
    private int status; 
    private String summary;
    
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public Date getFinishDate() {
        return finishDate;
    }
    public void setFinishDate(Date doneDate) {
        this.finishDate = doneDate;
    }
    public Integer getTimeUnitUsed() {
        return timeUnitUsed;
    }
    public void setTimeUnitUsed(Integer timeUnitUsed) {
        this.timeUnitUsed = timeUnitUsed;
    }
    public Integer getTimeUnitPlaned() {
        return timeUnitPlaned;
    }
    public void setTimeUnitPlaned(Integer timeUnitPlaned) {
        this.timeUnitPlaned = timeUnitPlaned;
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getSummary() {
        return summary;
    }
    public void setSummary(String summary) {
        this.summary = summary;
    }
    public Date getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public int getSecLeft() {
        return secLeft;
    }
    public void setSecLeft(int secLeft) {
        this.secLeft = secLeft;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public int getSecPassed() {
        return secPassed;
    }
    public void setSecPassed(int secPassed) {
        this.secPassed = secPassed;
    }
    
}