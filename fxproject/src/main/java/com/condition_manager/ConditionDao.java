package com.condition_manager;

import com.condition_manager.conditions.Condition;

import java.util.List;

public interface ConditionDao {
    List<Condition> getCurrentConditions();
    void addCurrentCondition(Condition currentConditions);
    void removeCurrentCondition(Condition condition);

    /**
     * Lower the duration of all conditions by 1 round.
     */
    void decreaseAllConditionDurations();
}
