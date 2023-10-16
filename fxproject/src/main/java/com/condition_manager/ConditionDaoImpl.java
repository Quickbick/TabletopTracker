package com.condition_manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConditionDaoImpl implements ConditionDao {
    private List<Condition> currentConditions = new ArrayList<>();

    private final List<String> allConditions = Arrays.asList(
            "Blinded",
            "Charmed",
            "Deafened",
            "Frightened",
            "Grappled",
            "Incapacitated",
            "Invisible",
            "Paralyzed",
            "Petrified",
            "Poisoned",
            "Prone",
            "Restrained",
            "Stunned",
            "Unconscious"
    );

    @Override
    public Condition createCondition(String conditionType, int duration) {
        return switch (conditionType) {
            case "Blinded" -> new Blinded(duration);
            case "Charmed" -> throw new IllegalStateException("Unexpected value: " + conditionType);
            case "Deafened" -> throw new IllegalStateException("Unexpected value: " + conditionType);
            case "Frightened" -> throw new IllegalStateException("Unexpected value: " + conditionType);
            case "Grappled" -> throw new IllegalStateException("Unexpected value: " + conditionType);
            case "Incapacitated" -> throw new IllegalStateException("Unexpected value: " + conditionType);
            case "Invisible" -> throw new IllegalStateException("Unexpected value: " + conditionType);
            case "Paralyzed" -> throw new IllegalStateException("Unexpected value: " + conditionType);
            case "Petrified" -> throw new IllegalStateException("Unexpected value: " + conditionType);
            case "Poisoned" -> throw new IllegalStateException("Unexpected value: " + conditionType);
            case "Prone" -> throw new IllegalStateException("Unexpected value: " + conditionType);
            case "Restrained" -> throw new IllegalStateException("Unexpected value: " + conditionType);
            case "Stunned" -> throw new IllegalStateException("Unexpected value: " + conditionType);
            case "Unconscious" -> throw new IllegalStateException("Unexpected value: " + conditionType);
            default -> throw new IllegalStateException("Unexpected value: " + conditionType);
        };
    }

    @Override
    public List<Condition> getCurrentConditions() {
        return currentConditions;
    }

    @Override
    public List<String> getAvailableConditions() {
        List<String> availableConditions = allConditions.stream()
                .filter(condition -> !currentConditions.stream()
                        .map(Condition::toString)
                        .collect(Collectors.toList())
                        .contains(condition))
                .collect(Collectors.toList());
        return availableConditions;
    }

    @Override
    public void addCurrentCondition(Condition condition) {
        this.currentConditions.add(condition);
    }


    @Override
    public void removeCurrentCondition(Condition condition) {
        this.currentConditions.remove(condition);
    }

    @Override
    public void decreaseAllConditionDurations() {
        for (Condition currCondition: this.currentConditions) {
            if (currCondition.getDuration() == 1) {
                this.removeCurrentCondition(currCondition);
                break;
            } else {
                currCondition.decrementDuration();
            }
        }
    }
}
