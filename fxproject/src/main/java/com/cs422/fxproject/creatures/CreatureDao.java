package com.cs422.fxproject.creatures;

import javafx.scene.image.Image;

public interface CreatureDao {
    Creature createAllyCreature(Image image, String name, int maxHealth, int maxBonusHealth, int initiative);
    Creature createNeutralCreature(Image image, String name, int maxHealth, int maxBonusHealth, int initiative);
    Creature createEnemyCreature(Image image, String name, int maxHealth, int maxBonusHealth, int initiative);

    void deleteCreature(Creature creature);
    void sortByInitiative();
}
