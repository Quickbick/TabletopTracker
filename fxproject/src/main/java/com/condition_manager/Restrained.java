package com.condition_manager;

/**
 * Attacks from creature are Disadvantaged, Attacks against creature are Advantaged.
 */
public class Restrained extends Condition {
    @Override
    public rollModifierEnum getAttacksFrom() {
        return rollModifierEnum.Disadvantage;
    }

    @Override
    public rollModifierEnum getAttacksAgainst() {
        return rollModifierEnum.Advantage;
    }

    @Override
    public boolean getSkipTurn() {
        return false;
    }

    protected Restrained(int duration) {
        super(duration);
    }
}
