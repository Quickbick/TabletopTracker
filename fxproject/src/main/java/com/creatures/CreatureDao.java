package com.creatures;

import java.io.File;

public interface CreatureDao {

    Creature createAllyCreature(String name, int maxHealth, int initiative, File image);

    Creature createNeutralCreature(String name, int maxHealth, int initiative, File image);

    Creature createEnemyCreature(String name, int maxHealth, int initiative, File image);

    void deleteCreature(Creature creature);

    void sortByInitiative();
}
