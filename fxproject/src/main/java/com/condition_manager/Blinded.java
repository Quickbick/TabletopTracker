package com.condition_manager;

/**
 * Attacks from creature are Disadvantaged,
 * Attacks against creature are Advantaged.
 * Does not skip turn.
 */
public class Blinded extends Condition {
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

    protected Blinded(int duration) {
        super(duration);
    }
}
