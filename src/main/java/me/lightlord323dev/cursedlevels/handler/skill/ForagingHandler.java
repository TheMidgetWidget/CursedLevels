package me.lightlord323dev.cursedlevels.handler.skill;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.sk89q.worldguard.bukkit.BukkitUtil;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import dev.mrshawn.oreregenerator.api.events.OreBreakEvent;
import me.lightlord323dev.cursedlevels.Main;
import me.lightlord323dev.cursedlevels.api.skill.Skill;
import me.lightlord323dev.cursedlevels.api.skill.data.skills.ForagingData;
import me.lightlord323dev.cursedlevels.api.user.CursedUser;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Luda on 9/1/2020.
 */
public class ForagingHandler extends SkillHandler {

    private ForagingData skillData;

    @Override
    public void onLoad() {
        super.onLoad();
        skillData = (ForagingData) Main.getInstance().getHandlerRegistry().getSkillDataHandler().getSkillData(Skill.FORAGING);
    }

    // TRACKER AND MINING BONUS
    @EventHandler
    public void onMineWood(BlockBreakEvent e) {
        if (Main.getInstance().getWorldGuardPlugin() != null) {
            ApplicableRegionSet regionSet = Main.getInstance().getWorldGuardPlugin().getRegionManager(e.getBlock().getWorld()).getApplicableRegions(BukkitUtil.toVector(e.getBlock()));
            if (regionSet.queryState(Main.getInstance().getWorldGuardPlugin().wrapPlayer(e.getPlayer()), DefaultFlag.BLOCK_BREAK) == StateFlag.State.DENY)
                return;
        }

        Island island = SuperiorSkyblockAPI.getIslandAt(e.getBlock().getLocation());
        if (island != null) {
            if (!island.isMember(SuperiorSkyblockAPI.getPlayer(e.getPlayer())))
                return;
        }

        if (e.getBlock().getType() == Material.LOG || e.getBlock().getType() == Material.LOG_2 || e.getBlock().getType() == Material.WOOD) {
            CursedUser cursedUser = Main.getInstance().getHandlerRegistry().getCursedUserHandler().getCursedUser(e.getPlayer().getUniqueId());

            int exp = skillData.getBlockExp(e.getBlock());

            if (exp == -1)
                return;

            // TRACKER UPDATE
            cursedUser.setSkillExp(skillData.getSkill(), cursedUser.getSkillExp(skillData.getSkill()) + exp);
            sendExpNotification(e.getPlayer(), cursedUser, exp, skillData);

            // LEVELUP CHECK
            checkLevelUp(e.getPlayer(), cursedUser, cursedUser.getSkillExp(skillData.getSkill()), skillData);

            // UPDATE BONUSES
            int level = cursedUser.getSkillLevel(skillData.getSkill());
            if (level > 0) {
                // SPEED
                applySpeedCheck(e.getPlayer(), level);
                // DOUBLE WOOD BONUS
                double chance = ThreadLocalRandom.current().nextInt(0, 401) / 400.0;
                if (chance <= skillData.getDoubleWoodChance(level)) {
                    e.setCancelled(true);
                    e.getBlock().getDrops().forEach(itemStack -> {
                        itemStack.setAmount(itemStack.getAmount() * 2);
                        e.getPlayer().getWorld().dropItemNaturally(e.getBlock().getLocation(), itemStack);
                    });
                    e.getBlock().setType(Material.AIR);
                }
            }
        }
    }

    @EventHandler
    public void onOreBreak(OreBreakEvent e) {
        CursedUser cursedUser = Main.getInstance().getHandlerRegistry().getCursedUserHandler().getCursedUser(e.getWhoBroke().getUniqueId());
        int exp = 1;

        // TRACKER UPDATE
        cursedUser.setSkillExp(skillData.getSkill(), cursedUser.getSkillExp(skillData.getSkill()) + exp);
        sendExpNotification(e.getWhoBroke(), cursedUser, exp, skillData);

        // LEVELUP CHECK
        checkLevelUp(e.getWhoBroke(), cursedUser, cursedUser.getSkillExp(skillData.getSkill()), skillData);

        // UPDATE BONUSES
        int level = cursedUser.getSkillLevel(skillData.getSkill());
        if (level > 0) {
            // SPEED
            applySpeedCheck(e.getWhoBroke(), level);
            // DOUBLE WOOD BONUS
            double chance = ThreadLocalRandom.current().nextInt(0, 401) / 400.0;
            if (chance <= skillData.getDoubleWoodChance(level)) {
                e.getBrokenBlock().getDrops().forEach(itemStack -> e.getBrokenBlock().getWorld().dropItemNaturally(e.getBrokenBlock().getLocation(), itemStack));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent e) {
        CursedUser cursedUser = Main.getInstance().getHandlerRegistry().getCursedUserHandler().getCursedUser(e.getPlayer().getUniqueId());
        applySpeedCheck(e.getPlayer(), cursedUser.getSkillLevel(skillData.getSkill()));
    }

    private void applySpeedCheck(Player player, int level) {
        float speed = skillData.getSpeed(level);
        if (speed > 1)
            speed = 1;
        if (speed < 0)
            speed = 0.2f;
        player.setWalkSpeed(speed);
    }

}
