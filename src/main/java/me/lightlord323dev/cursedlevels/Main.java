package me.lightlord323dev.cursedlevels;

import me.lightlord323dev.cursedlevels.api.handler.HandlerRegistry;
import me.lightlord323dev.cursedlevels.cmd.DevTestCmd;
import me.lightlord323dev.cursedlevels.util.file.AbstractFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by Luda on 8/26/2020.
 */
public class Main extends JavaPlugin {

    // instance
    private static Main instance;

    // handler registry
    private HandlerRegistry handlerRegistry;

    // executor service
    private ScheduledExecutorService executorService;

    // settings file
    private AbstractFile settingsFile;

    @Override
    public void onEnable() {

        instance = this;

        this.executorService = Executors.newScheduledThreadPool(4);

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
        saveResource("settings.yml", false);
        saveResource("skills_gui.yml", false);

        this.settingsFile = new AbstractFile(this, "settings.yml", true);
    }

    public static Main getInstance() {
        return instance;
    }

    public HandlerRegistry getHandlerRegistry() {
        return handlerRegistry;
    }

    public ScheduledExecutorService getExecutorService() {
        return executorService;
    }

    public AbstractFile getSettingsFile() {
        return settingsFile;
    }
}
