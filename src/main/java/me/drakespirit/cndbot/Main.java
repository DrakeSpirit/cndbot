package me.drakespirit.cndbot;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Main {
    
    private static final String TOKEN_KEY = "TOKEN";
    
    public static void main(String[] args) {
        Properties discordProperties = loadDiscordProperties();
        GatewayDiscordClient client = loginToDiscord(discordProperties);
        if (client == null) {
            System.err.println("Unable to log in.");
            return;
        }
        
        client.getEventDispatcher().on(ReadyEvent.class)
                .subscribe(event -> {
                    User self = event.getSelf();
                    System.out.println("Logged in as " + self.getUsername() + "#" + self.getDiscriminator());
                });
        
        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> message.getContent().equalsIgnoreCase("!ping"))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage("Pong!"))
                .subscribe();
        
        client.onDisconnect().block();
    }
    
    private static GatewayDiscordClient loginToDiscord(Properties discordProperties) {
        if (!discordProperties.containsKey(TOKEN_KEY)) {
            System.err.println("""
                    Unable to find a TOKEN in the discord.properties file.
                    This file should contain the following text:
                    TOKEN=your_bot_token_from_the_discord_developer_portal
                    """);
        }
        String token = discordProperties.getProperty(TOKEN_KEY);
        
        return DiscordClientBuilder.create(token)
                .build()
                .login()
                .block();
    }
    
    private static Properties loadDiscordProperties() {
        Properties discordProperties = new Properties();
        
        try (FileInputStream discordPropertiesInputStream = new FileInputStream("discord.properties")) {
            discordProperties.load(discordPropertiesInputStream);
        } catch (IOException e) {
            System.err.println("""
                    Unable to find discord.properties file.
                    Please create a file with this name in the same folder as the executable.
                    This file should contain the following text:
                    TOKEN=your_bot_token_from_the_discord_developer_portal
                    """);
        }
        
        return discordProperties;
    }
    
}
