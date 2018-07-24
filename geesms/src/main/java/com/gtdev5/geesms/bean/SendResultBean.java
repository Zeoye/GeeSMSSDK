package com.gtdev5.geesms.bean;

public class SendResultBean {
    String errcode;
    String errmsg;
    String taskno;

    public String getTaskno() {
        return taskno;
    }

    public void setTaskno(String taskno) {
        this.taskno = taskno;
    }


    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}
