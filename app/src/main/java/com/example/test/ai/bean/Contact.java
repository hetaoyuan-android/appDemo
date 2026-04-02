package com.example.test.ai.bean;

/**
 * 作者: yuanhetao
 * 时间: 2026/3/25
 * 描述:
 */
public class Contact {

    public String job;
    public String department;
    public String search_text;
    public String id;
    public String name;
    public String work_phone;
    public String mobile_phone;

    @Override
    public String toString() {
        return "Contact{" +
                "job='" + job + '\'' +
                ", department='" + department + '\'' +
                ", search_text='" + search_text + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", work_phone='" + work_phone + '\'' +
                ", mobile_phone='" + mobile_phone + '\'' +
                '}';
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSearch_text() {
        return search_text;
    }

    public void setSearch_text(String search_text) {
        this.search_text = search_text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWork_phone() {
        return work_phone;
    }

    public void setWork_phone(String work_phone) {
        this.work_phone = work_phone;
    }

    public String getMobile_phone() {
        return mobile_phone;
    }

    public void setMobile_phone(String mobile_phone) {
        this.mobile_phone = mobile_phone;
    }
}
