package me.lightlord323dev.cursedlevels.api.handler;

import me.lightlord323dev.cursedlevels.Main;
import me.lightlord323dev.cursedlevels.api.skill.data.handler.SkillDataHandler;
import me.lightlord323dev.cursedlevels.handler.*;
import me.lightlord323dev.cursedlevels.handler.skill.*;
import me.lightlord323dev.cursedlevels.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Luda on 8/16/2020.
 */
public class HandlerRegistry {

    private List<Handler> handlers;

    // handlers
    private SkillGUIHandler skillGUIHandler;
    private MessageUtil messageUtil;
    private SkillDataHandler skillDataHandler;
    private CursedUserHandler cursedUserHandler;
    private SkillMainMenuHandler skillMainMenuHandler;

    public void loadHanders() {
        handlers = new ArrayList<>();
        // REGISTER HANDLERS
        handlers.addAll(Arrays.asList(
                new CursedGUIHandler(),
                skillGUIHandler = new SkillGUIHandler(),
                messageUtil = new MessageUtil(),
                skillDataHandler = new SkillDataHandler(),
                cursedUserHandler = new CursedUserHandler(),
                skillMainMenuHandler = new SkillMainMenuHandler(),
                new CursedUserHealthHandler(),
                new CustomEffectStrengthHandler(),

                // SKILL HANDLERS
                new MiningHandler(),
                new CombatHandler(),
                new ForagingHandler(),
                new FishingHandler(),
                new FarmingHandler(),
                new BlacksmithingHandler(),
                new DefenseHandler(),
                new CraftsmanshipHandler(),
                new RunecraftingHandler()
        ));
        // call onLoad method
        handlers.forEach(handler -> {
            handler.onLoad();
            if (handler instanceof Listener) {
                Bukkit.getPluginManager().registerEvents(((Listener) handler), Main.getInstance());
            }
        });
    }

    public void unloadHandlers() {
        handlers.forEach(Handler::onUnload);
    }

    public SkillGUIHandler getSkillGUIHandler() {
        return skillGUIHandler;
    }

    public MessageUtil getMessageUtil() {
        return messageUtil;
    }

    public SkillDataHandler getSkillDataHandler() {
        return skillDataHandler;
    }

    public CursedUserHandler getCursedUserHandler() {
        return cursedUserHandler;
    }

    public SkillMainMenuHandler getSkillMainMenuHandler() {
        return skillMainMenuHandler;
    }
}
