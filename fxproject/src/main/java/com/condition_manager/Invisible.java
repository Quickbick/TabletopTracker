package com.condition_manager;

/**
 * Attacks from creature are Advantaged, Attacks against creature are Disadvantaged.
 */
public class Invisible extends Condition {
    @Override
    public rollModifierEnum getAttacksFrom() {
        return rollModifierEnum.Advantage;
    }

    @Override
    public rollModifierEnum getAttacksAgainst() {
        return rollModifierEnum.Disadvantage;
    }

    @Override
    public boolean getSkipTurn() {
        return false;
    }

    protected Invisible(int duration) {
        super(duration);
    }
}
