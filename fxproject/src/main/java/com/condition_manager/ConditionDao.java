package com.condition_manager;

import java.util.List;

public interface ConditionDao {
    List<Condition> getCurrentConditions();
    void addCurrentCondition(Condition currentConditions);
    void removeCurrentCondition(Condition condition);
}
