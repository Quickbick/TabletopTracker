package com.creatures;

import com.condition_manager.Charmed;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class AddConditionPairwiseTest {
    /*This test tests integration of Creature.addCondition and ConditionDao.createCondition*/
    @Test
    void addCondition_actually_add() {
        String inputType = "Charmed";
        Creature creature = new AllyCreature("John Creature", 25, 5, new File("./src/main/resources/com/cs422/fxproject/Default_Image.png"));
        creature.addCondition(inputType, 3);
        assertEquals(Charmed.class, creature.getCurrentConditions().get(0).getClass());
    }
}