package com.condition_manager;

/**
 * Attacks from creature are Disadvantaged if the source of fear is in line of sight.
 */
public class Frightened extends Condition {
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

    protected Frightened(int duration) {
        super(duration);
    }
}
