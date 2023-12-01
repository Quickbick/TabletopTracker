package com.creatures;

import com.condition_manager.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreatureTest {

    final int FULL_HEALTH = 25;
    final int MIN_DURATION = 1;

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
    void removeHealth_just_paralyzed(){
        int inputHealthPoints = 10;
        boolean inputCrit = false;
        int expected = 5;
        Creature creature = new AllyCreature("John Creature", 25, 5, new File("./src/main/resources/com/cs422/fxproject/Default_Image.png"));
        Creature spyCreature = spy(creature);
        ConditionDao critConditions = new ConditionDaoImpl();
        Condition paralyzed = critConditions.createCondition("Paralyzed", MIN_DURATION);
        critConditions.addCurrentCondition(paralyzed);
        Mockito.when(spyCreature.getCurrentConditions()).thenReturn(critConditions.getCurrentConditions());
        spyCreature.removeHealth(inputHealthPoints, inputCrit);
        assertEquals(expected, spyCreature.getCurrentHealth());
    }

    @Test
    void removeHealth_just_incapacitated(){
        int inputHealthPoints = 10;
        boolean inputCrit = false;
        int expected = 5;
        Creature creature = new AllyCreature("John Creature", 25, 5, new File("./src/main/resources/com/cs422/fxproject/Default_Image.png"));
        Creature spyCreature = spy(creature);
        ConditionDao critConditions = new ConditionDaoImpl();
        Condition paralyzed = critConditions.createCondition("Incapacitated", MIN_DURATION);
        critConditions.addCurrentCondition(paralyzed);
        Mockito.when(spyCreature.getCurrentConditions()).thenReturn(critConditions.getCurrentConditions());
        spyCreature.removeHealth(inputHealthPoints, inputCrit);
        assertEquals(expected, spyCreature.getCurrentHealth());
    }

    @Test
    void removeHealth_just_unconscious(){
        int inputHealthPoints = 10;
        boolean inputCrit = false;
        int expected = 5;
        Creature creature = new AllyCreature("John Creature", 25, 5, new File("./src/main/resources/com/cs422/fxproject/Default_Image.png"));
        Creature spyCreature = spy(creature);
        ConditionDao critConditions = new ConditionDaoImpl();
        Condition paralyzed = critConditions.createCondition("Unconscious", MIN_DURATION);
        critConditions.addCurrentCondition(paralyzed);
        Mockito.when(spyCreature.getCurrentConditions()).thenReturn(critConditions.getCurrentConditions());
        spyCreature.removeHealth(inputHealthPoints, inputCrit);
        assertEquals(expected, spyCreature.getCurrentHealth());
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
    void addCondition_more_than_once() {
        String inputType = "Charmed";
        int expected = 1;
        Creature creature = new AllyCreature("John Creature", 25, 5, new File("./src/main/resources/com/cs422/fxproject/Default_Image.png"));
        creature.addCondition(inputType, MIN_DURATION);
        creature.addCondition(inputType, MIN_DURATION);
        assertEquals(1, creature.getCurrentConditions().size());
    }

    @Test
    void removeCondition() {
        // Create a test creature instance
        Creature creature = new AllyCreature("John Creature", 25, 5, new File("/path/to/image.png"));

        // Add a condition to the creature
        String conditionType = "Frightened";
        creature.addCondition(conditionType, 3); // Replace the duration with an appropriate value

        // Get the initial count of conditions
        int initialConditionsCount = creature.getCurrentConditions().size();

        // Remove the condition
        creature.removeCondition(creature.getCurrentConditions().get(0)); // Pass the condition to be removed

        // Get the count of conditions after removal
        int conditionsCountAfterRemoval = creature.getCurrentConditions().size();

        // Verify that the count is decreased by 1 after the removal
        assertEquals(initialConditionsCount - 1, conditionsCountAfterRemoval);
    }

    @Test
    void decrementConditions() {
        Creature creature = new AllyCreature("John Creature", 25, 5, new File("/path/to/image.png"));

        // Add a few conditions to the creature
        creature.addCondition("Frightened", 3);
        creature.addCondition("Poisoned", 2);

        // Capture the base durations
        List<Integer> baseDurations = creature.getCurrentConditions().stream()
                .map(Condition::getDuration)
                .collect(Collectors.toList());

        // Decrement the conditions
        creature.decrementConditions();

        // Get the updated conditions list
        List<Condition> updatedConditions = creature.getCurrentConditions();

        // Check the duration of each condition in the updated list
        for (int i = 0; i < updatedConditions.size(); i++) {
            Condition updatedCondition = updatedConditions.get(i);
            Integer baseDuration = baseDurations.get(i);

            // Compare the updated condition duration with the base condition - 1
            assertEquals(baseDuration - 1, updatedCondition.getDuration());
        }
    }

    @Test
    void getName() {
        String expectedName = "John Creature";
        Creature creature = new AllyCreature(expectedName, 25, 5, new File("./path/to/image.png"));

        // Get the name from the creature object
        String actualName = creature.getName();

        // Verify if the retrieved name matches the expected name
        assertEquals(expectedName, actualName);
    }

    @Test
    void getImage() {
        // Create a test file
        File testImage = new File("/path/to/test_image.png");

        Creature creature = new AllyCreature("John Creature", 25, 5, testImage);

        // Retrieve the image file using the method
        File retrievedImage = creature.getImage();

        // Assert that the retrieved image matches the expected image
        assertEquals(testImage, retrievedImage);
    }

    @Test
    void getMaxHealth() {
        int expectedMaxHealth = 25; // Change to your expected maximum health value
        Creature creature = new AllyCreature("John Creature", expectedMaxHealth, 5, new File("/path/to/image.png"));

        // Retrieve the maximum health using the method
        int actualMaxHealth = creature.getMaxHealth();

        // Validate the retrieved maximum health
        assertEquals(expectedMaxHealth, actualMaxHealth);
    }

    @Test
    void getCurrentHealth() {
        int expectedCurrentHealth = 20; // Change to your expected current health value
        Creature creature = new AllyCreature("John Creature", 25, 5, new File("/path/to/image.png"));
        creature.setCurrentHealth(expectedCurrentHealth);

        // Retrieve the current health using the method
        int actualCurrentHealth = creature.getCurrentHealth();

        // Validate the retrieved current health
        assertEquals(expectedCurrentHealth, actualCurrentHealth);
    }

    @Test
    void getBonusHealth() {
        int expectedBonusHealth = 10; // Change to your expected bonus health value
        Creature creature = new AllyCreature("John Creature", 25, 5, new File("/path/to/image.png"));
        creature.setBonusHealth(expectedBonusHealth);

        // Retrieve the bonus health using the method
        int actualBonusHealth = creature.getBonusHealth();

        // Validate the retrieved bonus health
        assertEquals(expectedBonusHealth, actualBonusHealth);
    }

    @Test
    void getInitiative() {
        int expectedInitiative = 5; // Change to your expected initiative value
        Creature creature = new AllyCreature("John Creature", 25, expectedInitiative, new File("/path/to/image.png"));

        // Retrieve the initiative using the method
        int actualInitiative = creature.getInitiative();

        // Validate the retrieved initiative
        assertEquals(expectedInitiative, actualInitiative);
    }

    @Test
    void getCurrentConditions() {
        Creature creature = new AllyCreature("John Creature", 25, 5, new File("/path/to/image.png"));

        // Add a few conditions to the creature
        creature.addCondition("Frightened", 3);
        creature.addCondition("Poisoned", 2);

        // Get the current conditions of the creature
        List<Condition> currentConditions = creature.getCurrentConditions();

        // Assert that the current conditions match the added conditions
        assertEquals(2, currentConditions.size());
        assertTrue(currentConditions.get(0).toString().contains("Frightened"));
        assertTrue(currentConditions.get(1).toString().contains("Poisoned"));
    }
}