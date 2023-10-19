package com.condition_manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConditionDaoImpl implements ConditionDao, Serializable {
    private static final long serialVersionUID = 1L;

    private final List<Condition> currentConditions = new ArrayList<>();

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
            case "Charmed" -> new Charmed(duration);
            case "Deafened" -> new Deafened(duration);
            case "Frightened" -> new Frightened(duration);
            case "Grappled" -> new Grappled(duration);
            case "Incapacitated" -> new Incapacitated(duration);
            case "Invisible" -> new Invisible(duration);
            case "Paralyzed" -> new Paralyzed(duration);
            case "Petrified" -> new Petrified(duration);
            case "Poisoned" -> new Poisoned(duration);
            case "Prone" -> new Prone(duration);
            case "Restrained" -> new Restrained(duration);
            case "Stunned" -> new Stunned(duration);
            case "Unconscious" -> new Unconscious(duration);
            default -> throw new IllegalStateException("Unexpected value: " + conditionType);
        };
    }

    @Override
    public List<Condition> getCurrentConditions() {
        return currentConditions;
    }

    @Override
    public List<String> getAvailableConditions() {
        return allConditions.stream()
                .filter(condition -> !currentConditions.stream()
                        .map(Condition::toString)
                        .toList()
                        .contains(condition))
                .collect(Collectors.toList());
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
