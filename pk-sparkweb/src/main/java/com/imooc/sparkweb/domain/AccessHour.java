package com.imooc.sparkweb.domain;

public class AccessHour {

    String hour;
    String user;
    Double time;

    @Override
    public String toString() {
        return "AccessHour{" +
                "hour='" + hour + '\'' +
                ", user='" + user + '\'' +
                ", time=" + time +
                '}';
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Double getTime() {
        return time;
    }

    public void setTime(Double time) {
        this.time = time;
    }
}
