package com.creatures;

import com.condition_manager.Unconscious;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class RemoveHealthPairwiseTest {

    /*These tests are for the pairwise integration of Creature.removeHealth and Creature.addCondition*/

    private Creature creature;
    @Test
    void removeHealth_add_condition() {
        int inputHealthPoints = 30;
        boolean inputCrit = false;
        Creature creature = new AllyCreature("John Creature", 25, 5, new File("./src/main/resources/com/cs422/fxproject/Default_Image.png"));
        creature.removeHealth(inputHealthPoints, inputCrit);
        assertEquals(Unconscious.class, creature.getCurrentConditions().get(0).getClass());
    }
}