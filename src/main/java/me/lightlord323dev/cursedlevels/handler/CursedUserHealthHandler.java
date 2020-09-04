package me.lightlord323dev.cursedlevels.handler;

import me.lightlord323dev.cursedlevels.Main;
import me.lightlord323dev.cursedlevels.api.event.CursedUserDamageEvent;
import me.lightlord323dev.cursedlevels.api.handler.Handler;
import me.lightlord323dev.cursedlevels.api.skill.Skill;
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

import java.util.concurrent.TimeUnit;

/**
 * Created by Luda on 9/2/2020.
 */
public class CursedUserHealthHandler implements Handler, Listener {

    private String actionBarMessage;
    private FarmingData farmingData;

    @Override
    public void onLoad() {
        this.actionBarMessage = Main.getInstance().getHandlerRegistry().getMessageUtil().getMessage("action-bar.main-hud");
        this.farmingData = (FarmingData) Main.getInstance().getHandlerRegistry().getSkillDataHandler().getSkillData(Skill.FARMING);
        Main.getInstance().getExecutorService().scheduleAtFixedRate(() -> {
            Bukkit.getServer().getOnlinePlayers().forEach(player -> {
                CursedUser cursedUser = Main.getInstance().getHandlerRegistry().getCursedUserHandler().getCursedUser(player.getUniqueId());
                if (System.currentTimeMillis() - cursedUser.getLastInCombat() >= 5000) {
                    int regenAmt = cursedUser.getRegen();
                    if (regenAmt == 0)
                        regenAmt = (int) farmingData.getRegenBase();
                    cursedUser.setHealth(cursedUser.getHealth() + regenAmt);

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
                        sendActionBar(player, actionBarMessage.replace("%health%", cursedUser.getHealth() + "").replace("%maxHealth%", cursedUser.getMaxHealth() + ""));
                });
            });
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
        e.getCursedUser().setLastInCombat(System.currentTimeMillis());
        e.getCursedUser().setHealth(e.getCursedUser().getHealth() - e.getDamage());
        Player player = Bukkit.getServer().getPlayer(e.getCursedUser().getUniqueId());
        sendActionBar(player, actionBarMessage.replace("%health%", e.getCursedUser().getHealth() + "").replace("%maxHealth%", e.getCursedUser().getMaxHealth() + ""));
        if (e.getCursedUser().getHealth() <= 0) {
            player.setHealth(0);
            e.getCursedUser().setHealth(e.getCursedUser().getMaxHealth());
            e.setDead(true);
        }
    }

    @EventHandler
    public void onPlayerRegainHealth(EntityRegainHealthEvent event) {
        if (event.getEntity() instanceof Player && event.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED || event.getRegainReason() == EntityRegainHealthEvent.RegainReason.REGEN || event.getRegainReason() == EntityRegainHealthEvent.RegainReason.MAGIC_REGEN)
            event.setCancelled(true);
    }

    public static void sendActionBar(Player p, String message) {
        PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + message.replace("&", "§") + "\"}"), (byte) 2);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
    }
}
