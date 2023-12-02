package system_tests;

import com.creatures.CreatureManagerApp;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.api.FxRobot;

import static org.testfx.assertions.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
class CreatureManagerAppTest extends FxRobot {

    @Start
    private void start(Stage stage) {
        new CreatureManagerApp().start(stage);
    }

    @Test
    void testAddCreature() {
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
    }
}
