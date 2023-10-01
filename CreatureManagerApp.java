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
        creaturePane.setMinWidth(800); // Set a minimum width for the creature display area.

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

        // Create and configure the dialog content.
        Label nameLabel = new Label("Name:");
        TextField nameTextField = new TextField();
        Label healthLabel = new Label("Health:");
        TextField healthTextField = new TextField();
        Label initiativeLabel = new Label("Initiative:");
        TextField initiativeTextField = new TextField();

        VBox dialogContent = new VBox(10);
        dialogContent.getChildren().addAll(nameLabel, nameTextField, healthLabel, healthTextField, initiativeLabel, initiativeTextField);
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

                    Creature creature = new Creature(name, health, 0, initiative);
                    creatures.add(creature);

                    Collections.sort(creatures, (c1, c2) -> Integer.compare(c2.getInitiative(), c1.getInitiative()));

                    updateCreatureDisplay();

                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Invalid Input");
                    alert.setContentText("Please enter a valid initiative value.");
                    alert.showAndWait();
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void updateCreatureDisplay() {
        creaturePane.getChildren().clear();
        for (Creature creature : creatures) {
            VBox creatureInfoBox = new VBox(10);
            creatureInfoBox.setAlignment(Pos.CENTER);

            Button deleteButton = new Button("DELETE");

            Rectangle blackSquare = new Rectangle(100, 100);
            blackSquare.setFill(Color.BLACK);

            Label creatureLabel = new Label("Name: " + creature.getName() + " | Health: " + creature.getCurrentHealth() + " | Initiative: " + creature.getInitiative());

            creatureInfoBox.getChildren().addAll(deleteButton, blackSquare, creatureLabel);

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

