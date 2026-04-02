package com.example.test.ai.bean;

/**
 * @author yanteng
 * @date 2026-03-20 10:04
 * @description
 */
public class DataCall {

    public Contact contact;
    public String phoneType;

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "DataCall{" +
                "contact=" + contact +
                ", phoneType='" + phoneType + '\'' +
                '}';
    }
}
