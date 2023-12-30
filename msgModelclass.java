package com.example.mychat;

public class msgModelclass {

    String msg;
    String senderid;
    long timeStamp;

    public msgModelclass() {
    }

    public msgModelclass(String msg, String senderid, long timeStamp) {
        this.msg = msg;
        this.senderid = senderid;
        this.timeStamp = timeStamp;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSenderid() {
        return senderid;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
