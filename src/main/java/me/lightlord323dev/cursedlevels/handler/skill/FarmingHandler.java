package me.lightlord323dev.cursedlevels.handler.skill;

import com.sk89q.worldguard.bukkit.BukkitUtil;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import me.lightlord323dev.cursedlevels.Main;
import me.lightlord323dev.cursedlevels.api.skill.Skill;
import me.lightlord323dev.cursedlevels.api.skill.data.skills.FarmingData;
import me.lightlord323dev.cursedlevels.api.user.CursedUser;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Luda on 9/1/2020.
 */
public class FarmingHandler extends SkillHandler {

    private FarmingData skillData;

    @Override
    public void onLoad() {
        skillData = (FarmingData) Main.getInstance().getHandlerRegistry().getSkillDataHandler().getSkillData(Skill.FARMING);
    }

    // TRACKER AND FARMING BONUS
    @EventHandler
    public void onFarm(BlockBreakEvent e) {
        if (Main.getInstance().getWorldGuardPlugin() != null) {
            ApplicableRegionSet regionSet = Main.getInstance().getWorldGuardPlugin().getRegionManager(e.getBlock().getWorld()).getApplicableRegions(BukkitUtil.toVector(e.getBlock()));
            if (regionSet.queryState(Main.getInstance().getWorldGuardPlugin().wrapPlayer(e.getPlayer()), DefaultFlag.BLOCK_BREAK) == StateFlag.State.DENY)
                return;
        }

        if ((e.getBlock().getType() == Material.CROPS || e.getBlock().getType() == Material.POTATO || e.getBlock().getType() == Material.CARROT || e.getBlock().getType() == Material.NETHER_WARTS) && e.getBlock().getData() == 7) {
            CursedUser cursedUser = Main.getInstance().getHandlerRegistry().getCursedUserHandler().getCursedUser(e.getPlayer().getUniqueId());

            // TRACKER UPDATE
            cursedUser.setFarmingExp(cursedUser.getFarmingExp() + 1);

            // LEVELUP CHECK
            checkLevelUp(e.getPlayer(), cursedUser, cursedUser.getFarmingExp(), skillData);


            // FARMING BONUS
            int level = cursedUser.getSkillLevel(skillData.getSkill());
            if (level > 0) {
                double chance = ThreadLocalRandom.current().nextInt(0, 401) / 400.0;
                if (chance <= skillData.getDoubleCropChance(level)) {
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

}