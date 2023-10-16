package com.creatures;

import com.condition_manager.Condition;
import com.condition_manager.ConditionDao;
import com.condition_manager.ConditionDaoImpl;

import java.io.File;
import java.io.Serializable;
import java.util.List;

abstract class Creature implements Serializable {
    private final String name;
    private final File image;
    private final int maxHealth;
    private int currentHealth;
    private int bonusHealth;
    private final int initiative;
    private final ConditionDao conditionDao = new ConditionDaoImpl();

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
//            this.addCondition(Conditions.Unconscious);
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
     * Adds a condition to the condition Dao.
     * @param conditionType Type of condition to create.
     * @param duration Round duration of c
     */
    public void addCondition(String conditionType, int duration) {
        Condition newCondition = this.conditionDao.createCondition(conditionType, duration);

        // Check if an object of the same class type as newCondition already exists
        boolean exists = this.conditionDao.getCurrentConditions().stream()
                .anyMatch(existingCondition -> existingCondition.getClass().equals(newCondition.getClass()));

        if (!exists) {
            this.conditionDao.addCurrentCondition(newCondition);
        }
    }

    /**
     * Remove the condition from the creature.
     *
     * @param condition Health Condition to remove.
     */
    public void removeCondition(Condition condition) {
        conditionDao.removeCurrentCondition(condition);
    }

    /**
     * Lower condition count by 1 round.
     */
    public void decrementConditions() {
        this.conditionDao.decreaseAllConditionDurations();
    }

    public String getName() {
        return this.name;
    }
    public File getImage(){return this.image;}
    public int getMaxHealth(){return this.maxHealth; }
    public int getCurrentHealth() {
        return this.currentHealth;
    }
    public int getBonusHealth(){ return this.bonusHealth; }
    public int getInitiative() {
        return initiative;
    }
    public List<Condition> getCurrentConditions() {
        return conditionDao.getCurrentConditions();
    }
    public List<String> getAvailableConditions() {return conditionDao.getAvailableConditions(); }
}
