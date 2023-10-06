package com.cs422.fxproject.creatures;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

abstract class Creature implements Serializable {
    private static final long serializableUID = 1L;
    private final String name;
    private final File image;
    private final int maxHealth;
    private int currentHealth;
    private int bonusHealth;
    private final int initiative;
    private List<Conditions> currentConditions = new ArrayList<>();

    public int getInitiative() {
        return initiative;
    }

    /**
     * Creates a new Creature with name, health, and initiative.
     *
     * @param name        Name of Creature
     * @param maxHealth   Max Health of Creature
     * @param initiative  Initiative Number
     * @param image       Creature Image
     */
    Creature(String name, int maxHealth, int initiative, File image) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.initiative = initiative;
        this.currentHealth = this.maxHealth;
        this.bonusHealth = 0;
        this.image = image;
    }

    /**
     * This function adds health to the creature.
     *
     * @param healthPoints Points to heal
     */
    public void addHealth(int healthPoints) {
        if (this.currentHealth + healthPoints > this.maxHealth) {
            this.currentHealth = this.maxHealth;
        } else {
            this.currentHealth += healthPoints;
        }
    }

    /**
     * This function sets the bonus health of a creature.
     * If the new bonus health is lower than the old, we do not update it.
     *
     * @param newBonusHealth new Max Bonus health
     */
    public void addBonusHealth(int newBonusHealth) {
        // If the new bonus health is greater than the current bonus health,
        // replace the bonus health with the new value. Otherwise, do not replace it
        if (newBonusHealth > this.bonusHealth) {
            this.bonusHealth = newBonusHealth;
        }
    }

    /**
     * Adds a condition to the creature.
     *
     * @param condition Health Condition to Add.
     */
    public void addCondition(Conditions condition) {
        if (!currentConditions.contains(condition)) {
            currentConditions.add(condition);
        }
    }

    /**
     * Remove the condition from the creature.
     *
     * @param condition Health Condition to remove.
     */
    public void removeCondition(Conditions condition) {
        currentConditions.remove(condition);
    }

    public List<Conditions> getCurrentConditions() {
        return currentConditions;
    }


    /**
     * This function removes health from the creature. Sets the creature to Unconscious if HP goes to 0.
     *
     * @param healthPoints Damage to take
     */
    public void removeHealth(int healthPoints) {
        // Remove health from bonus health first.
        if (this.bonusHealth - healthPoints > 0) {
            this.bonusHealth -= healthPoints;
        } else if (this.bonusHealth - healthPoints <= 0) {
            int dmg = healthPoints - this.bonusHealth;
            this.bonusHealth = 0;
            this.currentHealth -= dmg;
        }

        if (this.currentHealth <= 0) {
            // Creature has died.
            this.currentHealth = 0;
            //add unconscious to status list
            this.addCondition(Conditions.Unconcious);
        }
    }

    public String getName() {
        return this.name;
    }

    public int getCurrentHealth() {
        return this.currentHealth;
    }

    public int getBonusHealth(){ return this.bonusHealth; }

    public int getMaxHealth(){return this.maxHealth; }

    public File getImage(){return this.image;}

}
