package com.xd.activiti.entity;

import java.io.Serializable;

public class LeaveInfo implements Serializable {

    private static final long serialVersionUID = 7087291099372864545L;

    private String applicant;

    private Integer day;

    private String reason;

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
