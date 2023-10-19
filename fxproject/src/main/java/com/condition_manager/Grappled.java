package com.condition_manager;

/**
 * No change in attack advantages or disadvantages.
 */
public class Grappled extends Condition {
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
        return false;
    }

    protected Grappled(int duration) {
        super(duration);
    }
}
