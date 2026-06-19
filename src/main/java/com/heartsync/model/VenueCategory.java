package com.heartsync.model;

public enum VenueCategory {
    PARK("Park", "#22c55e"),
    RESTAURANT("Restaurant", "#f97316"),
    FAST_FOOD("Fast Food", "#eab308"),
    CAFE("Cafe", "#a78bfa"),
    ACTIVITY("Activity", "#ec4899"),
    CINEMA("Cinema", "#3b82f6"),
    MUSEUM("Museum", "#14b8a6"),
    ROOFTOP("Rooftop", "#f43f5e"),
    BAR("Bar", "#8b5cf6"),
    OTHER("Other", "#6b7280");

    private final String label;
    private final String color;

    VenueCategory(String label, String color) {
        this.label = label;
        this.color = color;
    }

    public String getLabel() { return label; }
    public String getColor() { return color; }
}