package com.example.test.eventbus;

public class EventCenter {
    public static final int PUBLISH_PUNCH_SUCCESS = 1;
    private int eventType;
    private String bundleId;

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    public static EventCenter call(int eventType) {
        EventCenter center = new EventCenter();
        center.setEventType(eventType);
        return center;
    }

    public static EventCenter call(int eventType, String bundleId) {
        EventCenter center = new EventCenter();
        center.setEventType(eventType);
        center.setBundleId(bundleId);
        return center;
    }
}
