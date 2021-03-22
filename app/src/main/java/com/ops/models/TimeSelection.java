package com.ops.models;

public class TimeSelection {
    private String time;
    private boolean selected;

    public TimeSelection(String time, boolean selected) {
        this.time = time;
        this.selected = selected;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
