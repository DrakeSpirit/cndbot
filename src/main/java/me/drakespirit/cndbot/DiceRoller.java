package me.drakespirit.cndbot;

import java.util.List;
import java.util.Random;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class DiceRoller {
    
    public static final int MAX_DICE = 50;
    
    private final Random random;
    
    public DiceRoller(Random random) {
        this.random = random;
    }
    
    public List<Integer> roll(String roll) {
        if (!roll.matches("[1-9]\\d*?d[1-9]\\d*")) {
            return emptyList();
        }
        String[] numbers = roll.split("d");
        int numberOfDice = Integer.parseInt(numbers[0]);
        int maxDiceRoll = Integer.parseInt(numbers[1]);
        if (numberOfDice > MAX_DICE) {
            return emptyList();
        }
        return random.ints(numberOfDice, 1, maxDiceRoll + 1).boxed().collect(toList());
    }
    
}
