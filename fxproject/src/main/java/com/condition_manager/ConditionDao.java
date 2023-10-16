package com.condition_manager;

import com.condition_manager.conditions.Condition;

import java.util.List;

public interface ConditionDao {
    /**
     * Getter for condition list.
     * @return List of all conditions currently held by creature.
     */
    List<Condition> getCurrentConditions();

    /**
     * Adds a new condition to condition list.
     * @param currentConditions New condition to add.
     */
    void addCurrentCondition(Condition currentConditions);

    /**
     * Removes a current condition from condition list.
     * @param condition Current condition to remove.
     */
    void removeCurrentCondition(Condition condition);

    /**
     * Lower the duration of all conditions by 1 round.
     */
    void decreaseAllConditionDurations();
}
