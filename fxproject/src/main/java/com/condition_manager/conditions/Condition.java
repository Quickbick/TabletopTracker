package com.condition_manager.conditions;

import com.condition_manager.rollModifierEnum;

public abstract class Condition {
    private int duration;

    public abstract rollModifierEnum getAttacksFrom();
    public abstract rollModifierEnum getAttacksAgainst();

    public abstract boolean getSkipTurn();

    protected Condition(int duration) {
        this.duration = duration;
    }

    public void decrementDuration() {
        --this.duration;
    }

    public int getDuration() {
        return duration;
    }
}
