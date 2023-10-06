package com.creatures;

import javafx.scene.paint.Color;

import java.io.File;

public class AllyCreature extends Creature {

    /**
     * Creates a new Creature with name, health, and initiative.
     *
     * @param name       Name of Creature
     * @param maxHealth  Max Health of Creature
     * @param initiative Initiative Number
     * @param image      Creature Image
     */
    AllyCreature(String name, int maxHealth, int initiative, File image) {
        super(name, maxHealth, initiative, image);
    }

    /**
     * Border color for Ally creatures.
     */
    public static Color border = Color.GREEN;
}
