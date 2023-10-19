package com.condition_manager;

/**
 * Attacks from creature are Disadvantaged, Attacks against creature are Advantaged if the attacker is within 5 feet and Disadvantaged if the attacker is farther than 5 feet.
 */
public class Prone extends Condition {
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

    protected Prone(int duration) {
        super(duration);
    }
}
