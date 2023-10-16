package com.condition_manager;

public abstract class Condition {
    private int duration;

    protected Condition(int duration) {
        this.duration = duration;
    }
}
