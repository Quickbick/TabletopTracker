package com.condition_manager.conditions;

import com.condition_manager.rollModifierEnum;

/**
 * Attacks from creature are Disadvantaged,
 * Attacks against creature are Advantaged.
 * Does not skip turn.
 */
public class Blindness extends Condition {
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

    protected Blindness(int duration) {
        super(duration);
    }
}
