package com.creatures;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.internal.util.io.IOUtil.readLines;

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
        final File mockFile = mock(File.class);
        final CreatureDao creatureDao = new CreatureDaoImpl();
        creatureDao.createCreature("ALLY", "creatureName1", 100, 10, mockFile);
        creatureDao.createCreature("ENEMY", "creatureName2", 100, 100, mockFile);
        creatureDao.createCreature("NEUTRAL", "creatureName3", 100, 50, mockFile);
        assertEquals(3, creatureDao.getCreatureInventory().size());
        assertEquals("creatureName1", creatureDao.getCreatureInventory().get(0).getName());
        assertEquals("creatureName2", creatureDao.getCreatureInventory().get(1).getName());
        assertEquals("creatureName3", creatureDao.getCreatureInventory().get(2).getName());
        creatureDao.sortByInitiative();
        assertEquals("creatureName1", creatureDao.getCreatureInventory().get(2).getName());
        assertEquals("creatureName2", creatureDao.getCreatureInventory().get(0).getName());
        assertEquals("creatureName3", creatureDao.getCreatureInventory().get(1).getName());
    }

    @Test
    void advanceTurn() {
        String firstExpected = "creatureName1";
        String secondExpected = "creatureName2";
        String thirdExpected = "creatureName3";
        final File mockFile = mock(File.class);
        final CreatureDao creatureDao = new CreatureDaoImpl();
        creatureDao.createCreature("ALLY", firstExpected, 100, 100, mockFile);
        creatureDao.createCreature("ENEMY", secondExpected, 100, 50, mockFile);
        creatureDao.createCreature("NEUTRAL", thirdExpected, 100, 10, mockFile);
        creatureDao.sortByInitiative();
        creatureDao.advanceTurn();
        assertEquals(creatureDao.getCurrentTurnCreatures().get(0).getName(), firstExpected);
        creatureDao.advanceTurn();
        assertEquals(creatureDao.getCurrentTurnCreatures().get(0).getName(), secondExpected);
        creatureDao.advanceTurn();
        assertEquals(creatureDao.getCurrentTurnCreatures().get(0).getName(), thirdExpected);
        creatureDao.advanceTurn();
        assertEquals(creatureDao.getCurrentTurnCreatures().get(0).getName(), firstExpected);
    }

    @Test
    void advanceTurn_no_creatures() {
        final CreatureDao creatureDao = new CreatureDaoImpl();
        creatureDao.advanceTurn();
        assertTrue(creatureDao.getCurrentTurnCreatures().isEmpty());
    }

    @Test
    void saveCreatures() throws IOException {
        final File mockFile = mock(File.class);
        final CreatureDao creatureDao = new CreatureDaoImpl();
        creatureDao.createCreature("ALLY", "creatureName1", 100, 10, mockFile);
        creatureDao.createCreature("ENEMY", "creatureName2", 100, 100, mockFile);
        creatureDao.createCreature("NEUTRAL", "creatureName3", 100, 50, mockFile);
        final File file = new File("path");
        creatureDao.saveCreatures(file);
        assertTrue(file.length() > 0);
    }

    @Test
    void loadCreatures() throws IOException, ClassNotFoundException {
        final CreatureDao creatureDao = new CreatureDaoImpl();
        CreatureDao loadedDao = new CreatureDaoImpl();
        final File file = new File("path");
        creatureDao.saveCreatures(file);
        loadedDao.loadCreatures(file);
        assertEquals(creatureDao.getCreatureInventory(), loadedDao.getCreatureInventory());
    }

    @Test
    void getRoundNumber() {
        final CreatureDao creatureDao = new CreatureDaoImpl();
        assertEquals(0, creatureDao.getRoundNumber());
    }
}