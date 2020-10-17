package me.drakespirit.cndbot.discord.commands;

import discord4j.core.object.entity.Message;

import java.util.List;

public interface Command {
    
    List<String> getSupportedPrefixes();
    
    String processCommand(Message command);
    
}
