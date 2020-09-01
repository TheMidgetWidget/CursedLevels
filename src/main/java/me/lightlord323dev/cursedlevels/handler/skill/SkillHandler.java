package me.lightlord323dev.cursedlevels.handler.skill;

import me.lightlord323dev.cursedlevels.Main;
import me.lightlord323dev.cursedlevels.api.handler.Handler;
import me.lightlord323dev.cursedlevels.api.skill.data.SkillData;
import me.lightlord323dev.cursedlevels.api.user.CursedUser;
import me.lightlord323dev.cursedlevels.handler.CursedUserHealthHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

/**
 * Created by Luda on 8/31/2020.
 */
public class SkillHandler implements Handler, Listener {

    @Override
    public void onLoad() {
    }

    @Override
    public void onUnload() {
    }

    protected void checkLevelUp(Player player, CursedUser cursedUser, int currentExp, SkillData skill) {
        int level = cursedUser.getSkillLevel(skill.getSkill()), levelUpExp = skill.getAmtNeededToLevelUp(level);
        if (levelUpExp <= currentExp) {
            if (level >= skill.getLevelCap())
                return;
            cursedUser.addLevel(skill.getSkill());
            String msg = Main.getInstance().getHandlerRegistry().getMessageUtil().getMessage("skills.level-up-message").replace("%skill%", skill.getSkill().toString()).replace("%level%", (level + 1) + "");
            cursedUser.setLastSentLevelUp(System.currentTimeMillis());
            CursedUserHealthHandler.sendActionBar(player, msg);
            checkLevelUp(player, cursedUser, currentExp, skill);
        }
    }
}
