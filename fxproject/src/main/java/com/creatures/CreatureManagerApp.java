package com.creatures;

import com.condition_manager.Condition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class CreatureManagerApp extends Application {
    private final static CreatureDao creatureDao = new CreatureDaoImpl();
    private FlowPane creaturePane;
    private Label roundLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        //Basic GUI Setup
        primaryStage.setTitle("Creature Manager");
        BorderPane root = new BorderPane();
        creaturePane = new FlowPane(10, 10);
        creaturePane.setAlignment(Pos.CENTER);
        creaturePane.setMinWidth(800);
        ScrollPane sp = new ScrollPane(creaturePane);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setFitToWidth(true);

        //Add Creature Button
        Button addButton = new Button("Add Creature");
        addButton.setOnAction(e -> showCreatureDialog());

        //Save Button
        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> showSaveDialog());

        //Load Button
        Button loadButton = new Button("Load");
        loadButton.setOnAction(e -> showLoadDialog());

        //Next Turn Button
        Button nextTurnButton = new Button("Next Turn");
        nextTurnButton.setOnAction(e -> nextTurnPressed());

        //Setup Round Label
        roundLabel = new Label("Round: " + creatureDao.getRoundNumber());
        roundLabel.setPadding(new Insets(5, 10, 5, 10));
        roundLabel.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(5), Insets.EMPTY)));
        roundLabel.setBorder(new Border(new BorderStroke(Color.DARKGRAY, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(2))));

        //Adds All Buttons to GUI
        HBox hbox = new HBox();
        hbox.getChildren().addAll(addButton, saveButton, loadButton, nextTurnButton);

        //Layout and Styling Adjustments
        BorderPane topPane = new BorderPane();
        topPane.setLeft(hbox);
        topPane.setRight(roundLabel);
        root.setTop(topPane);
        root.setCenter(sp);

        //Shows GUI on Launch
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Set ID's for testing purposes
        roundLabel.setId("roundLabel");
        addButton.setId("addCreatureButton");
        saveButton.setId("saveButton");
        loadButton.setId("loadButton");
        nextTurnButton.setId("nextTurnButton");
    }

    private void showCreatureDialog() {
        // Basic Dialog Setup
        Dialog<Creature> dialog = new Dialog<>();
        dialog.setTitle("Add Creature");
        dialog.setHeaderText("Enter Creature Details");
        final File[] files = new File[1];

        // Add Fields and Labels
        Label nameLabel = new Label("Name:");
        TextField nameTextField = new TextField();
        nameTextField.setId("nameTextField"); // Set ID for nameTextField
        Label healthLabel = new Label("Health:");
        TextField healthTextField = new TextField();
        healthTextField.setId("healthTextField"); // Set ID for healthTextField
        Label initiativeLabel = new Label("Initiative:");
        TextField initiativeTextField = new TextField();
        initiativeTextField.setId("initiativeTextField"); // Set ID for initiativeTextField
        Label imageLabel = new Label("Image:");
        Button selectImage = new Button("Browse");
        selectImage.setOnAction(actionEvent -> selectImageAction(files, dialog));
        selectImage.setId("selectImageButton"); // Set ID for selectImage
        ChoiceBox<String> creatureTypeChoiceBox = new ChoiceBox<>();
        creatureTypeChoiceBox.getItems().addAll("ALLY", "NEUTRAL", "ENEMY");
        creatureTypeChoiceBox.setValue("ALLY");
        creatureTypeChoiceBox.setId("creatureTypeChoiceBox"); // Set ID for creatureTypeChoiceBox

        // Add Content to Dialog
        VBox dialogContent = new VBox();
        dialogContent.getChildren().addAll(
                nameLabel, nameTextField,
                imageLabel, selectImage,
                healthLabel, healthTextField,
                initiativeLabel, initiativeTextField,
                creatureTypeChoiceBox
        );
        dialogContent.setAlignment(Pos.CENTER);
        dialog.getDialogPane().setContent(dialogContent);

        // Add Cancel and Add Buttons
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = ButtonType.CANCEL;
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, cancelButtonType);

        Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
        addButton.setId("confirmAddCreatureButton");

        Node cancelButton = dialog.getDialogPane().lookupButton(cancelButtonType);
        cancelButton.setId("cancelButton");  // Set ID for Cancel button

        dialog.setResultConverter(buttonType -> addButtonAction(buttonType, addButtonType, nameTextField, healthTextField, initiativeTextField, creatureTypeChoiceBox, files));

        dialog.showAndWait();
    }

    private void showLoadDialog() {
        //Basic Dialog Setup
        Dialog<Creature> dialog = new Dialog<>();
        dialog.setTitle("Load Old Save");
        dialog.setHeaderText("Select file to upload from.");
        final File[] files = new File[1];

        //Add Select File Button
        Label fileLabel = new Label("File:");
        Button selectFile = new Button("Browse");
        selectFile.setOnAction(actionEvent -> selectLoadFileAction(files, dialog, fileLabel));

        //Add Content to Dialog
        VBox dialogContent = new VBox();
        dialogContent.getChildren().addAll(
                fileLabel, selectFile
        );
        dialogContent.setAlignment(Pos.CENTER);
        dialog.getDialogPane().setContent(dialogContent);

        //Add Load and Cancel Buttons
        ButtonType loadButton = new ButtonType("Load", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loadButton, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> dialogSetResultLoad(buttonType, loadButton, files));

        dialog.showAndWait();
    }
    private void showSaveDialog() {
        //Basic Dialog Setup
        Dialog<Creature> dialog = new Dialog<>();
        dialog.setTitle("Save File");
        dialog.setHeaderText("Select file to save as.");
        final File[] files = new File[1];
        Label fileLabel = new Label("File:");

        //Add Select File Button
        Button selectFile = new Button("Browse");
        selectFile.setOnAction(actionEvent -> selectSaveFileAction(files, dialog, fileLabel));

        //Adding Content to Dialog
        VBox dialogContent = new VBox();
        dialogContent.getChildren().addAll(
                fileLabel, selectFile
        );
        dialogContent.setAlignment(Pos.CENTER);
        dialog.getDialogPane().setContent(dialogContent);

        //Add Save and Cancel Buttons
        ButtonType saveButton = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> dialogSetResultSave(buttonType, saveButton, files));

        dialog.showAndWait();
    }

    private void selectImageAction(File[] files, Dialog<Creature> dialog){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
        files[0] = fileChooser.showOpenDialog(dialog.getOwner());
    }
    private void selectSaveFileAction(File[] files, Dialog<Creature> dialog, Label fileLabel){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Data");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Serialized File", "*.ser")
        );
        files[0] = fileChooser.showSaveDialog(dialog.getOwner());
        if (files[0] != null) {
            fileLabel.setText("File: " + files[0].getName());
            // Process the file immediately after it's selected
            try {
                creatureDao.saveCreatures(files[0]);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void selectLoadFileAction(File[] files, Dialog<Creature> dialog, Label fileLabel){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Save File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Load File", "*.ser")
        );
        files[0] = fileChooser.showOpenDialog(dialog.getOwner());
        if (files[0] != null) {
            fileLabel.setText("File: " + files[0].getName());
            // Process the file immediately after it's selected
            try {
                creatureDao.loadCreatures(files[0]);
                creatureDao.sortByInitiative();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Creature dialogSetResultLoad(ButtonType buttonType, ButtonType loadButton, File[] files){
        if (buttonType == loadButton) {
            try {

                File selectedFile = files[0];
                creatureDao.loadCreatures(selectedFile);
                // Sort the inventory
                creatureDao.sortByInitiative();
                // Update the display
                updateCreatureDisplay();

            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
    private Creature dialogSetResultSave(ButtonType buttonType, ButtonType saveButton, File[] files){
        if (buttonType == saveButton) {
            try {

                File selectedFile = files[0];
                creatureDao.saveCreatures(selectedFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    private Creature addButtonAction(ButtonType buttonType, ButtonType addButton,TextField nameTextField, TextField healthTextField, TextField initiativeTextField, ChoiceBox<String> creatureTypeChoiceBox, File[] files){
        if (buttonType == addButton) {
            try {
                String name = nameTextField.getText();
                int health = Integer.parseInt(healthTextField.getText());
                int initiative = Integer.parseInt(initiativeTextField.getText());
                String selectedCreatureType = creatureTypeChoiceBox.getValue();
                File selectedFile = files[0];

                // Add creature to inventory
                creatureDao.createCreature(selectedCreatureType, name, health, initiative, selectedFile);
                // Sort the inventory
                creatureDao.sortByInitiative();
                // Update the display
                updateCreatureDisplay();

            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Input");
                alert.setContentText("Please enter valid initiative and health values.");
                alert.showAndWait();
            }
        }
        return null;
    }

    private void updateCreatureDisplay(){
        //pull items from records
        creaturePane.getChildren().clear();
        this.roundLabel.setText("Round: " + creatureDao.getRoundNumber());

        //load each existing creature
        for (Creature creature : creatureDao.getCreatureInventory()) {
            //load creature information
            VBox creatureInfoBox = new VBox(10);
            creatureInfoBox.setAlignment(Pos.CENTER);
            Button deleteButton = new Button("DELETE");
            Button deleteConditionButton = new Button("DELETE CONDITION");
            StackPane stackPane = new StackPane();
            Rectangle portrait = new Rectangle(100, 100);
            Rectangle turnBorder = new Rectangle(108, 108);
            turnBorder.setFill(Color.TRANSPARENT);
            if (creature.getImage() != null) {
                portrait.setFill(new ImagePattern(new Image("file:" + creature.getImage().getAbsolutePath())));
            } else {
                portrait.setFill(new ImagePattern(new Image("file:" + new File("./src/main/resources/com/cs422/fxproject/Default_Image.png"))));
            }

            //Set the border color based on creature type
            if (creature instanceof AllyCreature) {
                portrait.setStroke(AllyCreature.border);
            } else if (creature instanceof NeutralCreature) {
                portrait.setStroke(NeutralCreature.border);
            } else if (creature instanceof EnemyCreature) {
                portrait.setStroke(EnemyCreature.border);
            }
            portrait.setStrokeWidth(5);

            //Set a highlight border color for current turn creatures.
            if (creatureDao.getCurrentTurnCreatures().contains(creature)) {
                turnBorder.setStroke(Color.YELLOW);
                turnBorder.setStrokeWidth(5);
            }

            //Add all set borders
            stackPane.getChildren().addAll(turnBorder, portrait);

            //Add information as labels
            Label nameLabel = new Label("| " + creature.getName());
            Label healthLabel = new Label("| " + creature.getCurrentHealth());
            Label initiativeLabel = new Label("| " + creature.getInitiative());
            Label bonusHealthLabel = new Label("| " + creature.getBonusHealth());

            //Add Action Buttons
            HBox buttonsTopRow = new HBox(10); // Top row for buttons
            buttonsTopRow.setAlignment(Pos.CENTER);
            Button damageButton = new Button("DAMAGE");
            Button healButton = new Button("HEAL");

            // Add a button for adding conditions
            Button addConditionButton = new Button("ADD CONDITION");
            buttonsTopRow.getChildren().addAll(addConditionButton, deleteConditionButton);

            //Add Button for Bonus Health
            HBox buttonsBottomRow = new HBox(10); // Bottom row for buttons
            buttonsBottomRow.setAlignment(Pos.CENTER);
            Button bonusHealthButton = new Button("BONUS HEALTH");

            //Add Buttons to Frame
            buttonsBottomRow.getChildren().addAll(deleteButton, damageButton, healButton, bonusHealthButton);

            deleteButton.setId("deleteButton_" + creature.getName().replace(" ", ""));
            deleteConditionButton.setId("deleteConditionButton_" + creature.getName().replace(" ", ""));
            damageButton.setId("damageButton_" + creature.getName().replace(" ", ""));
            healButton.setId("healButton_" + creature.getName().replace(" ", ""));
            addConditionButton.setId("addConditionButton_" + creature.getName().replace(" ", ""));
            bonusHealthButton.setId("bonusHealthButton_" + creature.getName().replace(" ", ""));

            //Styling and Labels
            GridPane characterInfo = new GridPane();
            characterInfo.setAlignment(Pos.CENTER);
            characterInfo.setHgap(10);
            characterInfo.setVgap(5);
            characterInfo.add(new Label("Name"), 0, 0);
            characterInfo.add(nameLabel, 1, 0);
            characterInfo.add(new Label("Health"), 0, 1);
            characterInfo.add(healthLabel, 1, 1);
            characterInfo.add(new Label("Initiative"), 0, 2);
            characterInfo.add(initiativeLabel, 1, 2);
            characterInfo.add(new Label("Bonus Health"), 0, 3);
            characterInfo.add(bonusHealthLabel, 1, 3);

            // Display creature conditions
            final List<Condition>[] currentConditions = new List[]{creature.getCurrentConditions()};
            if (!currentConditions[0].isEmpty()) {
                StringBuilder conditionsText = new StringBuilder("Conditions   | ");
                int conditionCount = 0;

                for (Condition condition : currentConditions[0]) {
                    conditionsText.append(condition).append(", ");
                    conditionCount++;

                    //Check if it's the second condition, and if so, add a new line
                    if (conditionCount % 2 == 0) {
                        conditionsText.append("\n"); // Add a new line
                    }
                }

                conditionsText.setLength(conditionsText.length() - 2); // Remove the trailing comma and space
                Label conditionsLabel = new Label(conditionsText.toString());
                characterInfo.add(conditionsLabel, 0, 4, 2, 1); // Span 2 columns
            }

            //Add Button Events
            damageButton.setOnAction(event -> damageButtonEvent(creature, healthLabel, bonusHealthLabel));
            healButton.setOnAction(event -> healButtonEvent(creature, healthLabel, bonusHealthLabel));
            addConditionButton.setOnAction(event -> conditionButtonEvent(creature));
            deleteConditionButton.setOnAction(event -> deleteConditionButtonEvent(currentConditions, creature));
            bonusHealthButton.setOnAction(event -> bonusHealthButtonEvent(creature, bonusHealthLabel));

            //Add Buttons to Frame
            creatureInfoBox.getChildren().addAll(buttonsTopRow, buttonsBottomRow, stackPane, characterInfo);
            creaturePane.getChildren().add(creatureInfoBox);

            deleteButton.setOnAction(event -> deleteButtonEvent(creature));
        }
    }

    private void bonusHealthButtonEvent(Creature creature, Label bonusHealthLabel){
        TextInputDialog bonusHealthDialog = new TextInputDialog();
        bonusHealthDialog.setTitle("Bonus Health");
        bonusHealthDialog.setHeaderText("Enter the bonus health amount:");
        bonusHealthDialog.setContentText("Amount:");

        Optional<String> bonusHealthResult = bonusHealthDialog.showAndWait();
        bonusHealthResult.ifPresent(bonusHealth -> ifBonusHealthPresent(bonusHealth, creature, bonusHealthLabel));
    }
    private void conditionButtonEvent(Creature creature){
        // Create the custom dialog
        Dialog<Pair<String, Integer>> dialog = new Dialog<>();
        dialog.setTitle("Add Condition");

        // Set up the buttons
        ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        Node addButtonNode = dialog.getDialogPane().lookupButton(addButton);
        addButtonNode.setId("confirmConditionYesButton");

        // Create the choice box and text field
        ChoiceBox<String> conditionChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList(creature.getAllConditions()));
        conditionChoiceBox.setValue(creature.getAllConditions().get(0));
        conditionChoiceBox.setId("conditionChoiceBox");
        TextField durationTextField = new TextField();
        durationTextField.setId("durationTextField");

        //Add content to dialog
        VBox content = new VBox();
        content.getChildren().addAll(new Label("Condition:"), conditionChoiceBox, new Label("Rounds:"), durationTextField);
        dialog.getDialogPane().setContent(content);

        // Convert the result to a pair
        dialog.setResultConverter(dialogButton -> convertResultToPair(dialogButton, addButton, durationTextField, conditionChoiceBox));

        // Get the result and use it
        Optional<Pair<String, Integer>> result = dialog.showAndWait();
        result.ifPresent(conditionDurationPair -> pairPresent(conditionDurationPair, creature));
    }
    private void deleteConditionButtonEvent(List<Condition>[] currentConditions, Creature creature){
        currentConditions[0] = creature.getCurrentConditions();
        if (!currentConditions[0].isEmpty()) {
            ChoiceDialog<Condition> deleteConditionDialog = new ChoiceDialog<>(currentConditions[0].get(0), currentConditions[0]);
            deleteConditionDialog.setTitle("DELETE CONDITION");
            deleteConditionDialog.setHeaderText("Select a condition to delete:");
            deleteConditionDialog.setContentText("Condition:");

            Optional<Condition> selectedCondition = deleteConditionDialog.showAndWait();
            selectedCondition.ifPresent(condition -> ifConditionPresent(creature, condition));
        }
    }
    private void deleteButtonEvent(Creature creature){
        boolean deleteConfirmed = showDeleteConfirmationDialog();
        if (deleteConfirmed) {
            creatureDao.deleteCreature(creature);
            updateCreatureDisplay();
        }
    }
    private void damageButtonEvent(Creature creature, Label healthLabel, Label bonusHealthLabel){
        VBox damageContent = new VBox();
        TextInputDialog damageDialog = new TextInputDialog();
        damageDialog.setTitle("Damage Creature");
        damageDialog.setHeaderText("Enter the damage amount:");
        damageDialog.setContentText("Amount:");
        CheckBox critBox = new CheckBox("Crit");
        critBox.setId("critBox");
        damageContent.getChildren().addAll(damageDialog.getDialogPane().getContent(), critBox);
        damageDialog.getDialogPane().setContent(damageContent);
        damageDialog.getDialogPane().setId("damageDialog");

        Optional<String> damageResult = damageDialog.showAndWait();
        damageResult.ifPresent(damage -> doDamage(damage, creature, critBox, healthLabel, bonusHealthLabel));
    }
    private  void healButtonEvent(Creature creature, Label healthLabel, Label bonusHealthLabel){
        TextInputDialog healDialog = new TextInputDialog();
        healDialog.setTitle("Heal Creature");
        healDialog.setHeaderText("Enter the healing amount:");
        healDialog.setContentText("Amount:");

        Optional<String> healResult = healDialog.showAndWait();
        healResult.ifPresent(heal -> doHealing(heal, creature, healthLabel, bonusHealthLabel));
    }

    private void doDamage(String damage, Creature creature, CheckBox critBox, Label healthLabel, Label bonusHealthLabel){
        int damageAmount = Integer.parseInt(damage);
        // Subtract from current health
        creature.removeHealth(damageAmount, critBox.isSelected());
        healthLabel.setText("| " + creature.getCurrentHealth() + "/" + creature.getMaxHealth());

        // Recalculate and update bonus health label
        bonusHealthLabel.setText("| " + creature.getBonusHealth());
    }
    private void  doHealing(String heal, Creature creature, Label healthLabel, Label bonusHealthLabel){
        int healAmount = Integer.parseInt(heal);
        // Add to current health (up to maximum health)
        creature.addHealth(healAmount);
        healthLabel.setText("| " + creature.getCurrentHealth());

        // Recalculate and update bonus health label
        bonusHealthLabel.setText("| " + creature.getBonusHealth());
    }

    private Pair<String, Integer> convertResultToPair(ButtonType dialogButton, ButtonType addButton, TextField durationTextField, ChoiceBox<String> conditionChoiceBox){
        if (dialogButton == addButton) {
            int duration;
            try {
                duration = Integer.parseInt(durationTextField.getText());
            } catch (NumberFormatException e) {
                duration = 99;
            }
            return new Pair<>(conditionChoiceBox.getValue(), duration);
        }
        return null;
    }

    private void ifBonusHealthPresent(String bonusHealth, Creature creature, Label bonusHealthLabel){
        int bonusHealthAmount = Integer.parseInt(bonusHealth);
        // Set bonus health using the addBonusHealth function
        creature.addBonusHealth(bonusHealthAmount);
        bonusHealthLabel.setText("| " + creature.getBonusHealth());
    }
    private void ifConditionPresent(Creature creature, Condition condition){
        creature.removeCondition(condition);
        updateCreatureDisplay(); // Update the display to remove the deleted condition
    }
    private void pairPresent(Pair<String, Integer> conditionDurationPair, Creature creature){
        String condition = conditionDurationPair.getKey();
        int duration = conditionDurationPair.getValue();
        creature.addCondition(condition, duration);

        updateCreatureDisplay(); // Update the display to show the new condition
    }

    private void nextTurnPressed(){
        creatureDao.advanceTurn();
        updateCreatureDisplay();
    }

    private boolean showDeleteConfirmationDialog() {
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Confirmation");
        confirmationDialog.setHeaderText("Are you sure you want to delete this creature?");
        confirmationDialog.setContentText("Choose your option.");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmationDialog.getButtonTypes().setAll(yesButton, noButton);

        Node yesButtonNode = confirmationDialog.getDialogPane().lookupButton(yesButton);
        yesButtonNode.setId("confirmDeleteYesButton");

        Node noButtonNode = confirmationDialog.getDialogPane().lookupButton(noButton);
        noButtonNode.setId("confirmDeleteNoButton");

        Optional<ButtonType> result = confirmationDialog.showAndWait();

        return result.isPresent() && result.get() == yesButton;
    }
}