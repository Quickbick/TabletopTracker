package com.creatures;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface CreatureDao {

    public Creature createCreature(String creatureType, String name, int health, int initiative, File image);

    void deleteCreature(Creature creature);

    void sortByInitiative();

    void advanceTurn();

    List<Creature> getCreatureInventory();

    List<Creature> getCurrentTurnCreatures();

    void saveCreatures(File file) throws IOException;

    void loadCreatures(File file) throws IOException, ClassNotFoundException;
}
