package me.drakespirit.cndbot.discord.commands;

import discord4j.core.object.entity.Message;
import me.drakespirit.cndbot.DiceRoller;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class DiceCommand implements Command {
    
    private final DiceRoller diceRoller;
    
    public DiceCommand() {
        this.diceRoller = new DiceRoller(new Random());
    }
    
    @Override
    public List<String> getSupportedPrefixes() {
        return List.of("!roll");
    }
    
    @Override
    public String processCommand(Message command) {
        return generateRollResponse(command.getContent());
    }
    
    private String generateRollResponse(String command) {
        List<DiceRoller.RollResult> rollResults = rollDice(command);
        
        String response = describeRollResults(rollResults);
        
        long totalDiceRolled = rollResults.stream().mapToLong(results -> results.result().size()).sum();
        if (totalDiceRolled > 1) {
            int total = sumAllRolls(rollResults);
            response += "\nThe sum of all rolls is " + total + ".";
        } else if (totalDiceRolled == 0) {
            response = """
                    There's nothing I can roll in that message.
                    Specify what dice you want me to roll by saying the number of dice, followed by a d, followed by the number of sides on the die.
                    For example: "!roll 3d20" rolls 3 20-sided dice.
                    """;
        }
        
        return response;
    }
    
    private int sumAllRolls(List<DiceRoller.RollResult> rollResults) {
        return rollResults.stream()
                .flatMap(results -> results.result().stream())
                .mapToInt(Integer::intValue).sum();
    }
    
    private String describeRollResults(List<DiceRoller.RollResult> rollResults) {
        return rollResults.stream()
                .filter(results -> !results.result().isEmpty())
                .map(results -> {
                    String rolls = results.result().stream().map(Object::toString).collect(Collectors.joining(", "));
                    return "Rolling " + results.roll() + " resulted in " + rolls + ".";
                })
                .collect(joining("\n"));
    }
    
    private List<DiceRoller.RollResult> rollDice(String command) {
        return stream(command.split(" "))
                .skip(1)
                .map(diceRoller::performRoll)
                .collect(toList());
    }
    
}
