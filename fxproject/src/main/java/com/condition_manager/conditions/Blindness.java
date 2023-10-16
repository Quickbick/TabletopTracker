package com.condition_manager.conditions;

import com.condition_manager.Condition;
import com.condition_manager.rollModifierEnum;

public class Blindness extends Condition {
    private final rollModifierEnum attacksFrom = rollModifierEnum.Disadvantage;
    private final rollModifierEnum attacksAgainst = rollModifierEnum.Advantage;

    // Advantage
    protected Blindness(int duration) {
        super(duration);
    }
}
