package me.lightlord323dev.cursedlevels;

import me.lightlord323dev.cursedlevels.api.handler.HandlerRegistry;
import me.lightlord323dev.cursedlevels.cmd.DevTestCmd;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Luda on 8/26/2020.
 */
public class Main extends JavaPlugin {

    // instance
    private static Main instance;

    // handler registry
    private HandlerRegistry handlerRegistry;

    @Override
    public void onEnable() {

        instance = this;

        // files
        initFiles();

        // registry
        handlerRegistry = new HandlerRegistry();
        handlerRegistry.loadHanders();

        getCommand("devtest").setExecutor(new DevTestCmd());
    }

    @Override
    public void onDisable() {
        handlerRegistry.unloadHandlers();
    }

    private void initFiles() {
        saveResource("messages.yml", false);
    }

    public static Main getInstance() {
        return instance;
    }

    public HandlerRegistry getHandlerRegistry() {
        return handlerRegistry;
    }
}
