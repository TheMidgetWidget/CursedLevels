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
        super.onLoad();
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

        Island island = SuperiorSkyblockAPI.getIslandAt(e.getBlock().getLocation());
        if (island != null) {
            if (!island.isMember(SuperiorSkyblockAPI.getPlayer(e.getPlayer())))
                return;
        }

        if ((e.getBlock().getType() == Material.CROPS || e.getBlock().getType() == Material.POTATO || e.getBlock().getType() == Material.CARROT || e.getBlock().getType() == Material.NETHER_WARTS) && e.getBlock().getData() == 7) {
            CursedUser cursedUser = Main.getInstance().getHandlerRegistry().getCursedUserHandler().getCursedUser(e.getPlayer().getUniqueId());

            int exp = 1;

            // TRACKER UPDATE
            cursedUser.setSkillExp(skillData.getSkill(), cursedUser.getSkillExp(skillData.getSkill()) + exp);
            sendExpNotification(e.getPlayer(), cursedUser, exp, skillData);

            // LEVELUP CHECK
            checkLevelUp(e.getPlayer(), cursedUser, cursedUser.getSkillExp(skillData.getSkill()), skillData);


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

        if (prevLevel < cursedUser.getSkillLevel(skillData.getSkill())) {
            int prevRegen = (int) skillData.getRegenAmt(prevLevel);
            cursedUser.setRegen(cursedUser.getRegen() - prevRegen + (int) skillData.getRegenAmt(cursedUser.getSkillLevel(skillData.getSkill())));
        }

        // FARMING BONUS
        int level = cursedUser.getSkillLevel(skillData.getSkill());
        if (level > 0) {
            double chance = ThreadLocalRandom.current().nextInt(0, 401) / 400.0;
            if (chance <= skillData.getDoubleCropChance(level)) {
                e.getBrokenBlock().getDrops().forEach(itemStack -> {
                    itemStack.setAmount(itemStack.getAmount());
                    e.getWhoBroke().getWorld().dropItemNaturally(e.getBrokenBlock().getLocation(), itemStack);
                });
            }
        }
    }

}
