package com.cs422.fxproject.creatures;

public interface CreatureDao {
    Creature createAllyCreature(String name, int maxHealth, int maxBonusHealth, int initiative);

    Creature createAllyCreature(String name, int maxHealth, int initiative);

    Creature createNeutralCreature(String name, int maxHealth, int maxBonusHealth, int initiative);

    Creature createEnemyCreature(String name, int maxHealth, int maxBonusHealth, int initiative);

    Creature createNeutralCreature(String name, int maxHealth, int initiative);

    Creature createEnemyCreature(String name, int maxHealth, int initiative);

    void deleteCreature(Creature creature);

    void sortByInitiative();
}
