package com.creatures;

import java.io.File;
import java.util.*;

public abstract class CreatureDaoImpl implements CreatureDao {
    private List<Creature> creatureInventory = new ArrayList<>();

    @Override
    public Creature createAllyCreature(String name, int maxHealth, int initiative, File image) {
        Creature new_creature = new AllyCreature(name, maxHealth, initiative, image);
        creatureInventory.add(new_creature);
        return new_creature;
    }

    @Override
    public Creature createNeutralCreature(String name, int maxHealth, int initiative, File image) {
        Creature new_creature = new NeutralCreature(name, maxHealth, initiative, image);
        creatureInventory.add(new_creature);
        return new_creature;
    }

    @Override
    public Creature createEnemyCreature(String name, int maxHealth, int initiative, File image) {
        Creature new_creature = new EnemyCreature(name, maxHealth, initiative, image);
        creatureInventory.add(new_creature);
        return new_creature;
    }

    @Override
    public void deleteCreature(Creature creature) {
        this.creatureInventory.remove(creature);
    }

    @Override
    public void sortByInitiative() {
        this.creatureInventory.sort(Comparator.comparing(creature -> creature.getInitiative()));
    }
}
