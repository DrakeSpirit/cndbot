package me.drakespirit.cndbot;

import java.util.List;
import java.util.Random;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class DiceRoller {
    
    public static final int MAX_DICE = 50;
    private static final String regex = "([1-9]\\d*)?[dD][1-9]\\d*";
    
    private final Random random;
    
    public DiceRoller(Random random) {
        this.random = random;
    }
    
    public record RollResult(String roll, List<Integer> result) {
    }
    
    public RollResult performRoll(String die) {
        return new RollResult(format(die), roll(die));
    }
    
    public List<Integer> roll(String roll) {
        if (!roll.matches(regex)) {
            return emptyList();
        }
        String[] numbers = roll.split("[dD]");
        int numberOfDice = numbers[0].isEmpty() ? 1 : Integer.parseInt(numbers[0]);
        int maxDiceRoll = Integer.parseInt(numbers[1]);
        if (numberOfDice > MAX_DICE) {
            return emptyList();
        }
        return random.ints(numberOfDice, 1, maxDiceRoll + 1).boxed().collect(toList());
    }
    
    public String format(String roll) {
        if (!roll.matches(regex)) {
            return "nothing";
        }
        String lowercase = roll.toLowerCase();
        return lowercase.startsWith("d") ? "1" + lowercase : lowercase;
    }
    
}
