package com.condition_manager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConditionDaoImplTest {

    @Test
    void createCondition() {
        ConditionDaoImpl conditionDao = new ConditionDaoImpl();

        String[] conditionTypes = {
                "Blinded", "Charmed", "Deafened", "Frightened", "Grappled", "Incapacitated",
                "Invisible", "Paralyzed", "Petrified", "Poisoned", "Prone", "Restrained",
                "Stunned", "Unconscious", "UnknownCondition"
        };

        int[] testDurations = { 3, 5, 2, 4, 3, 5, 2, 4, 3, 5, 2, 4, 3, 5, 10 };

        for (int i = 0; i < conditionTypes.length; i++) {
            String conditionType = conditionTypes[i];
            int duration = testDurations[i];

            System.out.println("Testing for " + conditionType + " with duration " + duration);

            Condition condition;
            boolean isUnknownCondition = false;

            try {
                condition = conditionDao.createCondition(conditionType, duration);
            } catch (IllegalStateException e) {
                isUnknownCondition = true;
            }

            if (conditionType.equals("UnknownCondition")) {
                assertTrue(isUnknownCondition);
            } else {
                assertNotNull(conditionDao);
            }
        }
    }

    @Test
    void addCurrentCondition() {
        ConditionDaoImpl conditionDao = new ConditionDaoImpl();

        Condition blinded = conditionDao.createCondition("Blinded", 3);
        conditionDao.addCurrentCondition(blinded);
        assertEquals(1, conditionDao.getCurrentConditions().size());

        Condition charmed = conditionDao.createCondition("Charmed", 5);
        conditionDao.addCurrentCondition(charmed);
        assertEquals(2, conditionDao.getCurrentConditions().size());
    }

    @Test
    void removeCurrentCondition() {
        ConditionDaoImpl conditionDao = new ConditionDaoImpl();

        Condition blinded = conditionDao.createCondition("Blinded", 3);
        conditionDao.addCurrentCondition(blinded);
        assertEquals(1, conditionDao.getCurrentConditions().size());

        conditionDao.removeCurrentCondition(blinded);
        assertEquals(0, conditionDao.getCurrentConditions().size());
    }

    @Test
    void addAndRemoveCurrentCondition() {
        ConditionDaoImpl conditionDao = new ConditionDaoImpl();

        Condition blinded = conditionDao.createCondition("Blinded", 3);
        conditionDao.addCurrentCondition(blinded);
        assertEquals(1, conditionDao.getCurrentConditions().size());

        conditionDao.removeCurrentCondition(blinded);
        assertEquals(0, conditionDao.getCurrentConditions().size());
    }

    @Test
    void decreaseAllConditionDurations_RemoveConditionWhenDurationIsOne() {
        ConditionDaoImpl conditionDao = new ConditionDaoImpl();

        // Add a condition with duration 1
        Condition stunned = conditionDao.createCondition("Stunned", 1);
        conditionDao.addCurrentCondition(stunned);

        // Add a condition with duration > 1
        Condition blinded = conditionDao.createCondition("Blinded", 3);
        conditionDao.addCurrentCondition(blinded);

        conditionDao.decreaseAllConditionDurations();

        // Validate that the condition with duration 1 is removed
        assertEquals(1, conditionDao.getCurrentConditions().size());
        assertTrue(conditionDao.getCurrentConditions().contains(blinded));
    }
}