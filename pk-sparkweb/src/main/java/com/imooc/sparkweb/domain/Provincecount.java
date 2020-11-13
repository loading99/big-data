package com.imooc.sparkweb.domain;

public class Provincecount {

    private String day;
    private String province;
    private Long count;

    @Override
    public String toString() {
        return "Provincecount{" +
                "day='" + day + '\'' +
                ", province='" + province + '\'' +
                ", count=" + count +
                '}';
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
