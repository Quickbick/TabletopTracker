package com.creatures;

import java.io.*;
import java.util.*;

public class CreatureDaoImpl implements CreatureDao {
    private int roundNumber = 0;
    private List<List<Creature>> groupedByTurnCreatures;
    private List<Creature> currentTurnCreatures;
    private List<Creature> creatureInventory = new ArrayList<>();

    @Override
    public Creature createCreature(String creatureType, String name, int health, int initiative, File image) {
        return switch (creatureType) {
            case "ALLY" -> createAllyCreature(name, health, initiative, image);
            case "NEUTRAL" -> createNeutralCreature(name, health, initiative, image);
            case "ENEMY" -> createEnemyCreature(name, health, initiative, image);
            default -> null;
        };
    }

    private Creature createAllyCreature(String name, int maxHealth, int initiative, File image) {
        Creature new_creature = new AllyCreature(name, maxHealth, initiative, image);
        this.creatureInventory.add(new_creature);
        return new_creature;
    }

    private Creature createNeutralCreature(String name, int maxHealth, int initiative, File image) {
        Creature new_creature = new NeutralCreature(name, maxHealth, initiative, image);
        this.creatureInventory.add(new_creature);
        return new_creature;
    }

    private Creature createEnemyCreature(String name, int maxHealth, int initiative, File image) {
        Creature new_creature = new EnemyCreature(name, maxHealth, initiative, image);
        this.creatureInventory.add(new_creature);
        return new_creature;
    }

    /**
     * Group Creatures based on affiliation. Any same affiliation creatures next to each other share a turn.
     */
    private List<List<Creature>> groupCreatures() {
        List<List<Creature>> groupedListList = new ArrayList<>();

        // Group the creatures based on their neighbors affiliation class.
        for (int i = 0; i < creatureInventory.size(); i++) {
            List<Creature> groupedList = new ArrayList<>();
            for (int j = 0; i+ j < creatureInventory.size(); j++) {
                if (creatureInventory.get(i).getClass() == creatureInventory.get(i+j).getClass()) {
                    groupedList.add(creatureInventory.get(i+j));
                    i = i+j;
                } else {
                    break;
                }
            }
            groupedListList.add(groupedList);
        }

        this.groupedByTurnCreatures = groupedListList;
        return groupedListList;
    }

    @Override
    public void deleteCreature(Creature creature) {
        this.creatureInventory.remove(creature);
        this.groupCreatures();
    }

    @Override
    public void sortByInitiative() {
        this.creatureInventory.sort(Comparator.comparing(Creature::getInitiative).reversed());
        this.groupCreatures();
    }

    /**
     * Advance Turn by updating the current turn creatures.
     * COULD ALSO UPDATE THE DURATION OF CONDITIONS HERE.
     */
    @Override
    public void advanceTurn() {
        if (this.groupedByTurnCreatures == null) {
            return;
        }
        if (this.currentTurnCreatures == null) {
            this.currentTurnCreatures = this.groupedByTurnCreatures.get(0);
        }
        // If the current turn creatures are the last to go, update round # and set current creatures to beginning.
        // Otherwise, just set current creatures to next in turn list.
        int ind = this.groupedByTurnCreatures.indexOf(this.currentTurnCreatures);
        if (ind == this.groupedByTurnCreatures.size() - 1) {
            ++this.roundNumber;
            this.currentTurnCreatures = this.groupedByTurnCreatures.get(0);
        } else {
            this.currentTurnCreatures = this.groupedByTurnCreatures.get(ind+1);
        }
    }

    @Override
    public List<Creature> getCreatureInventory() {
        return creatureInventory;
    }

    @Override
    public List<Creature> getCurrentTurnCreatures() {
        return this.currentTurnCreatures;
    }

    @Override
    public void saveCreatures(File file) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(creatureInventory);
        objectOutputStream.flush();
        objectOutputStream.close();
    }

    @Override
    public void loadCreatures(File file) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(file);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        List<Creature> e2 = (List<Creature>) objectInputStream.readObject();
        objectInputStream.close();
        this.creatureInventory = e2;
    }

    public int getRoundNumber() {
        return roundNumber;
    }
}
