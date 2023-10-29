package com.creatures;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreatureDaoImplTest {

    @Test
    void createCreature() {
        final File mockFile = mock(File.class);
        final List<String> creatureTypes = Arrays.asList("ALLY", "ENEMY", "NEUTRAL");
        final String creatureName = "creatureName";
        final int maxHealth = 100;
        final int initiative = 10;

        for (String creatureType : creatureTypes) {
            final CreatureDao creatureDao = new CreatureDaoImpl();

            creatureDao.createCreature(creatureType, creatureName, maxHealth, initiative, mockFile);

            List<Creature> creatureInventory = creatureDao.getCreatureInventory();
            assertEquals(1, creatureInventory.size());
            Creature createdCreature = creatureInventory.get(0);
            assertEquals(creatureName, createdCreature.getName());
            assertEquals(maxHealth, createdCreature.getMaxHealth());
            assertEquals(initiative, createdCreature.getInitiative());
            assertEquals(mockFile, createdCreature.getImage());
        }
    }


    @Test
    void deleteCreature() {
        final File mockFile = mock(File.class);
        final CreatureDao creatureDao = new CreatureDaoImpl();
        creatureDao.createCreature("ALLY", "creatureName", 100, 10, mockFile);
        List<Creature> creatureInventory = creatureDao.getCreatureInventory();
        assertEquals(1, creatureInventory.size());
        Creature createdCreature = creatureInventory.get(0);
        creatureDao.deleteCreature(createdCreature);
        assertEquals(0, creatureInventory.size());
    }

    @Test
    void sortByInitiative() {
    }

    @Test
    void advanceTurn() {
    }

    @Test
    void getCreatureInventory() {
    }

    @Test
    void getCurrentTurnCreatures() {
    }

    @Test
    void saveCreatures() {
    }

    @Test
    void loadCreatures() {
    }

    @Test
    void getRoundNumber() {
    }
}