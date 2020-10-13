package me.drakespirit.cndbot;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Main {
    
    private static final String TOKEN_KEY = "TOKEN";
    
    public static void main(String[] args) {
        Properties discordProperties = loadDiscordProperties();
        
        if (!discordProperties.containsKey(TOKEN_KEY)) {
            System.err.println("""
                    Unable to find a TOKEN in the discord.properties file.
                    This file should contain the following text:
                    TOKEN=your_bot_token_from_the_discord_developer_portal
                    """);
        }
        String token = discordProperties.getProperty(TOKEN_KEY);
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
