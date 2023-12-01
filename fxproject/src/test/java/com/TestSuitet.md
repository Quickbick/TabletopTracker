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
    * Create new CreatureDao
    * Create 3 creatures with different initiative values
    * Expected: 3 creatures in Dao inventory
    * Expected: Creatures sorted in order of creation
    * Sort Creatures by Initiative
    * Expected: Creatures sorted in initiative order
* C10 Advance Turn
    * Create new creatureDao
    * Create 3 creatures with different initiatives
    * Sort creatures by initiative
    * Advance Turn
    * Expected: Current Turn Creature = High Initiative Creature
    * Advance Turn
    * Expected: Current Turn Creature = Medium Initiative Creature
    * Advance Turn
    * Expected: Current Turn Creature = Low Initiative Creature
    * Advance Turn
    * Expected: Current Turn Creature = High Initiative Creature
* C11 Advance Turn No Creatures
    * Creature new creatureDao
    * Advance turn
    * Expected: Current Turn Creatures is Empty
* C12 Save Creatures
    * Create new creatureDao
    * Create 3 creatures
    * Create empty file
    * Save to file
    * Expected: File length > 0
* C13 Load Creatures
    * Create two CreatureDao
    * Create new file slot
    * Save Dao 1 to file
    * Load file into Dao 2
    * Expected: Creature inventory of both Dao are equal
* C14 Get Round Number
    * Create new creature Dao
    * Expected: Round number = 0

## S3 Creature Tests
* C15 Add Health to Full
    * Create new creature
    * Set Creature health to 20
    * Add 15 Health
    * Expected: Creature Health = Full Health
* C16 Add Health Not Full
    * Create new creature
    * Set Creature health to 20
    * Add 3 health
    * Expected: Health = 23
* C17 Remove Health With Auto Crits
    * Create a Creature
    * Create a ConditionDao with crit conditions active
    * Spy creature to use crit conditions
    * Remove 10 health from creature
    * Expected: Health = 5
* C18 Remove Health With More Bonus Health
    * Create new creature
    * Set bonus health to 15
    * remove 10 health
    * Expected: Health = 25
    * Expected: Bonus Health = 5
* C19 Remove HeRalth Kill
    * Create new creature
    * Remove 30 health with crit
    * Expected: Health = 0
    * Expected: Conditions contains Unconscious
* C20 Remove Health Just Paralyzed
    * Create Creature
    * Create ConditionDao with Paralyzed
    * Spy creature to use special conditionDao
    * Remove 10 health with no crit
    * Expected: Health = 5
* C21 Remove Health Just Incapacitated
    * Create Creature
    * Create ConditionDao with Incapacitated
    * Spy creature to use special conditionDao
    * Remove 10 health with no crit
    * Expected: Health = 5
* C22 Remove Health Just Unconscious
    * Create Creature
    * Create ConditionDao with Unconscious
    * Spy creature to use special conditionDao
    * Remove 10 health with no crit
    * Expected: Health = 5
* C23 Add Bonus Health
    * Create Creature
    * Add 5 Bonus Health
    * Expected: Bonus Health = 5
* C24 Add Bonus Health No Change
    * Create Creature
    * Set Bonus Health to 5
    * Add 3 Bonus Health
    * Expected: Bonus Health  = 5
* C25 Add Condition
    * Create New Creature
    * Add Condition
    * Expected: Condition is there
* C26 Add Condition More Than Once
    * Create Creature
    * Add Condition
    * Add Same Condition Again
    * Expected: Current Conditions Size = 1
* C27 Remove Condition
    * Create Creature
    * Add Condition to Creature
    * Get Initial Condition Count
    * Remove Condition
    * Get After Condition Count
    * Expected: Condition Count = Initial Count - 1
* C28 Decrement Conditions
    * Create New Creature
    * Add Condition with Duration 2
    * Add Condition with Duration 3
    * Collect Condition Durations
    * Decrement Conditions
    * Collect New Condition Durations
    * Expected: Each New Condition Duration = Condition Duration - 1
* C29 Get Name
    * Create Creature with Specific Name
    * Get Name
    * Expected: Name = Specific Name
* C30 Get Image
    * Create Creature with Specific Image
    * Get Image
    * Expected: Image = Specific Image
* C31 Get Max Health
    * Create Creature with Specific Health
    * Get Max Health
    * Expected: Max Health = Specific Max Health
* C32 Get Current Health
    * Create Creature
    * Set Health to specific non-max value
    * Get Current Health
    * Expected: Current Health = Specific Value
* C33 Get Bonus Health
    * Create Creature
    * Set Creature Bonus Health to value
    * Get Bonus Health
    * Expected: Bonus Health = Value
* C34 Get Initiative
    * Create Creature with Specific Initiative
    * Get Initiative
    * Expected: Initiative = Specific Initiative
* C35 Get Current Conditions
    * Create Creature
    * Add 2 Unique Conditions
    * Expected: Current Condition Count  = 2
    * Expected: Current Conditions Contains Condition 1
    * Expected: Current Conditions Contains Condition 2