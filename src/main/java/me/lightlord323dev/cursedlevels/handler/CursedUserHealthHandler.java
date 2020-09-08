package me.lightlord323dev.cursedlevels.handler;

import me.lightlord323dev.cursedlevels.Main;
import me.lightlord323dev.cursedlevels.api.event.CursedUserDamageEvent;
import me.lightlord323dev.cursedlevels.api.handler.Handler;
import me.lightlord323dev.cursedlevels.api.skill.Skill;
import me.lightlord323dev.cursedlevels.api.skill.data.skills.BlacksmithingData;
import me.lightlord323dev.cursedlevels.api.skill.data.skills.FarmingData;
import me.lightlord323dev.cursedlevels.api.user.CursedUser;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.plugin.IllegalPluginAccessException;

import java.util.concurrent.TimeUnit;

/**
 * Created by Luda on 9/2/2020.
 */
public class CursedUserHealthHandler implements Handler, Listener {

    private String actionBarMessage;
    private FarmingData farmingData;
    private BlacksmithingData blacksmithingData;
    private long combatTag;

    @Override
    public void onLoad() {
        this.actionBarMessage = Main.getInstance().getHandlerRegistry().getMessageUtil().getMessage("action-bar.main-hud");
        this.farmingData = (FarmingData) Main.getInstance().getHandlerRegistry().getSkillDataHandler().getSkillData(Skill.FARMING);
        this.blacksmithingData = (BlacksmithingData) Main.getInstance().getHandlerRegistry().getSkillDataHandler().getSkillData(Skill.BLACKSMITHING);
        this.combatTag = (long) (Main.getInstance().getSettingsFile().getConfig().getDouble("combat-tag-timer") * 1000);
        Main.getInstance().getExecutorService().scheduleAtFixedRate(() -> {
            try {
                Bukkit.getServer().getOnlinePlayers().forEach(player -> {
                    CursedUser cursedUser = Main.getInstance().getHandlerRegistry().getCursedUserHandler().getCursedUser(player.getUniqueId());
                    if (cursedUser != null) {
                        if (System.currentTimeMillis() - cursedUser.getLastInCombat() >= combatTag) {
                            int regenAmt = cursedUser.getRegen(), manaRegenAmt = cursedUser.getManaregen();
                            if (regenAmt == 0)
                                cursedUser.setRegen((int) farmingData.getRegenBase());

                            cursedUser.setHealth(cursedUser.getHealth() + regenAmt);
                            cursedUser.setMana(cursedUser.getMana() + manaRegenAmt);

                            // UPDATE HEALTH
                            int cursedUserHealth = cursedUser.getHealth();
                            double healthFactor = cursedUserHealth / (double) cursedUser.getMaxHealth(), newHealth = player.getMaxHealth() * healthFactor;
                            if (healthFactor >= 0.05 && newHealth - player.getHealth() >= 0.5) {
                                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
                                    player.setHealth(newHealth);
                                });
                            }
                        }
                        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
                            int stayDuration = 2300;
                            if (System.currentTimeMillis() - cursedUser.getLastSentLevelUp() >= stayDuration && System.currentTimeMillis() - cursedUser.getLastSentExp() >= stayDuration)
                                sendActionBar(player, actionBarMessage.replace("%health%", String.valueOf(cursedUser.getHealth())).replace("%maxHealth%", String.valueOf(cursedUser.getMaxHealth())).replace("%defense%", String.valueOf(cursedUser.getDefense())).replace("%mana%", String.valueOf(cursedUser.getMana())).replace("%speed%", String.valueOf(player.getWalkSpeed())).replace("%regen%", String.valueOf(cursedUser.getRegen())).replace("%strength%", String.valueOf((int) (cursedUser.getStrength() * 100))).replace("%luck%", String.valueOf(Main.getInstance().getEmberPlugin().getItemManager().getLuckLevelOf(player))).replace("%maxMana%", String.valueOf(cursedUser.getMaxMana())));
                        });
                    }
                });
            } catch (IllegalPluginAccessException e) {
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 500, TimeUnit.MILLISECONDS);
    }

    @Override
    public void onUnload() {
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDmg(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            CursedUser cursedUser = Main.getInstance().getHandlerRegistry().getCursedUserHandler().getCursedUser(player.getUniqueId());
            CursedUserDamageEvent event = new CursedUserDamageEvent(cursedUser, (int) e.getDamage());
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                e.setCancelled(true);
                player.damage(0);
                if (!event.isDead()) {
                    int cursedUserHealth = cursedUser.getHealth() - event.getDamage();
                    double healthFactor = cursedUserHealth / (double) cursedUser.getMaxHealth();
                    if (healthFactor >= 0.05)
                        player.setHealth(player.getMaxHealth() * healthFactor);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCursedUserDmg(CursedUserDamageEvent e) {
        int dmg = e.getDamage() - e.getCursedUser().getDefense() / 10; // defense divided by 10
        if (dmg < 0)
            dmg = 0;
        e.setDamage(dmg);

        e.getCursedUser().setLastInCombat(System.currentTimeMillis());
        e.getCursedUser().setHealth(e.getCursedUser().getHealth() - e.getDamage());
        Player player = Bukkit.getServer().getPlayer(e.getCursedUser().getUniqueId());
        sendActionBar(player, actionBarMessage.replace("%health%", String.valueOf(e.getCursedUser().getHealth())).replace("%maxHealth%", String.valueOf(e.getCursedUser().getMaxHealth())).replace("%defense%", String.valueOf(e.getCursedUser().getDefense())).replace("%mana%", String.valueOf(e.getCursedUser().getMana())).replace("%speed%", String.valueOf(player.getWalkSpeed())).replace("%regen%", String.valueOf(e.getCursedUser().getRegen())).replace("%strength%", String.valueOf((int) (e.getCursedUser().getStrength() * 100))).replace("%luck%", String.valueOf(Main.getInstance().getEmberPlugin().getItemManager().getLuckLevelOf(player))).replace("%maxMana%", String.valueOf(e.getCursedUser().getMaxMana())));
        if (e.getCursedUser().getHealth() <= 0) {
            player.setHealth(0);
            e.getCursedUser().setHealth(e.getCursedUser().getMaxHealth());
            e.setDead(true);
        }
    }

    @EventHandler
    public void onPlayerRegainHealth(EntityRegainHealthEvent event) {
        if (event.getEntity() instanceof Player)
            event.setCancelled(true);
    }

    public static void sendActionBar(Player p, String message) {
        PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + message.replace("&", "§") + "\"}"), (byte) 2);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
    }
}
