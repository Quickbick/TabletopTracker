package com.creatures;

import com.condition_manager.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreatureTest {

    final int FULL_HEALTH = 25;
    final int MIN_DURATION = 1;

    @BeforeEach
    public void init(){
        Creature creature = new AllyCreature("John Creature", 25, 5, new File("./src/main/resources/com/cs422/fxproject/Default_Image.png"));
        Creature spyCreature = spy(creature);
    }

    private ConditionDao getCritConditions() {
        ConditionDao critConditions = new ConditionDaoImpl();
        Condition paralyzed = critConditions.createCondition("Paralyzed", MIN_DURATION);
        critConditions.addCurrentCondition(paralyzed);
        Condition unconscious = critConditions.createCondition("Unconscious", MIN_DURATION);
        critConditions.addCurrentCondition(unconscious);
        Condition incapacitated = critConditions.createCondition("Incapacitated", MIN_DURATION);
        critConditions.addCurrentCondition(incapacitated);
        return critConditions;
    }

    @Test
    void addHealth_to_full() {
        int input = 15;
        Creature creature = new AllyCreature("John Creature", 25, 5, new File("./src/main/resources/com/cs422/fxproject/Default_Image.png"));
        creature.setCurrentHealth(20);
        creature.addHealth(input);
        assertEquals(FULL_HEALTH, creature.getCurrentHealth());
    }

    @Test
    void addHealth_not_full() {
        int input = 3;
        int expected = 23;
        Creature creature = new AllyCreature("John Creature", 25, 5, new File("./src/main/resources/com/cs422/fxproject/Default_Image.png"));
        creature.setCurrentHealth(20);
        creature.addHealth(input);
        assertEquals(expected, creature.getCurrentHealth());
    }

    @Test
    void removeHealth_with_auto_crits(){
        int inputHealthPoints = 10;
        boolean inputCrit = false;
        int expected = 5;
        Creature creature = new AllyCreature("John Creature", 25, 5, new File("./src/main/resources/com/cs422/fxproject/Default_Image.png"));
        Creature spyCreature = spy(creature);
        ConditionDao critConditions = getCritConditions();
        Mockito.when(spyCreature.getCurrentConditions()).thenReturn(critConditions.getCurrentConditions());
        spyCreature.removeHealth(inputHealthPoints, inputCrit);
        assertEquals(expected, spyCreature.getCurrentHealth());
    }

    @Test
    void removeHealth_with_more_bonus_health(){
        int inputHealthPoints = 10;
        boolean inputCrit = false;
        int expectedHealth = 25;
        int expectedBonusHealth = 5;
        Creature creature = new AllyCreature("John Creature", 25, 5, new File("./src/main/resources/com/cs422/fxproject/Default_Image.png"));
        creature.setBonusHealth(15);
        creature.removeHealth(inputHealthPoints, inputCrit);
        assertEquals(expectedHealth, creature.getCurrentHealth());
        assertEquals(expectedBonusHealth, creature.getBonusHealth());
    }

    @Test
    void removeHealth_kill(){
        int inputHealthPoints = 30;
        boolean inputCrit = true;
        int expected = 0;
        Creature creature = new AllyCreature("John Creature", 25, 5, new File("./src/main/resources/com/cs422/fxproject/Default_Image.png"));
        creature.removeHealth(inputHealthPoints, inputCrit);
        assertEquals(expected, creature.getCurrentHealth());
        assertEquals(Unconscious.class, creature.getCurrentConditions().get(0).getClass());
    }

    @Test
    void addBonusHealth() {
        int input = 5;
        int expected = 5;
        Creature creature = new AllyCreature("John Creature", 25, 5, new File("./src/main/resources/com/cs422/fxproject/Default_Image.png"));
        creature.addBonusHealth(input);
        assertEquals(expected, creature.getBonusHealth());
    }

    @Test
    void addBonusHealth_no_change() {
        int input = 3;
        int expected = 5;
        Creature creature = new AllyCreature("John Creature", 25, 5, new File("./src/main/resources/com/cs422/fxproject/Default_Image.png"));
        creature.setBonusHealth(5);
        creature.addBonusHealth(input);
        assertEquals(expected, creature.getBonusHealth());
    }

    @Test
    void addCondition() {
        String inputType = "Charmed";
        Creature creature = new AllyCreature("John Creature", 25, 5, new File("./src/main/resources/com/cs422/fxproject/Default_Image.png"));
        creature.addCondition(inputType, MIN_DURATION);
        assertEquals(Charmed.class, creature.getCurrentConditions().get(0).getClass());
    }

    @Test
    void removeCondition() {
    }

    @Test
    void decrementConditions() {
    }

    @Test
    void getName() {
    }

    @Test
    void getImage() {
    }

    @Test
    void getMaxHealth() {
    }

    @Test
    void getCurrentHealth() {
    }

    @Test
    void getBonusHealth() {
    }

    @Test
    void getInitiative() {
    }

    @Test
    void getCurrentConditions() {
    }

    @Test
    void getAvailableConditions() {
    }
}