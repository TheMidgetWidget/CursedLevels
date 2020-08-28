package me.lightlord323dev.cursedlevels.util;

import me.lightlord323dev.cursedlevels.Main;
import me.lightlord323dev.cursedlevels.api.handler.Handler;
import me.lightlord323dev.cursedlevels.util.file.AbstractFile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

/**
 * Created by Luda on 7/16/2020.
 */
public class MessageUtil implements Handler {

    private HashMap<String, String> messages;

    @Override
    public void onLoad() {
        this.messages = new HashMap<>();
        FileConfiguration config = new AbstractFile(Main.getInstance(), "messages.yml", true).getConfig();
        config.getConfigurationSection("").getKeys(false).forEach(section -> config.getConfigurationSection(section).getKeys(false).forEach(identifier -> messages.put(section + "." + identifier, ChatColor.translateAlternateColorCodes('&', config.getString(section + "." + identifier)))));
    }

    @Override
    public void onUnload() {

    }

    public String getMessage(String identifier) {
        if (this.messages.containsKey(identifier))
            return this.messages.get(identifier);
        return null;
    }

    private static final String PREFIX = ChatColor.GRAY + "[" + ChatColor.GOLD + "Cursed Levels" + ChatColor.GRAY + "] ";

    public static void error(CommandSender target, String message) {
        target.sendMessage(PREFIX + ChatColor.RED + message);
    }

    public static void info(CommandSender target, String message) {
        target.sendMessage(PREFIX + ChatColor.RESET + message);
    }

    public static void success(CommandSender target, String message) {
        target.sendMessage(PREFIX + ChatColor.GREEN + message);
    }

    public static void log(String message) {
        Bukkit.getLogger().info(PREFIX + ChatColor.RESET + message);
    }

}
