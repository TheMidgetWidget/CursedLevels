package me.lightlord323dev.cursedlevels.handler.skill;

import me.brook.embercore.api.events.EmberCraftEvent;
import me.lightlord323dev.cursedlevels.Main;
import me.lightlord323dev.cursedlevels.api.skill.Skill;
import me.lightlord323dev.cursedlevels.api.skill.data.skills.RunecraftingData;
import me.lightlord323dev.cursedlevels.api.user.CursedUser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

/**
 * Created by Luda on 9/3/2020.
 */
public class RunecraftingHandler extends SkillHandler {

    private RunecraftingData skillData;

    @Override
    public void onLoad() {
        super.onLoad();
        skillData = (RunecraftingData) Main.getInstance().getHandlerRegistry().getSkillDataHandler().getSkillData(Skill.MINING);
    }

    // TRACKER
    @EventHandler
    public void onCraft(EmberCraftEvent e) {

        Player player = (Player) e.getPlayer();

        CursedUser cursedUser = Main.getInstance().getHandlerRegistry().getCursedUserHandler().getCursedUser(e.getPlayer().getUniqueId());
        int exp = 1;

        // TRACKER UPDATE
        cursedUser.setSkillExp(skillData.getSkill(), cursedUser.getSkillExp(skillData.getSkill()) + exp);
        sendExpNotification(player, cursedUser, exp, skillData);

        // LEVELUP CHECK
        checkLevelUp(player, cursedUser, cursedUser.getSkillExp(skillData.getSkill()), skillData);
    }

}
