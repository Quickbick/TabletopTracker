package com.cs422.fxproject.creatures;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class EnemyCreature extends Creature{
    /**
     * Creates a new Creature with image, name, health, and initiative.
     *
     * @param image          Image of Creature
     * @param name           Name of Creature
     * @param maxHealth      Max Health of Creature
     * @param maxBonusHealth Max Bonus Health of Creature
     * @param initiative     Initiative Number
     */
    EnemyCreature(Image image, String name, int maxHealth, int maxBonusHealth, int initiative) {
        super(image, name, maxHealth, maxBonusHealth, initiative);
    }

    /**
     * Border color for Enemy creatures.
     */
    public static Color border = Color.RED;
}
