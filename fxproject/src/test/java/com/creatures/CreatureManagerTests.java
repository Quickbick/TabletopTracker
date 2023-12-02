package com.creatures;

import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.lang.reflect.Field;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(ApplicationExtension.class)
class CreatureManagerAppTest extends FxRobot {
    CreatureManagerApp creatureManagerApp;

    @Start
    private void start(Stage stage) {
        creatureManagerApp = new CreatureManagerApp();
        creatureManagerApp.start(stage);
    }

    @BeforeEach
    void setUp() throws Exception {
        CreatureDao creatureDao = getCreatureDaoViaReflection(creatureManagerApp);
        creatureDao.clearInventory(); // hypothetical method to reset state
    }


    private CreatureDao getCreatureDaoViaReflection(CreatureManagerApp app) throws NoSuchFieldException, IllegalAccessException {
        Field creatureDaoField = CreatureManagerApp.class.getDeclaredField("creatureDao");
        creatureDaoField.setAccessible(true);
        return (CreatureDao) creatureDaoField.get(app);
    }

    @Test
    void testAddCreature() throws NoSuchFieldException, IllegalAccessException {
        // Click on "Add Creature" button to open the dialog
        clickOn("#addCreatureButton");

        // Now interact with the dialog's contents
        clickOn("#nameTextField").write("Test Creature");
        clickOn("#healthTextField").write("100");
        clickOn("#initiativeTextField").write("10");
        // Handle other fields as needed

        // Submit the dialog to add the creature
        clickOn("#confirmAddCreatureButton");

        CreatureDao creatureDao =  getCreatureDaoViaReflection(creatureManagerApp);
        assertEquals(creatureDao.getCreatureInventory().size(), 1);

        Creature creature = creatureDao.getCreatureInventory().get(0);
        assertEquals(creature.getName(), "Test Creature");
        assertEquals(creature.getMaxHealth(), 100);
        assertEquals(creature.getInitiative(), 10);
    }

        @Test
        void testAddAndDeleteCreature() throws NoSuchFieldException, IllegalAccessException {
            // Open the dialog and add a creature
            clickOn("#addCreatureButton");

            clickOn("#nameTextField").write("Test Creature");
            clickOn("#healthTextField").write("100");
            clickOn("#initiativeTextField").write("10");
            clickOn("#confirmAddCreatureButton");

            // Retrieve the CreatureDao to check the state after addition
            CreatureDao creatureDao = getCreatureDaoViaReflection(creatureManagerApp);
            assertEquals(creatureDao.getCreatureInventory().size(), 1, "One creature should be added");

            Creature creature = creatureDao.getCreatureInventory().get(0);
            assertEquals(creature.getName(), "Test Creature");
            assertEquals(creature.getMaxHealth(), 100);
            assertEquals(creature.getInitiative(), 10);

            // Simulate deletion of the creature
            // Assuming the delete button for each creature includes the creature's name in its ID
            clickOn("#deleteButton_TestCreature");

            // Confirm deletion if required
            // If your application shows a confirmation dialog, handle it here
            clickOn("#confirmDeleteYesButton"); // Replace with the actual ID of the confirmation button

            // Check the state after deletion
            assertEquals(creatureDao.getCreatureInventory().size(), 0, "Creature should be deleted");
        }

        @Test
        void testCreateMultipleCreatures() throws NoSuchFieldException, IllegalAccessException {
            Random random = new Random();

            String[] creatureTypes = {"ALLY", "NEUTRAL", "ENEMY"};
            for (int i = 0; i < 4; i++) {
                // Click on "Add Creature" button to open the dialog
                clickOn("#addCreatureButton");

                // Now interact with the dialog's contents
                clickOn("#nameTextField").write("Creature " + (i + 1));

                // Generate random initiative between 0 and 99
                int initiative = random.nextInt(100);
                clickOn("#initiativeTextField").write(Integer.toString(initiative));

                // Generate random health between 1 and 200 (rounded to nearest 5)
                int health = (random.nextInt(40) + 1) * 5;
                clickOn("#healthTextField").write(Integer.toString(health));

                // Set a random creatureType
                String creatureType = creatureTypes[random.nextInt(creatureTypes.length)];
                clickOn("#creatureTypeChoiceBox").clickOn(creatureType);
                clickOn("#confirmAddCreatureButton");

                // Retrieve the CreatureDao to check the state after addition
                CreatureDao creatureDao = getCreatureDaoViaReflection(creatureManagerApp);
                assertEquals(creatureDao.getCreatureInventory().size(), i+1);
              }
        }

        @Test
        void testAdvanceTurnMultipleCreature() throws NoSuchFieldException, IllegalAccessException {
            CreatureDao creatureDao = getCreatureDaoViaReflection(creatureManagerApp);
            Random random = new Random();

            String[] creatureTypes = {"ALLY", "NEUTRAL", "ENEMY"};
            for (int i = 0; i < 4; i++) {
                // Click on "Add Creature" button to open the dialog
                clickOn("#addCreatureButton");

                // Now interact with the dialog's contents
                clickOn("#nameTextField").write("Creature " + (i + 1));

                // Generate random initiative between 0 and 99
                int initiative = random.nextInt(100);
                clickOn("#initiativeTextField").write(Integer.toString(initiative));

                // Generate random health between 1 and 200 (rounded to nearest 5)
                int health = (random.nextInt(40) + 1) * 5;
                clickOn("#healthTextField").write(Integer.toString(health));

                // Set a random creatureType
                String creatureType = creatureTypes[random.nextInt(creatureTypes.length)];
                clickOn("#creatureTypeChoiceBox").clickOn(creatureType);
                clickOn("#confirmAddCreatureButton");

                // Retrieve the CreatureDao to check the state after addition
                assertEquals(creatureDao.getCreatureInventory().size(), i+1);
            }

            // Load
            assertEquals(0, creatureDao.getRoundNumber());
            clickOn("#nextTurnButton");
            clickOn("#nextTurnButton");

            int initialRoundNumber = creatureDao.getRoundNumber();
            assertEquals(0, initialRoundNumber);

            for (int i = 0; i < 10; i++) {
                clickOn("#nextTurnButton");
                // Assert round number increments
            }
            assertNotEquals(initialRoundNumber, creatureDao.getRoundNumber());
        }

        @Test
        void testAddCreatureWithConditions() throws NoSuchFieldException, IllegalAccessException {
            // Click on "Add Creature" button to open the dialog
            clickOn("#addCreatureButton");

            // Now interact with the dialog's contents
            clickOn("#nameTextField").write("Test Creature");
            clickOn("#healthTextField").write("100");
            clickOn("#initiativeTextField").write("10");
            // Handle other fields as needed

            // Submit the dialog to add the creature
            clickOn("#confirmAddCreatureButton");

            CreatureDao creatureDao =  getCreatureDaoViaReflection(creatureManagerApp);
            assertEquals(creatureDao.getCreatureInventory().size(), 1);

            // Click on "Add Condition" button
            clickOn("#addConditionButton_TestCreature");

            // Now interact with the condition dialog's contents
            // For example, set a condition name and details
            clickOn("#conditionChoiceBox").clickOn("Blinded");
            clickOn("#durationTextField").write("20");

            // Submit the condition dialog
            clickOn("#confirmConditionYesButton");

            // Assert condition is added
            Creature creatureWithCondition = creatureDao.getCreatureInventory().get(0);
            assertEquals(creatureWithCondition.getCurrentConditions().size(), 1);
            assertEquals(20, creatureWithCondition.getCurrentConditions().get(0).getDuration());
        }

        @Test
        void testAddCreatureWithMultipleConditions() throws NoSuchFieldException, IllegalAccessException {
            String[] Conditions = {"Blinded", "Charmed", "Deafened", "Frightened", "Invisible", "Stunned"};

            // Click on "Add Creature" button to open the dialog
            clickOn("#addCreatureButton");

            // Now interact with the dialog's contents
            clickOn("#nameTextField").write("Test Creature");
            clickOn("#healthTextField").write("100");
            clickOn("#initiativeTextField").write("10");
            // Handle other fields as needed

            // Submit the dialog to add the creature
            clickOn("#confirmAddCreatureButton");

            CreatureDao creatureDao =  getCreatureDaoViaReflection(creatureManagerApp);
            assertEquals(creatureDao.getCreatureInventory().size(), 1);

            for (int i = 0; i < 6; i++) {
                // Click on "Add Condition" button
                clickOn("#addConditionButton_TestCreature");

                // Now interact with the condition dialog's contents
                // For example, set a condition name and details
                clickOn("#conditionChoiceBox").clickOn(Conditions[i]);
                clickOn("#durationTextField").write("20");

                // Submit the condition dialog
                clickOn("#confirmConditionYesButton");
            }

            // Assert condition is added
            Creature creatureWithCondition = creatureDao.getCreatureInventory().get(0);
            assertEquals(creatureWithCondition.getCurrentConditions().size(), 6);
            assertEquals(20, creatureWithCondition.getCurrentConditions().get(0).getDuration());
        }

        @Test
        void testDamageCreatureNoCrit() throws NoSuchFieldException, IllegalAccessException {
            // Click on "Add Creature" button to open the dialog
            clickOn("#addCreatureButton");

            // Now interact with the dialog's contents
            clickOn("#nameTextField").write("Test Creature");
            clickOn("#healthTextField").write("100");
            clickOn("#initiativeTextField").write("10");
            // Handle other fields as needed

            // Submit the dialog to add the creature
            clickOn("#confirmAddCreatureButton");

            CreatureDao creatureDao =  getCreatureDaoViaReflection(creatureManagerApp);
            assertEquals(creatureDao.getCreatureInventory().size(), 1);

            clickOn("#damageButton_TestCreature");

            clickOn("#damageDialog").write("50");

            clickOn("OK");

            Creature damagedCreature = creatureDao.getCreatureInventory().get(0);
            assertEquals(50, damagedCreature.getCurrentHealth());
        }

        @Test
        void testDamageCreatureWithCrit() throws NoSuchFieldException, IllegalAccessException {
            // Click on "Add Creature" button to open the dialog
            clickOn("#addCreatureButton");

            // Now interact with the dialog's contents
            clickOn("#nameTextField").write("Test Creature");
            clickOn("#healthTextField").write("100");
            clickOn("#initiativeTextField").write("10");
            // Handle other fields as needed

            // Submit the dialog to add the creature
            clickOn("#confirmAddCreatureButton");

            CreatureDao creatureDao =  getCreatureDaoViaReflection(creatureManagerApp);
            assertEquals(creatureDao.getCreatureInventory().size(), 1);

            clickOn("#damageButton_TestCreature");

            clickOn("#damageDialog").write("50");

            clickOn("#critBox");

            clickOn("OK");

            Creature damagedCreature = creatureDao.getCreatureInventory().get(0);
            assertEquals(100 - (50 * 2), damagedCreature.getCurrentHealth());
        }

        @Test
        void testDamageHealCreature() throws NoSuchFieldException, IllegalAccessException {
            // Click on "Add Creature" button to open the dialog
            clickOn("#addCreatureButton");

            // Now interact with the dialog's contents
            clickOn("#nameTextField").write("Test Creature");
            clickOn("#healthTextField").write("100");
            clickOn("#initiativeTextField").write("10");
            // Handle other fields as needed

            // Submit the dialog to add the creature
            clickOn("#confirmAddCreatureButton");

            CreatureDao creatureDao =  getCreatureDaoViaReflection(creatureManagerApp);
            assertEquals(creatureDao.getCreatureInventory().size(), 1);

            // Damage Creature
            clickOn("#damageButton_TestCreature");

            clickOn("#damageDialog").write("50");

            clickOn("#critBox");

            clickOn("OK");

            Creature damagedCreature = creatureDao.getCreatureInventory().get(0);
            assertEquals(100 - (50 * 2), damagedCreature.getCurrentHealth());

            // Heal Creature
            clickOn("#healButton_TestCreature");

            write("30");

            clickOn("OK");

            assertEquals(30, damagedCreature.getCurrentHealth());
        }

        @Test
        void testDamageCreatureWithBonusHealth() throws NoSuchFieldException, IllegalAccessException {
            // Click on "Add Creature" button to open the dialog
            clickOn("#addCreatureButton");

            // Now interact with the dialog's contents
            clickOn("#nameTextField").write("Test Creature");
            clickOn("#healthTextField").write("100");
            clickOn("#initiativeTextField").write("10");
            // Handle other fields as needed

            // Submit the dialog to add the creature
            clickOn("#confirmAddCreatureButton");

            CreatureDao creatureDao =  getCreatureDaoViaReflection(creatureManagerApp);
            assertEquals(creatureDao.getCreatureInventory().size(), 1);

            // Give Creature Bonus Health

            clickOn("#bonusHealthButton_TestCreature");

            write("50");

            clickOn("OK");

            Creature creature = creatureDao.getCreatureInventory().get(0);
            assertEquals(50, creature.getBonusHealth());

            // Damage the Creature
            clickOn("#damageButton_TestCreature");

            clickOn("#damageDialog").write("50");

            clickOn("#critBox");

            clickOn("OK");

            assertEquals(0, creature.getBonusHealth());
            assertEquals(50, creature.getCurrentHealth());
        }

        @Test
        void testDamageCreatureWithBonusHealthAndHeal() throws NoSuchFieldException, IllegalAccessException {
            // Click on "Add Creature" button to open the dialog
            clickOn("#addCreatureButton");

            // Now interact with the dialog's contents
            clickOn("#nameTextField").write("Test Creature");
            clickOn("#healthTextField").write("100");
            clickOn("#initiativeTextField").write("10");
            // Handle other fields as needed

            // Submit the dialog to add the creature
            clickOn("#confirmAddCreatureButton");

            CreatureDao creatureDao =  getCreatureDaoViaReflection(creatureManagerApp);
            assertEquals(creatureDao.getCreatureInventory().size(), 1);

            // Give Creature Bonus Health

            clickOn("#bonusHealthButton_TestCreature");

            write("50");

            clickOn("OK");

            Creature creature = creatureDao.getCreatureInventory().get(0);
            assertEquals(50, creature.getBonusHealth());

            // Damage the Creature
            clickOn("#damageButton_TestCreature");

            clickOn("#damageDialog").write("50");

            clickOn("#critBox");

            clickOn("OK");

            assertEquals(0, creature.getBonusHealth());
            assertEquals(50, creature.getCurrentHealth());

            // Heal the Creature
            clickOn("#healButton_TestCreature");

            write("150");

            clickOn("OK");

            assertEquals(100, creature.getCurrentHealth());
        }
}
