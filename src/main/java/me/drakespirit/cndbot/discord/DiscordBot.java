package me.drakespirit.cndbot.discord;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import me.drakespirit.cndbot.discord.commands.Command;
import me.drakespirit.cndbot.discord.commands.DiceCommand;
import me.drakespirit.cndbot.discord.commands.PingCommand;
import reactor.core.publisher.Flux;

import java.util.List;

public class DiscordBot {
    
    private final String token;
    private List<Command> commands;
    
    public DiscordBot(String token) {
        this.token = token;
    }
    
    public void start() {
        GatewayDiscordClient client = login();
        
        commands = List.of(new DiceCommand(), new PingCommand());
        
        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .flatMap(message -> Flux.fromIterable(commands)
                        .filter(command -> command.getSupportedPrefixes().stream().anyMatch(prefix -> messageStartsWithIgnoreCase(message, prefix)))
                        .map(command -> logCommandProcessing(message, command))
                        .flatMap(command -> message.getChannel().flatMap(channel -> channel.createMessage(command.processCommand(message)))))
                .subscribe();
        
        client.onDisconnect().block();
    }
    
    private Command logCommandProcessing(Message message, Command command) {
        System.out.println(command.getClass().getSimpleName() + " processed command \"" + message.getContent() + "\" sent by " + message.getAuthor().map(User::getTag).orElse("<unknown user>"));
        return command;
    }
    
    private boolean messageStartsWithIgnoreCase(Message message, String prefix) {
        return message.getContent().substring(0, prefix.length()).equalsIgnoreCase(prefix);
    }
    
    private GatewayDiscordClient login() {
        GatewayDiscordClient client = DiscordClientBuilder.create(token)
                .build()
                .login()
                .block();
        
        if (client == null) {
            throw new IllegalStateException("Unable to log in to discord.");
        }
        
        client.getEventDispatcher().on(ReadyEvent.class)
                .subscribe(event -> {
                    User self = event.getSelf();
                    System.out.println("Logged in as " + self.getUsername() + "#" + self.getDiscriminator());
                });
        
        return client;
    }
    
}
