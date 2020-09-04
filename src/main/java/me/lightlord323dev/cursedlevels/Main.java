package me.lightlord323dev.cursedlevels;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import me.brook.embercore.EmberPlugin;
import me.lightlord323dev.cursedlevels.api.handler.HandlerRegistry;
import me.lightlord323dev.cursedlevels.api.user.CursedUser;
import me.lightlord323dev.cursedlevels.cmd.LevelsCommand;
import me.lightlord323dev.cursedlevels.cmd.SkillsCommand;
import me.lightlord323dev.cursedlevels.placeholderapi.CursedLevelsExpansion;
import me.lightlord323dev.cursedlevels.util.file.AbstractFile;
import n3kas.ae.api.AEAPI;
import n3kas.ae.api.AEnchantmentType;
import n3kas.ae.api.CustomEffect;
import n3kas.ae.api.EffectActivationEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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

    // worldguard
    private WorldGuardPlugin worldGuardPlugin;

    // embercore
    private EmberPlugin emberPlugin;

    @Override
    public void onEnable() {
        instance = this;

        this.executorService = Executors.newScheduledThreadPool(4);

        // world guard
        if (getServer().getPluginManager().getPlugin("WorldGuard") != null)
            worldGuardPlugin = WorldGuardPlugin.inst();

        // embercore
        if (Bukkit.getPluginManager().getPlugin("EmberCore") != null)
            emberPlugin = (EmberPlugin) Bukkit.getPluginManager().getPlugin("EmberCore");

        // files
        initFiles();

        // registry
        handlerRegistry = new HandlerRegistry();
        handlerRegistry.loadHanders();

        // AE effects
        advancedEnchantmentsEffects();

        getCommand("skills").setExecutor(new SkillsCommand());
        getCommand("levels").setExecutor(new LevelsCommand());

        // registering placeholders
        new CursedLevelsExpansion(this).register();
    }

    @Override
    public void onDisable() {
        handlerRegistry.unloadHandlers();
    }

    private void initFiles() {
        saveResource("messages.yml", false);
        saveResource("settings.yml", false);
        saveResource("skills_settings.yml", false);

        this.settingsFile = new AbstractFile(this, "settings.yml", true);
    }

    private void advancedEnchantmentsEffects() {

        // HEALTH EFFECTS
        AEAPI.registerEffect(this, "INCREASE_HEALTH", new CustomEffect() {
            @Override
            public boolean onEffectActivation(EffectActivationEvent e) {
                if (!e.getEnchantmentType().equals(AEnchantmentType.EFFECT_STATIC))
                    return false;
                if (e.getArgs().length == 0)
                    return false;
                if (!(e.getMainEntity() instanceof Player))
                    return false;

                int health = Integer.parseInt(e.getArgs()[0]);
                CursedUser user = getHandlerRegistry().getCursedUserHandler().getCursedUser(e.getMainEntity().getUniqueId());

                if (e.isRemoval()) {
                    user.setMaxHealth(user.getMaxHealth() - health);
                } else {
                    user.setMaxHealth(user.getMaxHealth() + health);
                }
                return true;
            }
        });
        AEAPI.registerEffect(this, "DECREASE_HEALTH", new CustomEffect() {
            @Override
            public boolean onEffectActivation(EffectActivationEvent e) {
                if (!e.getEnchantmentType().equals(AEnchantmentType.EFFECT_STATIC))
                    return false;
                if (e.getArgs().length == 0)
                    return false;
                if (!(e.getMainEntity() instanceof Player))
                    return false;

                int health = Integer.parseInt(e.getArgs()[0]);
                CursedUser user = getHandlerRegistry().getCursedUserHandler().getCursedUser(e.getMainEntity().getUniqueId());

                if (e.isRemoval()) {
                    user.setMaxHealth(user.getMaxHealth() + health);
                } else {
                    user.setMaxHealth(user.getMaxHealth() - health);
                }
                return true;
            }
        });

        // DEFENSE EFFECTS
        AEAPI.registerEffect(this, "INCREASE_DEFENSE", new CustomEffect() {
            @Override
            public boolean onEffectActivation(EffectActivationEvent e) {
                if (!e.getEnchantmentType().equals(AEnchantmentType.EFFECT_STATIC))
                    return false;
                if (e.getArgs().length == 0)
                    return false;
                if (!(e.getMainEntity() instanceof Player))
                    return false;

                int defense = Integer.parseInt(e.getArgs()[0]);
                CursedUser user = getHandlerRegistry().getCursedUserHandler().getCursedUser(e.getMainEntity().getUniqueId());

                if (e.isRemoval()) {
                    user.setDefense(user.getDefense() - defense);
                } else {
                    user.setDefense(user.getDefense() + defense);
                }
                return true;
            }
        });
        AEAPI.registerEffect(this, "DECREASE_DEFENSE", new CustomEffect() {
            @Override
            public boolean onEffectActivation(EffectActivationEvent e) {
                if (!e.getEnchantmentType().equals(AEnchantmentType.EFFECT_STATIC))
                    return false;
                if (e.getArgs().length == 0)
                    return false;
                if (!(e.getMainEntity() instanceof Player))
                    return false;

                int defense = Integer.parseInt(e.getArgs()[0]);
                CursedUser user = getHandlerRegistry().getCursedUserHandler().getCursedUser(e.getMainEntity().getUniqueId());

                if (e.isRemoval()) {
                    user.setDefense(user.getDefense() + defense);
                } else {
                    user.setDefense(user.getDefense() - defense);
                }
                return true;
            }
        });

        // MANA EFFECTS
        AEAPI.registerEffect(this, "INCREASE_MANA", new CustomEffect() {
            @Override
            public boolean onEffectActivation(EffectActivationEvent e) {
                if (!e.getEnchantmentType().equals(AEnchantmentType.EFFECT_STATIC))
                    return false;
                if (e.getArgs().length == 0)
                    return false;
                if (!(e.getMainEntity() instanceof Player))
                    return false;

                int mana = Integer.parseInt(e.getArgs()[0]);
                CursedUser user = getHandlerRegistry().getCursedUserHandler().getCursedUser(e.getMainEntity().getUniqueId());

                if (e.isRemoval()) {
                    user.setMana(user.getMana() - mana);
                } else {
                    user.setMana(user.getMana() + mana);
                }
                return true;
            }
        });
        AEAPI.registerEffect(this, "DECREASE_MANA", new CustomEffect() {
            @Override
            public boolean onEffectActivation(EffectActivationEvent e) {
                if (!e.getEnchantmentType().equals(AEnchantmentType.EFFECT_STATIC))
                    return false;
                if (e.getArgs().length == 0)
                    return false;
                if (!(e.getMainEntity() instanceof Player))
                    return false;

                int mana = Integer.parseInt(e.getArgs()[0]);
                CursedUser user = getHandlerRegistry().getCursedUserHandler().getCursedUser(e.getMainEntity().getUniqueId());

                if (e.isRemoval()) {
                    user.setMana(user.getMana() + mana);
                } else {
                    user.setMana(user.getMana() - mana);
                }
                return true;
            }
        });

        // LUCK EFFECTS
        AEAPI.registerEffect(this, "INCREASE_LUCK", new CustomEffect() {
            @Override
            public boolean onEffectActivation(EffectActivationEvent e) {
                if (!e.getEnchantmentType().equals(AEnchantmentType.EFFECT_STATIC))
                    return false;
                if (e.getArgs().length == 0)
                    return false;
                if (!(e.getMainEntity() instanceof Player))
                    return false;

                Player player = (Player) e.getMainEntity();

                int luck = Integer.parseInt(e.getArgs()[0]);

                if (e.isRemoval()) {
                    getEmberPlugin().getItemManager().setLuckLevels(player.getUniqueId(), getEmberPlugin().getItemManager().getLuckLevelOf(player) - luck);
                } else {
                    getEmberPlugin().getItemManager().setLuckLevels(player.getUniqueId(), getEmberPlugin().getItemManager().getLuckLevelOf(player) + luck);
                }
                return true;
            }
        });
        AEAPI.registerEffect(this, "DECREASE_LUCK", new CustomEffect() {
            @Override
            public boolean onEffectActivation(EffectActivationEvent e) {
                if (!e.getEnchantmentType().equals(AEnchantmentType.EFFECT_STATIC))
                    return false;
                if (e.getArgs().length == 0)
                    return false;
                if (!(e.getMainEntity() instanceof Player))
                    return false;

                Player player = (Player) e.getMainEntity();

                int luck = Integer.parseInt(e.getArgs()[0]);

                if (e.isRemoval()) {
                    getEmberPlugin().getItemManager().setLuckLevels(player.getUniqueId(), getEmberPlugin().getItemManager().getLuckLevelOf(player) + luck);
                } else {
                    getEmberPlugin().getItemManager().setLuckLevels(player.getUniqueId(), getEmberPlugin().getItemManager().getLuckLevelOf(player) - luck);
                }
                return true;
            }
        });

        // SPEED EFFECTS
        AEAPI.registerEffect(this, "INCREASE_SPEED", new CustomEffect() {
            @Override
            public boolean onEffectActivation(EffectActivationEvent e) {
                if (!e.getEnchantmentType().equals(AEnchantmentType.EFFECT_STATIC))
                    return false;
                if (e.getArgs().length == 0)
                    return false;
                if (!(e.getMainEntity() instanceof Player))
                    return false;

                Player player = (Player) e.getMainEntity();

                float multiplier = Float.parseFloat(e.getArgs()[0]);

                if (e.isRemoval()) {
                    player.setWalkSpeed(player.getWalkSpeed() / multiplier);
                } else {
                    player.setWalkSpeed(player.getWalkSpeed() * multiplier);
                }
                return true;
            }
        });
        AEAPI.registerEffect(this, "DECREASE_SPEED", new CustomEffect() {
            @Override
            public boolean onEffectActivation(EffectActivationEvent e) {
                if (!e.getEnchantmentType().equals(AEnchantmentType.EFFECT_STATIC))
                    return false;
                if (e.getArgs().length == 0)
                    return false;
                if (!(e.getMainEntity() instanceof Player))
                    return false;

                Player player = (Player) e.getMainEntity();

                float multiplier = Float.parseFloat(e.getArgs()[0]);

                if (e.isRemoval()) {
                    player.setWalkSpeed(player.getWalkSpeed() * multiplier);
                } else {
                    player.setWalkSpeed(player.getWalkSpeed() / multiplier);
                }
                return true;
            }
        });

        // REGEN EFFECTS
        AEAPI.registerEffect(this, "INCREASE_REGEN", new CustomEffect() {
            @Override
            public boolean onEffectActivation(EffectActivationEvent e) {
                if (!e.getEnchantmentType().equals(AEnchantmentType.EFFECT_STATIC))
                    return false;
                if (e.getArgs().length == 0)
                    return false;
                if (!(e.getMainEntity() instanceof Player))
                    return false;

                int regen = Integer.parseInt(e.getArgs()[0]);
                CursedUser user = getHandlerRegistry().getCursedUserHandler().getCursedUser(e.getMainEntity().getUniqueId());

                if (e.isRemoval()) {
                    user.setRegen(user.getRegen() - regen);
                } else {
                    user.setRegen(user.getRegen() + regen);
                }
                return true;
            }
        });
        AEAPI.registerEffect(this, "DECREASE_REGEN", new CustomEffect() {
            @Override
            public boolean onEffectActivation(EffectActivationEvent e) {
                if (!e.getEnchantmentType().equals(AEnchantmentType.EFFECT_STATIC))
                    return false;
                if (e.getArgs().length == 0)
                    return false;
                if (!(e.getMainEntity() instanceof Player))
                    return false;

                int regen = Integer.parseInt(e.getArgs()[0]);
                CursedUser user = getHandlerRegistry().getCursedUserHandler().getCursedUser(e.getMainEntity().getUniqueId());

                if (e.isRemoval()) {
                    user.setRegen(user.getRegen() + regen);
                } else {
                    user.setRegen(user.getRegen() - regen);
                }
                return true;
            }
        });

        // STRENGTH EFFECTS
        AEAPI.registerEffect(this, "INCREASE_STRENGTH", new CustomEffect() {
            @Override
            public boolean onEffectActivation(EffectActivationEvent e) {
                if (!e.getEnchantmentType().equals(AEnchantmentType.EFFECT_STATIC))
                    return false;
                if (e.getArgs().length == 0)
                    return false;
                if (!(e.getMainEntity() instanceof Player))
                    return false;

                double strength = Double.parseDouble(e.getArgs()[0]);
                CursedUser user = getHandlerRegistry().getCursedUserHandler().getCursedUser(e.getMainEntity().getUniqueId());

                if (e.isRemoval()) {
                    user.setStrength(user.getStrength() / strength);
                } else {
                    user.setStrength(user.getStrength() * strength);
                }
                return true;
            }
        });
        AEAPI.registerEffect(this, "DECREASE_STRENGTH", new CustomEffect() {
            @Override
            public boolean onEffectActivation(EffectActivationEvent e) {
                if (!e.getEnchantmentType().equals(AEnchantmentType.EFFECT_STATIC))
                    return false;
                if (e.getArgs().length == 0)
                    return false;
                if (!(e.getMainEntity() instanceof Player))
                    return false;

                double strength = Double.parseDouble(e.getArgs()[0]);
                CursedUser user = getHandlerRegistry().getCursedUserHandler().getCursedUser(e.getMainEntity().getUniqueId());

                if (e.isRemoval()) {
                    user.setStrength(user.getStrength() / strength);
                } else {
                    user.setStrength(user.getStrength() * strength);
                }
                return true;
            }
        });
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

    public WorldGuardPlugin getWorldGuardPlugin() {
        return worldGuardPlugin;
    }

    public EmberPlugin getEmberPlugin() {
        return emberPlugin;
    }
}
