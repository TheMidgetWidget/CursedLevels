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
import me.lightlord323dev.cursedlevels.api.skill.data.skills.mining.MiningData;
import me.lightlord323dev.cursedlevels.api.user.CursedUser;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Luda on 8/31/2020.
 */
public class MiningHandler extends SkillHandler {

    private MiningData skillData;

    @Override
    public void onLoad() {
        super.onLoad();
        skillData = (MiningData) Main.getInstance().getHandlerRegistry().getSkillDataHandler().getSkillData(Skill.MINING);
    }

    // TRACKER AND MINING BONUS
    @EventHandler
    public void onMine(BlockBreakEvent e) {
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

        int exp = skillData.getBlockExp(e.getBlock());

        if (exp == -1)
            return;

        CursedUser cursedUser = Main.getInstance().getHandlerRegistry().getCursedUserHandler().getCursedUser(e.getPlayer().getUniqueId());

        // TRACKER UPDATE
        cursedUser.setSkillExp(skillData.getSkill(), cursedUser.getSkillExp(skillData.getSkill()) + exp);
        sendExpNotification(e.getPlayer(), cursedUser, exp, skillData);

        // LEVELUP CHECK
        checkLevelUp(e.getPlayer(), cursedUser, cursedUser.getSkillExp(skillData.getSkill()), skillData);

        // MINING BONUS
        int level = cursedUser.getSkillLevel(skillData.getSkill());
        if (level > 0 && e.getBlock().getType() == Material.GOLD_ORE || e.getBlock().getType() == Material.IRON_ORE) {
            double chance = ThreadLocalRandom.current().nextInt(0, 401) / 400.0;
            if (chance <= skillData.getDoubleOreChance(level)) {
                e.setCancelled(true);
                e.getBlock().getDrops().forEach(itemStack -> {
                    itemStack.setAmount(itemStack.getAmount() * 2);
                    e.getPlayer().getWorld().dropItemNaturally(e.getBlock().getLocation(), itemStack);
                });
                e.getBlock().setType(Material.AIR);
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

        int prevLevel = cursedUser.getSkillLevel(skillData.getSkill());

        // LEVELUP CHECK
        checkLevelUp(e.getWhoBroke(), cursedUser, cursedUser.getSkillExp(skillData.getSkill()), skillData);
        int level = cursedUser.getSkillLevel(skillData.getSkill());

        if (level > prevLevel) {
            cursedUser.setStrength(cursedUser.getStrength() + skillData.getDmgMultiplier());
        }

        // MINING BONUS
        if (level > 0 && e.getBrokenBlock().getType() == Material.GOLD_ORE || e.getBrokenBlock().getType() == Material.IRON_ORE) {
            double chance = ThreadLocalRandom.current().nextInt(0, 401) / 400.0;
            if (chance <= skillData.getDoubleOreChance(level)) {
                e.getBrokenBlock().getDrops().forEach(itemStack -> e.getBrokenBlock().getWorld().dropItemNaturally(e.getBrokenBlock().getLocation(), itemStack));
            }
        }
    }

}
