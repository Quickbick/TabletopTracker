package com.condition_manager;

/**
 * Attacks from creature are Disadvantaged.
 */
public class Poisoned extends Condition {
    @Override
    public rollModifierEnum getAttacksFrom() {
        return rollModifierEnum.Disadvantage;
    }

    @Override
    public rollModifierEnum getAttacksAgainst() {
        return rollModifierEnum.None;
    }

    @Override
    public boolean getSkipTurn() {
        return false;
    }

    protected Poisoned(int duration) {
        super(duration);
    }
}
