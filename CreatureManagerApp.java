package com.cs422.fxproject.creatures;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CreatureManagerApp extends Application {
    private List<Creature> creatures = new ArrayList<>();
    private FlowPane creaturePane;

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

        Button addButton = new Button("Add Creature");
        addButton.setOnAction(e -> showCreatureDialog());

        root.setTop(addButton);
        root.setCenter(creaturePane);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showCreatureDialog() {
        Dialog<Creature> dialog = new Dialog<>();
        dialog.setTitle("Add Creature");
        dialog.setHeaderText("Enter Creature Details");

        Label nameLabel = new Label("Name:");
        TextField nameTextField = new TextField();
        Label healthLabel = new Label("Health:");
        TextField healthTextField = new TextField();
        Label initiativeLabel = new Label("Initiative:");
        TextField initiativeTextField = new TextField();

        ChoiceBox<String> creatureTypeChoiceBox = new ChoiceBox<>();
        creatureTypeChoiceBox.getItems().addAll("ALLY", "NEUTRAL", "ENEMY");
        creatureTypeChoiceBox.setValue("ALLY");

        VBox dialogContent = new VBox(10);
        dialogContent.getChildren().addAll(
                nameLabel, nameTextField,
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

                    Creature creature = createCreature(selectedCreatureType, name, health, initiative);

                    if (creature != null) {
                        // Add the created creature to the list
                        creatures.add(creature);

                        // Sort the creatures based on initiative
                        Collections.sort(creatures, (c1, c2) -> Integer.compare(c2.getInitiative(), c1.getInitiative()));

                        // Update the display
                        updateCreatureDisplay();
                    }
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

    private Creature createCreature(String creatureType, String name, int health, int initiative) {
        switch (creatureType) {
            case "ALLY":
                return new AllyCreature(name, health, initiative);
            case "NEUTRAL":
                return new NeutralCreature(name, health, initiative);
            case "ENEMY":
                return new EnemyCreature(name, health, initiative);
            default:
                return null;
        }
    }

    private void updateCreatureDisplay() {
        creaturePane.getChildren().clear();
        for (Creature creature : creatures) {
            VBox creatureInfoBox = new VBox(10);
            creatureInfoBox.setAlignment(Pos.CENTER);

            Button deleteButton = new Button("DELETE");
            Button deleteConditionButton = new Button("DELETE CONDITION");

            Rectangle blackSquare = new Rectangle(100, 100);
            blackSquare.setFill(Color.BLACK);

            // Set the border color based on creature type
            if (creature instanceof AllyCreature) {
                blackSquare.setStroke(AllyCreature.border);
                blackSquare.setStrokeWidth(5);
            } else if (creature instanceof NeutralCreature) {
                blackSquare.setStroke(NeutralCreature.border);
                blackSquare.setStrokeWidth(5);
            } else if (creature instanceof EnemyCreature) {
                blackSquare.setStroke(EnemyCreature.border);
                blackSquare.setStrokeWidth(5);
            }

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
            final List<Conditions>[] currentConditions = new List[]{creature.getCurrentConditions()};
            if (!currentConditions[0].isEmpty()) {
                StringBuilder conditionsText = new StringBuilder("Conditions   | ");
                int conditionCount = 0;

                for (Conditions condition : currentConditions[0]) {
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
                TextInputDialog damageDialog = new TextInputDialog();
                damageDialog.setTitle("Damage Creature");
                damageDialog.setHeaderText("Enter the damage amount:");
                damageDialog.setContentText("Amount:");

                Optional<String> damageResult = damageDialog.showAndWait();
                damageResult.ifPresent(damage -> {
                    int damageAmount = Integer.parseInt(damage);
                    // Subtract from current health
                    creature.removeHealth(damageAmount);
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
                ChoiceDialog<Conditions> conditionDialog = new ChoiceDialog<>(Conditions.values()[0], Conditions.values());
                conditionDialog.setTitle("ADD CONDITION");
                conditionDialog.setHeaderText("Select a condition to add:");
                conditionDialog.setContentText("Condition:");

                Optional<Conditions> selectedCondition = conditionDialog.showAndWait();
                selectedCondition.ifPresent(condition -> {
                    creature.addCondition(condition);
                    updateCreatureDisplay(); // Update the display to show the new condition
                });
            });

            // Delete condition handling
            deleteConditionButton.setOnAction(event -> {
                currentConditions[0] = creature.getCurrentConditions();
                if (!currentConditions[0].isEmpty()) {
                    ChoiceDialog<Conditions> deleteConditionDialog = new ChoiceDialog<>(currentConditions[0].get(0), currentConditions[0]);
                    deleteConditionDialog.setTitle("DELETE CONDITION");
                    deleteConditionDialog.setHeaderText("Select a condition to delete:");
                    deleteConditionDialog.setContentText("Condition:");

                    Optional<Conditions> selectedCondition = deleteConditionDialog.showAndWait();
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

            creatureInfoBox.getChildren().addAll(buttonsTopRow, buttonsBottomRow, blackSquare, characterInfo);

            creaturePane.getChildren().add(creatureInfoBox);

            deleteButton.setOnAction(event -> {
                boolean deleteConfirmed = showDeleteConfirmationDialog();
                if (deleteConfirmed) {
                    creatures.remove(creature);
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
