package com.srihari.ruasportal;

public class notifications_adapter {

    String Title;
    String Time;
    String Body;

    public notifications_adapter() {
    }

    public notifications_adapter(String title,String time, String body) {
        Title = title;
        Time = time;
        Body = body;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getBody() {
        return Body;
    }

    public void setBody(String body) {
        Body = body;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
