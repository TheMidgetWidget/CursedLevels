package me.lightlord323dev.cursedlevels.handler.skill;

import me.lightlord323dev.cursedlevels.Main;
import me.lightlord323dev.cursedlevels.api.skill.Skill;
import me.lightlord323dev.cursedlevels.api.skill.data.skills.FishingData;
import me.lightlord323dev.cursedlevels.api.user.CursedUser;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Luda on 9/1/2020.
 */
public class FishingHandler extends SkillHandler {
    private FishingData skillData;

    @Override
    public void onLoad() {
        super.onLoad();
        skillData = (FishingData) Main.getInstance().getHandlerRegistry().getSkillDataHandler().getSkillData(Skill.FISHING);
    }

    @EventHandler
    public void onFish(PlayerFishEvent e) {
        if ((e.getState() == PlayerFishEvent.State.CAUGHT_ENTITY || e.getState() == PlayerFishEvent.State.CAUGHT_FISH) && e.getCaught() instanceof Item) {
            CursedUser cursedUser = Main.getInstance().getHandlerRegistry().getCursedUserHandler().getCursedUser(e.getPlayer().getUniqueId());

            int exp = 1;

            // TRACKER UPDATE
            cursedUser.setSkillExp(skillData.getSkill(), cursedUser.getSkillExp(skillData.getSkill()) + exp);
            sendExpNotification(e.getPlayer(), cursedUser, exp, skillData);

            // LEVELUP CHECK
            checkLevelUp(e.getPlayer(), cursedUser, cursedUser.getSkillExp(skillData.getSkill()), skillData);

            // FISHING BONUS
            int level = cursedUser.getSkillLevel(skillData.getSkill());
            if (level > 0) {
                double chance = ThreadLocalRandom.current().nextInt(0, 401) / 400.0;
                if (chance <= skillData.getBetterItemChance(level)) {
                    ItemStack itemStack = skillData.getRandomItem();
                    if (itemStack != null)
                        ((Item) e.getCaught()).setItemStack(itemStack);
                }
            }
        }
    }

}
