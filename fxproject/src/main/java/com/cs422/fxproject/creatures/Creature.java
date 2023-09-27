package com.cs422.fxproject.creatures;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

abstract class Creature {
    private final Image image;
    private final String name;
    private final int maxHealth;
    private int currentHealth;
    private int BonusHealth;
    private final int initiative;
    private List<Boolean> currentConditions = new ArrayList<>(Conditions.values().length);
  
    public int getInitiative() {
        return initiative;
    }

    /**
     * Creates a new Creature with image, name, health, and initiative.
     * @param image Image of Creature
     * @param name Name of Creature
     * @param maxHealth Max Health of Creature
     * @param initiative Initiative Number
     */
    Creature(Image image, String name, int maxHealth, int initiative) {

        this.image = image;
        this.name = name;
        this.maxHealth = maxHealth;
        this.initiative = initiative;
        this.currentHealth = this.maxHealth;
        this.currentBonusHealth = 0;
        this.conditions = new List<Status>;
    }

    /**
     * This function adds a status to the creature's condition list
     * @param status
     */
    private void addStatus(Status status){
        if (!this.conditions.stream().anyMatch(c -> c.equals(status))){
            this.conditions.add(status);
        }
    }

    /**
     * This function removes a status from a creature's condition list.
     * @param status
     */
    private  void removeStatus(Status status){
        int removeMe = this.conditions.indexOf(status);
        this.conditions.remove(removeMe);
    }

    /**
     * This function adds health to the creature.
     * @param healthPoints Points to heal
     */
    public void addHealth(int healthPoints) {
        if (this.currentHealth + healthPoints > this.maxHealth) {
            this.currentHealth = this.maxHealth;
        }
        else {
            this.currentHealth += healthPoints;
        }
    }


    /**
     * This function sets the bonus health of a creature.
     * If the new bonus health is lower than the old, we do not update it.
     * @param newBonusHealth new Max Bonus health
     */
    public void addBonusHealth(int newBonusHealth) {
        // If the new bonus health is greater than the current bonus health,
        // replace the bonus health with the new value. Otherwise, do not replace it
        if (newBonusHealth > this.currentBonusHealth) {
            this.currentBonusHealth = this.maxBonusHealth = newBonusHealth;
        }
    }

    /**
     * Adds a condition to the creature.
     * @param condition Health Condition to Add.
     */
    public void addCondition(Conditions condition) {
        this.currentConditions.set(condition.ordinal(), true);
    }


    /**
     * Remove the condition from the creature.
     * @param condition Health Condition to remove.
     */
    public void removeCondition(Conditions condition) {
        this.currentConditions.set(condition.ordinal(), false);
    }

    /**
     * This function removes health from the creature. Sets the creature to Unconscious if HP goes to 0.
     * @param healthPoints Damage to take
     */
    public void removeHealth(int healthPoints) {
        // Remove health from bonus health first.
        if (this.currentBonusHealth - healthPoints > 0) {
            this.currentBonusHealth -= healthPoints;
        } else if (this.currentBonusHealth - healthPoints <= 0) {
            this.currentBonusHealth = 0;
            int dmg = healthPoints - this.currentBonusHealth;
            this.currentHealth -= dmg;
        }

        if (this.currentHealth <= 0) {
            // Creature has died.
            this.currentHealth = 0;
            //add unconcious to status list
        }
    }
}
