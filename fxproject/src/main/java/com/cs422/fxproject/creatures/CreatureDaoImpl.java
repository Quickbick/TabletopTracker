package com.cs422.fxproject.creatures;

import java.io.*;
import java.util.*;

public class CreatureDaoImpl implements CreatureDao {
    private final List<Creature> creatureInventory = new ArrayList<>();

    @Override
    public void createCreature(String creatureType, String name, int health, int initiative, File image) {
        switch (creatureType) {
            case "ALLY" -> createAllyCreature(name, health, initiative, image);
            case "NEUTRAL" -> createNeutralCreature(name, health, initiative, image);
            case "ENEMY" -> createEnemyCreature(name, health, initiative, image);
            default -> {
            }
        }
    }

    private Creature createAllyCreature(String name, int maxHealth, int initiative, File image) {
        Creature new_creature = new AllyCreature(name, maxHealth, initiative, image);
        creatureInventory.add(new_creature);
        return new_creature;
    }

    private Creature createNeutralCreature(String name, int maxHealth, int initiative, File image) {
        Creature new_creature = new NeutralCreature(name, maxHealth, initiative, image);
        creatureInventory.add(new_creature);
        return new_creature;
    }

    private Creature createEnemyCreature(String name, int maxHealth, int initiative, File image) {
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

    @Override
    public List<Creature> getCreatureInventory() {
        return creatureInventory;
    }

    @Override
    public void saveCreatures() throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream("yourfile2.txt");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(creatureInventory);
        objectOutputStream.flush();
        objectOutputStream.close();
    }

    @Override
    public void loadCreatures(File file) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream
                = new FileInputStream(file);
        ObjectInputStream objectInputStream
                = new ObjectInputStream(fileInputStream);
        Creature e2 = (Creature) objectInputStream.readObject();
        objectInputStream.close();
        int a = 2;
    }
}
