package com.creatures;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class CreateCreatureNeighborhood {
    @Mock
    private File mockImage;

    private CreatureDaoImpl creatureDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        creatureDao = new CreatureDaoImpl();
    }

    @Test
    void testCreateAllyCreature() {
        String name = "Ally1";
        int health = 100;
        int initiative = 10;

        creatureDao.createCreature("ALLY", name, health, initiative, mockImage);
        assertEquals(1, creatureDao.getCreatureInventory().size());
        assertEquals(AllyCreature.class, creatureDao.getCreatureInventory().get(0).getClass());
        assertEquals(name, creatureDao.getCreatureInventory().get(0).getName());
        assertEquals(health, creatureDao.getCreatureInventory().get(0).getCurrentHealth());
        assertEquals(initiative, creatureDao.getCreatureInventory().get(0).getInitiative());
    }

    @Test
    void testCreateNeutralCreature() {
        String name = "Neutral1";
        int health = 80;
        int initiative = 5;

        creatureDao.createCreature("NEUTRAL", name, health, initiative, mockImage);
        assertEquals(1, creatureDao.getCreatureInventory().size());
        assertEquals(NeutralCreature.class, creatureDao.getCreatureInventory().get(0).getClass());
        assertEquals(name, creatureDao.getCreatureInventory().get(0).getName());
        assertEquals(health, creatureDao.getCreatureInventory().get(0).getCurrentHealth());
        assertEquals(initiative, creatureDao.getCreatureInventory().get(0).getInitiative());
    }

    @Test
    void testCreateEnemyCreature() {
        String name = "Enemy1";
        int health = 120;
        int initiative = 15;

        creatureDao.createCreature("ENEMY", name, health, initiative, mockImage);
        assertEquals(1, creatureDao.getCreatureInventory().size());
        assertEquals(EnemyCreature.class, creatureDao.getCreatureInventory().get(0).getClass());
        assertEquals(name, creatureDao.getCreatureInventory().get(0).getName());
        assertEquals(health, creatureDao.getCreatureInventory().get(0).getCurrentHealth());
        assertEquals(initiative, creatureDao.getCreatureInventory().get(0).getInitiative());
    }

}

