package com.creatures;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SortByInitiativePairwiseTest {

    /* These tests are for the pairwise integration of CreatureDaoImpl.sortByInitiative and CreatureDaoImpl.groupCreatures*/

    private  CreatureDaoImpl creatureDao;
    @Test
    void sortByInitiative_no_groups() {
        creatureDao = new CreatureDaoImpl();
        int expected = 1;
        Creature ally = new AllyCreature("Ally", 25, 3, new File("./src/main/resources/com/cs422/fxproject/Default_Image.png"));
        Creature neutral = new NeutralCreature("Neutral", 25, 3, new File("./src/main/resources/com/cs422/fxproject/Default_Image.png"));
        Creature enemy = new EnemyCreature("Enemy", 25, 3, new File("./src/main/resources/com/cs422/fxproject/Default_Image.png"));
        creatureDao.getCreatureInventory().add(ally);
        creatureDao.getCreatureInventory().add(enemy);
        creatureDao.getCreatureInventory().add(neutral);
        creatureDao.sortByInitiative();
        //check each group only has one creature
        creatureDao.advanceTurn();
        List<Creature> current = creatureDao.getCurrentTurnCreatures();
        assertEquals(expected, current.size());
        creatureDao.advanceTurn();
        current = creatureDao.getCurrentTurnCreatures();
        assertEquals(expected, current.size());
        creatureDao.advanceTurn();
        current = creatureDao.getCurrentTurnCreatures();
        assertEquals(expected, current.size());
    }

    @Test
    void sortByInitiative_groups() {
        creatureDao = new CreatureDaoImpl();
        int expected = 3;
        Creature ally1 = new AllyCreature("Ally1", 25, 1, new File("./src/main/resources/com/cs422/fxproject/Default_Image.png"));
        Creature ally2 = new AllyCreature("Ally2", 25, 2, new File("./src/main/resources/com/cs422/fxproject/Default_Image.png"));
        Creature ally3 = new AllyCreature("Ally3", 25, 3, new File("./src/main/resources/com/cs422/fxproject/Default_Image.png"));
        creatureDao.getCreatureInventory().add(ally1);
        creatureDao.getCreatureInventory().add(ally2);
        creatureDao.getCreatureInventory().add(ally3);
        creatureDao.sortByInitiative();
        //check group has three creatures
        creatureDao.advanceTurn();
        List<Creature> current = creatureDao.getCurrentTurnCreatures();
        assertEquals(expected, current.size());
    }
}