package com.condition_manager;

/**
 * Creature can't take actions or reactions. Attacks against creature are Advantaged if the attacker is within 5 feet.
 */
public class Unconscious extends Condition {
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

    protected Unconscious(int duration) {
        super(duration);
    }
}
