package com.condition_manager;

/**
 * Creature can't take actions or reactions.
 */
public class Incapacitated extends Condition {
    @Override
    public rollModifierEnum getAttacksFrom() {
        return rollModifierEnum.None;
    }

    @Override
    public rollModifierEnum getAttacksAgainst() {
        return rollModifierEnum.None;
    }

    @Override
    public boolean getSkipTurn() {
        return true;
    }

    protected Incapacitated(int duration) {
        super(duration);
    }
}
