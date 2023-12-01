package com.creatures;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
/*
    Justification:
    Advancing to the next round is very important for our application as it
    makes users able to track whose turn it is as well as how long their conditions will last for.
    The advance turn button has different functionality depending on if the creatures group is the first/middle, or last.
    I wanted to make sure that conditions, a member of the creature class, decremented correctly when advancing a turn.
*/

public class AdvanceTurnPairwiseTest {
    private CreatureDaoImpl creatureDao;

    @BeforeEach
    void setUp() {
        creatureDao = new CreatureDaoImpl();
    }

    @Nested
    class Pairwise_Integration_AdvanceTurn {
        @Test
        public void testAdvanceTurn_EmptyGroupedByTurnCreatures() {
            // Setup for empty groupedByTurnCreatures
            // No need to add anything as the default state is empty

            // Action
            creatureDao.advanceTurn();

            // Verification
            assertTrue(creatureDao.getCurrentTurnCreatures().isEmpty());
            assertEquals(0, creatureDao.getRoundNumber());
        }

        @Test
        public void testAdvanceTurn_SingleGroupWithCondition() {
            // Setup for one group with a creature that has a condition
            Creature creatureWithCondition = creatureDao.createCreature("ALLY", "Ally1", 100, 10, new File("path/to/image"));
            // Assuming there's a method to set conditions on the creature
            creatureWithCondition.addCondition("Blinded", 10);
            int initialDuration = 10;

            creatureDao.sortByInitiative(); // This groups the creatures

            // Action
            creatureDao.advanceTurn(); // Set currentTurnCreatures to the group
            creatureDao.advanceTurn(); // Advance turn to trigger new round

            // Verification
            // Assuming there's a method to check if conditions were decremented
            assertEquals(initialDuration - 1, creatureWithCondition.getCurrentConditions().get(0).getDuration());
            assertEquals(1, creatureDao.getRoundNumber());
            assertEquals(creatureDao.getCreatureInventory(), creatureDao.getCurrentTurnCreatures());
        }

        @Test
        public void testAdvanceTurn_MultipleGroupsMiddleGroupNoCondition() {
            // Setup for multiple groups, currentTurnCreatures is a middle group, creatures have no conditions
            Creature creatureWithoutCondition = creatureDao.createCreature("NEUTRAL", "Neutral1", 100, 20, new File("path/to/image"));
            Creature anotherCreature = creatureDao.createCreature("ENEMY", "Enemy1", 100, 30, new File("path/to/image"));

            creatureDao.sortByInitiative(); // This groups the creatures

            // Set currentTurnCreatures to the middle group
            creatureDao.advanceTurn();
            creatureDao.advanceTurn();

            List<Creature> originalCurrentTurnCreatures = new ArrayList<>(creatureDao.getCurrentTurnCreatures());

            // Action
            creatureDao.advanceTurn();

            // Verification
            assertNotSame(originalCurrentTurnCreatures, creatureDao.getCurrentTurnCreatures());
            assertEquals(1, creatureDao.getRoundNumber());
            // Check the conditions of the creatures
            assertTrue(creatureWithoutCondition.getCurrentConditions().isEmpty());
        }

        @Test
        public void testAdvanceTurn_MultipleGroupsLastGroupWithCondition() {
            // Setup for multiple groups, last group with creatures having conditions
            Creature creatureWithCondition = creatureDao.createCreature("ALLY", "Ally1", 100, 10, new File("path/to/image"));
            creatureWithCondition.addCondition("Blinded", 10);

            Creature anotherCreatureWithCondition = creatureDao.createCreature("ENEMY", "Enemy1", 100, 20, new File("path/to/image"));
            anotherCreatureWithCondition.addCondition("Stunned", 5);

            creatureDao.sortByInitiative(); // This groups the creatures

            // Set currentTurnCreatures to the last group
            creatureDao.advanceTurn(); // First group
            creatureDao.advanceTurn(); // Second group (last group)

            int initialDurationAlly = creatureWithCondition.getCurrentConditions().get(0).getDuration();
            int initialDurationEnemy = anotherCreatureWithCondition.getCurrentConditions().get(0).getDuration();

            // Action
            creatureDao.advanceTurn(); // Should wrap around to the first group and increment round number

            // Verification
            assertEquals(1, creatureDao.getRoundNumber());
            assertEquals(initialDurationAlly - 1, creatureWithCondition.getCurrentConditions().get(0).getDuration());
            assertEquals(initialDurationEnemy - 1, anotherCreatureWithCondition.getCurrentConditions().get(0).getDuration());
        }
    }
}
