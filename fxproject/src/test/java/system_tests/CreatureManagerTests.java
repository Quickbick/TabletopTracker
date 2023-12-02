package system_tests;

import com.creatures.CreatureDao;
import com.creatures.CreatureManagerApp;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(ApplicationExtension.class)
class CreatureManagerAppTest extends FxRobot {
    CreatureManagerApp creatureManagerApp;

    @Start
    private void start(Stage stage) {
        creatureManagerApp = new CreatureManagerApp();
        creatureManagerApp.start(stage);
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

        // Wait for the dialog to open
        sleep(500); // Wait for 500 milliseconds

        // Now interact with the dialog's contents
        clickOn("#nameTextField").write("Test Creature");
        clickOn("#healthTextField").write("100");
        clickOn("#initiativeTextField").write("10");
        // Handle other fields as needed

        // Submit the dialog to add the creature
        clickOn("#confirmAddCreatureButton");

        CreatureDao creatureDao =  getCreatureDaoViaReflection(creatureManagerApp);
        assertEquals(creatureDao.getCreatureInventory().size(), 1);
    }

    @Test
    void testAddAndDeleteCreature() throws NoSuchFieldException, IllegalAccessException {
        // Open the dialog and add a creature
        clickOn("#addCreatureButton");
        sleep(500); // Wait for dialog to open
        clickOn("#nameTextField").write("Test Creature");
        clickOn("#healthTextField").write("100");
        clickOn("#initiativeTextField").write("10");
        clickOn("#confirmAddCreatureButton");

        // Retrieve the CreatureDao to check the state after addition
        CreatureDao creatureDao = getCreatureDaoViaReflection(creatureManagerApp);
        assertEquals(creatureDao.getCreatureInventory().size(), 1, "One creature should be added");

        // Simulate deletion of the creature
        // Assuming the delete button for each creature includes the creature's name in its ID
        clickOn("#deleteButton_TestCreature");

        // Confirm deletion if required
        // If your application shows a confirmation dialog, handle it here
        clickOn("#confirmDeleteYesButton"); // Replace with the actual ID of the confirmation button

        // Check the state after deletion
        assertEquals(creatureDao.getCreatureInventory().size(), 0, "Creature should be deleted");
    }
}
