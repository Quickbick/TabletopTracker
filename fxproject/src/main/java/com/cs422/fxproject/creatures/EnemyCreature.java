package com.cs422.fxproject.creatures;

import javafx.scene.paint.Color;

public class EnemyCreature extends Creature {
    /**
     * Creates a new Creature with a name, health, and initiative.
     *
     * @param name        Name of Creature
     * @param maxHealth   Max Health of Creature
     * @param initiative  Initiative Number
     */
    EnemyCreature(String name, int maxHealth, int initiative) {
        super(name, maxHealth, initiative);
    }

    /**
     * Border color for Enemy creatures.
     */
    public static Color border = Color.RED;
}
