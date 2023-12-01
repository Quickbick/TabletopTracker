# Tabletop Tracker Tests


## S1 ConditionDaoImpl Tests
* C1 Create Condition
    * Create empty conditionDao, list of condition types, and list of matching durations
    * Attempt to Create each condition with the matching duration
    * Expected: Unknown Condition or Condition Not Null
* C2 Add Current Condition
    * Create new conditionDao
    * Add a condition
    * Expected: Current Condition List Size = 1
    * Add another condition
    * Expected: Current Condition List Size = 2
* C3 Remove Current Condition
    * Create new conditionDao
    * Add a condition
    * Expected: Current Condition List Size = 1
    * Remove a Condition
    * Expected: Current Condition List Size = 0
* C4 Add And Remove Current Condition
    * Create new conditionDao
    * Add a condition
    * Expected: Current Condition List Size = 1
    * Remove a Condition
    * Expected: Current Condition List Size = 0
* C5 Decrease All Condition Durations Remove Condition When Duration Is One.
    * Create new ConditionDao
    * Add condition with duration of 1
    * Add condition with duration > 1
    * Decrease all condition durations
    * Expected: Current Condition List Size = 1
    * Expected: Higher Duration Condition Still in List

## S2 CreatureDaoImpl Tests
* C6 Create Creature
    * Create values needed for creature creation
    * Create list of all creature types
    * For each creature type create new creatureDao and add creature
    * Expected: One creature in each inventory
    * Expected: Creature data matches inputted fields
* C7 Delete Creature
    * Create new CreatureDao
    * Create creature in Dao
    * Expected: Creature Inventory Size = 1
    * Delete Creature from Dao
    * Expected: Creature Inventory Size = 0
* C9 Sort By Initiative
    * 