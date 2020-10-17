package me.drakespirit.cndbot.discord.commands;

import discord4j.core.object.entity.Message;

import java.util.List;

public class PingCommand implements Command {
    
    @Override
    public List<String> getSupportedPrefixes() {
        return List.of("!ping");
    }
    
    @Override
    public String processCommand(Message command) {
        return "Pong!";
    }
    
}
