package me.lightlord323dev.cursedlevels.handler.skill;

import me.lightlord323dev.cursedlevels.Main;
import me.lightlord323dev.cursedlevels.api.event.CursedUserDamageEvent;
import me.lightlord323dev.cursedlevels.api.skill.Skill;
import me.lightlord323dev.cursedlevels.api.skill.data.skills.DefenseData;
import me.lightlord323dev.cursedlevels.api.user.CursedUser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Created by Luda on 9/1/2020.
 */
public class DefenseHandler extends SkillHandler {

    private DefenseData skillData;

    @Override
    public void onLoad() {
        super.onLoad();
        skillData = (DefenseData) Main.getInstance().getHandlerRegistry().getSkillDataHandler().getSkillData(Skill.DEFENSE);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDmg(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            CursedUser cursedUser = Main.getInstance().getHandlerRegistry().getCursedUserHandler().getCursedUser(player.getUniqueId());

            int exp = 1;

            // TRACKER UPDATE
            cursedUser.setSkillExp(skillData.getSkill(), cursedUser.getSkillExp(skillData.getSkill()) + exp);
            sendExpNotification(player, cursedUser, exp, skillData);

            int level = cursedUser.getSkillLevel(skillData.getSkill());

            // LEVELUP CHECK
            checkLevelUp(player, cursedUser, cursedUser.getSkillExp(skillData.getSkill()), skillData);

            if (level < cursedUser.getSkillLevel(skillData.getSkill())) {
                int prevDefense = (int) skillData.getDefenseAmt(level);
                cursedUser.setDefense(cursedUser.getDefense() - prevDefense + (int) skillData.getDefenseAmt(cursedUser.getSkillLevel(skillData.getSkill())));
            }
        }
    }

}
