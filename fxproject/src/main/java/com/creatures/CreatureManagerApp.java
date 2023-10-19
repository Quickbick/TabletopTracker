package com.creatures;

import com.condition_manager.Condition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
        primaryStage.setTitle("Creature Manager");

        BorderPane root = new BorderPane();
        creaturePane = new FlowPane(10, 10);
        creaturePane.setAlignment(Pos.CENTER);
        creaturePane.setMinWidth(800);
        ScrollPane sp = new ScrollPane(creaturePane);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setFitToWidth(true);

        Button addButton = new Button("Add Creature");
        addButton.setOnAction(e -> showCreatureDialog());

        // Save Button
        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> showSaveDialog());

        // Load Button
        Button loadButton = new Button("Load");
        loadButton.setOnAction(e -> showLoadDialog());

        // Next Turn Button
        Button nextTurnButton = new Button("Next Turn");
        nextTurnButton.setOnAction(e -> {
            creatureDao.advanceTurn();
            updateCreatureDisplay();
        });

        roundLabel = new Label("Round: " + creatureDao.getRoundNumber());
        roundLabel.setPadding(new Insets(5, 10, 5, 10));
        roundLabel.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(5), Insets.EMPTY)));
        roundLabel.setBorder(new Border(new BorderStroke(Color.DARKGRAY, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(2))));

        HBox hbox = new HBox();
        hbox.getChildren().addAll(addButton, saveButton, loadButton, nextTurnButton);

        BorderPane topPane = new BorderPane();
        topPane.setLeft(hbox);
        topPane.setRight(roundLabel);

        root.setTop(topPane);
        root.setCenter(sp);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showSaveDialog() {
        Dialog<Creature> dialog = new Dialog<>();
        dialog.setTitle("Save File");
        dialog.setHeaderText("Select file to save as.");
        final File[] files = new File[1];

        Label fileLabel = new Label("File:");
        Button selectFile = new Button("Browse");
        selectFile.setOnAction(actionEvent -> {
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
        });

        VBox dialogContent = new VBox();
        dialogContent.getChildren().addAll(
                fileLabel, selectFile
        );
        dialogContent.setAlignment(Pos.CENTER);

        dialog.getDialogPane().setContent(dialogContent);

        ButtonType saveButton = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == saveButton) {
                try {

                    File selectedFile = files[0];
                    creatureDao.saveCreatures(selectedFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showCreatureDialog() {
        Dialog<Creature> dialog = new Dialog<>();
        dialog.setTitle("Add Creature");
        dialog.setHeaderText("Enter Creature Details");
        final File[] files = new File[1];

        Label nameLabel = new Label("Name:");
        TextField nameTextField = new TextField();
        Label healthLabel = new Label("Health:");
        TextField healthTextField = new TextField();
        Label initiativeLabel = new Label("Initiative:");
        TextField initiativeTextField = new TextField();
        Label imageLabel = new Label("Image:");
        Button selectImage = new Button("Browse");
        selectImage.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Image File");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
            );
            files[0] = fileChooser.showOpenDialog(dialog.getOwner());
        });

        ChoiceBox<String> creatureTypeChoiceBox = new ChoiceBox<>();
        creatureTypeChoiceBox.getItems().addAll("ALLY", "NEUTRAL", "ENEMY");
        creatureTypeChoiceBox.setValue("ALLY");

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

        ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
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
        });

        dialog.showAndWait();
    }

    private void showLoadDialog() {
        Dialog<Creature> dialog = new Dialog<>();
        dialog.setTitle("Load Old Save");
        dialog.setHeaderText("Select file to upload from.");
        final File[] files = new File[1];

        Label fileLabel = new Label("File:");
        Button selectFile = new Button("Browse");
        selectFile.setOnAction(actionEvent -> {
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
        });


        VBox dialogContent = new VBox();
        dialogContent.getChildren().addAll(
                fileLabel, selectFile
        );
        dialogContent.setAlignment(Pos.CENTER);

        dialog.getDialogPane().setContent(dialogContent);

        ButtonType loadButton = new ButtonType("Load", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loadButton, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
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
        });

        dialog.showAndWait();
    }

    private void updateCreatureDisplay(){
        creaturePane.getChildren().clear();
        this.roundLabel.setText("Round: " + creatureDao.getRoundNumber());
        for (Creature creature : creatureDao.getCreatureInventory()) {
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

            // Set the border color based on creature type
            if (creature instanceof AllyCreature) {
                portrait.setStroke(AllyCreature.border);
            } else if (creature instanceof NeutralCreature) {
                portrait.setStroke(NeutralCreature.border);
            } else if (creature instanceof EnemyCreature) {
                portrait.setStroke(EnemyCreature.border);
            }
            portrait.setStrokeWidth(5);

            // Set a highlight border color for current turn creatures.
            if (creatureDao.getCurrentTurnCreatures().contains(creature)) {
                turnBorder.setStroke(Color.YELLOW);
                turnBorder.setStrokeWidth(5);
            }

            stackPane.getChildren().addAll(turnBorder, portrait);

            Label nameLabel = new Label("| " + creature.getName());
            Label healthLabel = new Label("| " + creature.getCurrentHealth());
            Label initiativeLabel = new Label("| " + creature.getInitiative());
            Label bonusHealthLabel = new Label("| " + creature.getBonusHealth());

            HBox buttonsTopRow = new HBox(10); // Top row for buttons
            buttonsTopRow.setAlignment(Pos.CENTER);
            Button damageButton = new Button("DAMAGE");
            Button healButton = new Button("HEAL");

            // Add a button for adding conditions
            Button addConditionButton = new Button("ADD CONDITION");

            buttonsTopRow.getChildren().addAll(addConditionButton, deleteConditionButton);

            HBox buttonsBottomRow = new HBox(10); // Bottom row for buttons
            buttonsBottomRow.setAlignment(Pos.CENTER);
            Button bonusHealthButton = new Button("BONUS HEALTH");
            buttonsBottomRow.getChildren().addAll(deleteButton, damageButton, healButton, bonusHealthButton);

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

                    // Check if it's the third condition, and if so, add a new line
                    if (conditionCount % 3 == 0) {
                        conditionsText.append("\n"); // Add a new line
                    }
                }

                conditionsText.setLength(conditionsText.length() - 2); // Remove the trailing comma and space
                Label conditionsLabel = new Label(conditionsText.toString());
                characterInfo.add(conditionsLabel, 0, 4, 2, 1); // Span 2 columns
            }

            damageButton.setOnAction(event -> {
                VBox damageContent = new VBox();
                TextInputDialog damageDialog = new TextInputDialog();
                damageDialog.setTitle("Damage Creature");
                damageDialog.setHeaderText("Enter the damage amount:");
                damageDialog.setContentText("Amount:");
                CheckBox critBox = new CheckBox("Crit");
                damageContent.getChildren().addAll(damageDialog.getDialogPane().getContent(), critBox);
                damageDialog.getDialogPane().setContent(damageContent);

                Optional<String> damageResult = damageDialog.showAndWait();
                damageResult.ifPresent(damage -> {
                    int damageAmount = Integer.parseInt(damage);
                    // Subtract from current health
                    creature.removeHealth(damageAmount, critBox.isSelected());
                    healthLabel.setText("| " + creature.getCurrentHealth() + "/" + creature.getMaxHealth());

                    // Recalculate and update bonus health label
                    bonusHealthLabel.setText("| " + creature.getBonusHealth());
                });
            });

            healButton.setOnAction(event -> {
                TextInputDialog healDialog = new TextInputDialog();
                healDialog.setTitle("Heal Creature");
                healDialog.setHeaderText("Enter the healing amount:");
                healDialog.setContentText("Amount:");

                Optional<String> healResult = healDialog.showAndWait();
                healResult.ifPresent(heal -> {
                    int healAmount = Integer.parseInt(heal);
                    // Add to current health (up to maximum health)
                    creature.addHealth(healAmount);
                    healthLabel.setText("| " + creature.getCurrentHealth());

                    // Recalculate and update bonus health label
                    bonusHealthLabel.setText("| " + creature.getBonusHealth());
                });
            });

            // Add condition handling
            addConditionButton.setOnAction(event -> {
                // Create the custom dialog
                Dialog<Pair<String, Integer>> dialog = new Dialog<>();
                dialog.setTitle("Add Condition");

                // Set up the buttons
                ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

                // Create the choice box and text field
                ChoiceBox<String> conditionChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList(creature.getAvailableConditions()));
                conditionChoiceBox.setValue(creature.getAvailableConditions().get(0));
                TextField durationTextField = new TextField();

                VBox content = new VBox();
                content.getChildren().addAll(new Label("Condition:"), conditionChoiceBox, new Label("Rounds:"), durationTextField);
                dialog.getDialogPane().setContent(content);

                // Convert the result to a pair
                dialog.setResultConverter(dialogButton -> {
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
                });

                // Get the result and use it
                Optional<Pair<String, Integer>> result = dialog.showAndWait();

                result.ifPresent(conditionDurationPair -> {
                    String condition = conditionDurationPair.getKey();
                        int duration = conditionDurationPair.getValue();
                        creature.addCondition(condition, duration);

                    updateCreatureDisplay(); // Update the display to show the new condition
                });
            });

            // Delete condition handling
            deleteConditionButton.setOnAction(event -> {
                currentConditions[0] = creature.getCurrentConditions();
                if (!currentConditions[0].isEmpty()) {
                    ChoiceDialog<Condition> deleteConditionDialog = new ChoiceDialog<>(currentConditions[0].get(0), currentConditions[0]);
                    deleteConditionDialog.setTitle("DELETE CONDITION");
                    deleteConditionDialog.setHeaderText("Select a condition to delete:");
                    deleteConditionDialog.setContentText("Condition:");

                    Optional<Condition> selectedCondition = deleteConditionDialog.showAndWait();
                    selectedCondition.ifPresent(condition -> {
                        creature.removeCondition(condition);
                        updateCreatureDisplay(); // Update the display to remove the deleted condition
                    });
                }
            });

            bonusHealthButton.setOnAction(event -> {
                TextInputDialog bonusHealthDialog = new TextInputDialog();
                bonusHealthDialog.setTitle("Bonus Health");
                bonusHealthDialog.setHeaderText("Enter the bonus health amount:");
                bonusHealthDialog.setContentText("Amount:");

                Optional<String> bonusHealthResult = bonusHealthDialog.showAndWait();
                bonusHealthResult.ifPresent(bonusHealth -> {
                    int bonusHealthAmount = Integer.parseInt(bonusHealth);
                    // Set bonus health using the addBonusHealth function
                    creature.addBonusHealth(bonusHealthAmount);
                    bonusHealthLabel.setText("| " + creature.getBonusHealth());
                });
            });

            creatureInfoBox.getChildren().addAll(buttonsTopRow, buttonsBottomRow, stackPane, characterInfo);
            creaturePane.getChildren().add(creatureInfoBox);

            deleteButton.setOnAction(event -> {
                boolean deleteConfirmed = showDeleteConfirmationDialog();
                if (deleteConfirmed) {
                    creatureDao.deleteCreature(creature);
                    updateCreatureDisplay();
                }
            });
        }
    }

    private boolean showDeleteConfirmationDialog() {
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Confirmation");
        confirmationDialog.setHeaderText("Are you sure you want to delete this creature?");
        confirmationDialog.setContentText("Choose your option.");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmationDialog.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = confirmationDialog.showAndWait();

        return result.isPresent() && result.get() == yesButton;
    }
}