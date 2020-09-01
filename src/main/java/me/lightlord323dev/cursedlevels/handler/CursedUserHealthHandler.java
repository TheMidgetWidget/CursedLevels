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
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.concurrent.TimeUnit;

/**
 * Created by Luda on 9/2/2020.
 */
public class CursedUserHealthHandler implements Handler, Listener {

    private final String actionBarMessage = ChatColor.GREEN + "HEALTH: " + ChatColor.GOLD + "%health%" + ChatColor.GRAY + "/" + ChatColor.GOLD + "%maxHealth%";
    private FarmingData farmingData;

    @Override
    public void onLoad() {
        this.farmingData = (FarmingData) Main.getInstance().getHandlerRegistry().getSkillDataHandler().getSkillData(Skill.FARMING);
        Main.getInstance().getExecutorService().scheduleAtFixedRate(() -> {
            Bukkit.getServer().getOnlinePlayers().forEach(player -> {
                CursedUser cursedUser = Main.getInstance().getHandlerRegistry().getCursedUserHandler().getCursedUser(player.getUniqueId());
                if (System.currentTimeMillis() - cursedUser.getLastInCombat() >= 5000 && System.currentTimeMillis() - cursedUser.getLastSentLevelUp() >= 3000) {
                    int regenAmt = (int) farmingData.getRegenAmt(cursedUser.getSkillLevel(Skill.FARMING));
                    if (regenAmt == 0)
                        regenAmt = (int) farmingData.getRegenBase();
                    cursedUser.setHealth(cursedUser.getHealth() + regenAmt);
                }
                sendActionBar(player, actionBarMessage.replace("%health%", cursedUser.getHealth() + "").replace("%maxHealth%", cursedUser.getMaxHealth() + ""));

                // UPDATE HEALTH
                int cursedUserHealth = cursedUser.getHealth();
                double healthFactor = cursedUserHealth / (double) cursedUser.getMaxHealth();
                if (healthFactor >= 0.05) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
                        player.setHealth(player.getMaxHealth() * healthFactor);
                    });
                }
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
                int cursedUserHealth = cursedUser.getHealth() - event.getDamage();
                double healthFactor = cursedUserHealth / (double) cursedUser.getMaxHealth();
                if (healthFactor >= 0.05)
                    player.setHealth(player.getMaxHealth() * healthFactor);
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
        }
    }

    public static void sendActionBar(Player p, String message) {
        PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + message.replace("&", "§") + "\"}"), (byte) 2);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
    }
}
