package com.creatures;

import com.condition_manager.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

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
        final File mockFile = mock(File.class);
        Creature creature = new AllyCreature("John Creature", 25, 5, mockFile);
        creature.setCurrentHealth(20);
        creature.addHealth(input);
        assertEquals(FULL_HEALTH, creature.getCurrentHealth());
    }

    @Test
    void addHealth_not_full() {
        int input = 3;
        int expected = 23;
        final File mockFile = mock(File.class);
        Creature creature = new AllyCreature("John Creature", 25, 5, mockFile);
        creature.setCurrentHealth(20);
        creature.addHealth(input);
        assertEquals(expected, creature.getCurrentHealth());
    }

    @Test
    void removeHealth_with_auto_crits(){
        final File mockFile = mock(File.class);
        int inputHealthPoints = 10;
        boolean inputCrit = false;
        int expected = 5;
        ConditionDao critConditions = getCritConditions();

        Creature creature = new AllyCreature("John Creature", 25, 5, mockFile);
        Creature spyCreature = spy(creature);
        Mockito.when(spyCreature.getCurrentConditions()).thenReturn(critConditions.getCurrentConditions());
        spyCreature.removeHealth(inputHealthPoints, inputCrit);
        assertEquals(expected, spyCreature.getCurrentHealth());

        creature = new AllyCreature("John Creature", 25, 5, mockFile);
        spyCreature = spy(creature);
        critConditions.removeCurrentCondition(critConditions.getCurrentConditions().get(0));
        Mockito.when(spyCreature.getCurrentConditions()).thenReturn(critConditions.getCurrentConditions());
        spyCreature.removeHealth(inputHealthPoints, inputCrit);
        assertEquals(expected, spyCreature.getCurrentHealth());

        creature = new AllyCreature("John Creature", 25, 5, mockFile);
        spyCreature = spy(creature);
        critConditions.removeCurrentCondition(critConditions.getCurrentConditions().get(0));
        Mockito.when(spyCreature.getCurrentConditions()).thenReturn(critConditions.getCurrentConditions());
        spyCreature.removeHealth(inputHealthPoints, inputCrit);
        assertEquals(expected, spyCreature.getCurrentHealth());
    }

    @Test
    void removeHealth_with_more_bonus_health(){
        final File mockFile = mock(File.class);
        int inputHealthPoints = 10;
        boolean inputCrit = false;
        int expectedHealth = 25;
        int expectedBonusHealth = 5;
        Creature creature = new AllyCreature("John Creature", 25, 5, mockFile);
        creature.setBonusHealth(15);
        creature.removeHealth(inputHealthPoints, inputCrit);
        assertEquals(expectedHealth, creature.getCurrentHealth());
        assertEquals(expectedBonusHealth, creature.getBonusHealth());
    }

    @Test
    void removeHealth_kill(){
        final File mockFile = mock(File.class);
        int inputHealthPoints = 30;
        boolean inputCrit = true;
        int expected = 0;
        Creature creature = new AllyCreature("John Creature", 25, 5, mockFile);
        creature.removeHealth(inputHealthPoints, inputCrit);
        assertEquals(expected, creature.getCurrentHealth());
        assertEquals(Unconscious.class, creature.getCurrentConditions().get(0).getClass());
    }

    @Test
    void addBonusHealth() {
        final File mockFile = mock(File.class);
        int input = 5;
        int expected = 5;
        Creature creature = new AllyCreature("John Creature", 25, 5, mockFile);
        creature.addBonusHealth(input);
        assertEquals(expected, creature.getBonusHealth());
    }

    @Test
    void addBonusHealth_no_change() {
        final File mockFile = mock(File.class);
        int input = 3;
        int expected = 5;
        Creature creature = new AllyCreature("John Creature", 25, 5, mockFile);
        creature.setBonusHealth(5);
        creature.addBonusHealth(input);
        assertEquals(expected, creature.getBonusHealth());
    }

    @Test
    void addCondition() {
        final File mockFile = mock(File.class);
        String inputType = "Charmed";
        Creature creature = new EnemyCreature("John Creature", 25, 5, mockFile);
        creature.addCondition(inputType, MIN_DURATION);
        creature.addCondition(inputType, MIN_DURATION);
        assertEquals(Charmed.class, creature.getCurrentConditions().get(0).getClass());
    }

    @Test
    void removeCondition() {
        final File mockFile = mock(File.class);
        // Create a test creature instance
        Creature creature = new AllyCreature("John Creature", 25, 5, mockFile);

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
        final File mockFile = mock(File.class);
        Creature creature = new NeutralCreature("John Creature", 25, 5, mockFile);

        // Add a few conditions to the creature
        creature.addCondition("Frightened", 3);
        creature.addCondition("Poisoned", 2);

        // Capture the base durations
        List<Integer> baseDurations = creature.getCurrentConditions().stream()
                .map(Condition::getDuration)
                .toList();

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
        final File mockFile = mock(File.class);
        String expectedName = "John Creature";
        Creature creature = new AllyCreature(expectedName, 25, 5, mockFile);

        // Get the name from the creature object
        String actualName = creature.getName();

        // Verify if the retrieved name matches the expected name
        assertEquals(expectedName, actualName);
    }

    @Test
    void getImage() {
        // Create a test file
        File testImage = mock(File.class);

        Creature creature = new AllyCreature("John Creature", 25, 5, testImage);

        // Retrieve the image file using the method
        File retrievedImage = creature.getImage();

        // Assert that the retrieved image matches the expected image
        assertEquals(testImage, retrievedImage);
    }

    @Test
    void getMaxHealth() {
        final File mockFile = mock(File.class);
        int expectedMaxHealth = 25; // Change to your expected maximum health value
        Creature creature = new AllyCreature("John Creature", expectedMaxHealth, 5, mockFile);

        // Retrieve the maximum health using the method
        int actualMaxHealth = creature.getMaxHealth();

        // Validate the retrieved maximum health
        assertEquals(expectedMaxHealth, actualMaxHealth);
    }

    @Test
    void getCurrentHealth() {
        final File mockFile = mock(File.class);
        int expectedCurrentHealth = 20; // Change to your expected current health value
        Creature creature = new AllyCreature("John Creature", 25, 5, mockFile);
        creature.setCurrentHealth(expectedCurrentHealth);

        // Retrieve the current health using the method
        int actualCurrentHealth = creature.getCurrentHealth();

        // Validate the retrieved current health
        assertEquals(expectedCurrentHealth, actualCurrentHealth);
    }

    @Test
    void getBonusHealth() {
        final File mockFile = mock(File.class);
        int expectedBonusHealth = 10; // Change to your expected bonus health value
        Creature creature = new AllyCreature("John Creature", 25, 5, mockFile);
        creature.setBonusHealth(expectedBonusHealth);

        // Retrieve the bonus health using the method
        int actualBonusHealth = creature.getBonusHealth();

        // Validate the retrieved bonus health
        assertEquals(expectedBonusHealth, actualBonusHealth);
    }

    @Test
    void getInitiative() {
        final File mockFile = mock(File.class);
        int expectedInitiative = 5; // Change to your expected initiative value
        Creature creature = new AllyCreature("John Creature", 25, expectedInitiative, mockFile);

        // Retrieve the initiative using the method
        int actualInitiative = creature.getInitiative();

        // Validate the retrieved initiative
        assertEquals(expectedInitiative, actualInitiative);
    }

    @Test
    void getCurrentConditions() {
        final File mockFile = mock(File.class);
        Creature creature = new AllyCreature("John Creature", 25, 5, mockFile);

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

    @Test
    void getAvailableConditions() {
        final File mockFile = mock(File.class);
        final List<String> allConditions = Arrays.asList(
                "Blinded",
                "Charmed",
                "Deafened",
                "Frightened",
                "Grappled",
                "Incapacitated",
                "Invisible",
                "Paralyzed",
                "Petrified",
                "Poisoned",
                "Prone",
                "Restrained",
                "Stunned",
                "Unconscious"
        );
        Creature creature = new AllyCreature("John Creature", 25, 5, mockFile);

        assertEquals(creature.getAllConditions(), allConditions);
    }
}