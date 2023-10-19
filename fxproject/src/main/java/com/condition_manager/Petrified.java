package com.condition_manager;

/**
 * Creature can't take actions or reactions. Attacks against creature are Advantaged.
 */
public class Petrified extends Condition {
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

    protected Petrified(int duration) {
        super(duration);
    }
}
