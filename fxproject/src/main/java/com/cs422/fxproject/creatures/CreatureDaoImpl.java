package com.cs422.fxproject.creatures;

import javafx.scene.image.Image;

import java.util.*;

public class CreatureDaoImpl implements CreatureDao {
    private List<Creature> creatureInventory = new ArrayList<>();

    @Override
    public Creature createAllyCreature(Image image, String name, int maxHealth, int maxBonusHealth, int initiative) {
        Creature new_creature = new AllyCreature(image, name, maxHealth, maxBonusHealth, initiative);
        creatureInventory.add(new_creature);
        return new_creature;
    }

    @Override
    public Creature createNeutralCreature(Image image, String name, int maxHealth, int maxBonusHealth, int initiative) {
        Creature new_creature = new NeutralCreature(image, name, maxHealth, maxBonusHealth, initiative);
        creatureInventory.add(new_creature);
        return new_creature;
    }

    @Override
    public Creature createEnemyCreature(Image image, String name, int maxHealth, int maxBonusHealth, int initiative) {
        Creature new_creature = new EnemyCreature(image, name, maxHealth, maxBonusHealth, initiative);
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
