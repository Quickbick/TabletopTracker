package com.cs422.fxproject.creatures;

import javafx.scene.paint.Color;

public class AllyCreature extends Creature {

    /**
     * Creates a new Creature with name, health, and initiative.
     *
     * @param name        Name of Creature
     * @param maxHealth   Max Health of Creature
     * @param initiative  Initiative Number
     */
    AllyCreature(String name, int maxHealth, int initiative) {
        super(name, maxHealth, initiative);
    }

    /**
     * Border color for Ally creatures.
     */
    public static Color border = Color.GREEN;
}
