package com.condition_manager;

/**
 * Creature can't take actions or reactions. Attacks against creature are Advantaged.
 */
public class Paralyzed extends Condition {
    @Override
    public rollModifierEnum getAttacksFrom() {
        return rollModifierEnum.None;
    }

    @Override
    public rollModifierEnum getAttacksAgainst() {
        return rollModifierEnum.Advantage;
    }

    @Override
    public boolean getSkipTurn() {
        return true;
    }

    protected Paralyzed(int duration) {
        super(duration);
    }
}
