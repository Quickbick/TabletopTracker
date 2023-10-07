package com.creatures;

import javafx.scene.paint.Color;

import java.io.File;

public class NeutralCreature extends Creature {
    /**
     * Creates a new Creature with a name, health, and initiative.
     *
     * @param name       Name of Creature
     * @param maxHealth  Max Health of Creature
     * @param initiative Initiative Number
     * @param image      Creature Image
     */
    NeutralCreature(String name, int maxHealth, int initiative, File image) {
        super(name, maxHealth, initiative, image);
    }

    /**
     * Border color for Neutral creatures.
     */
    public static Color border = Color.GRAY;
}
