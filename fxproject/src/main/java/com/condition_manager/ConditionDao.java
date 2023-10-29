package com.condition_manager;

import java.util.List;

public interface ConditionDao {

    /**
     * Creates a condition based on a string and a duration.
     * @param conditionType Type of condition to create.
     * @param duration Duration of created condition.
     * @return Return created condition.
     */
    Condition createCondition(String conditionType, int duration);

    /**
     * Getter for condition list.
     * @return List of all conditions currently held by creature.
     */
    List<Condition> getCurrentConditions();

    /**
     * Getter for list of all possible conditions.
     * @return Returns a list of string names of all possible conditions.
     */
    List<String> getAllConditions();

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
