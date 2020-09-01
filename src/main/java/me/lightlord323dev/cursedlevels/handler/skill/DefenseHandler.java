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
        skillData = (DefenseData) Main.getInstance().getHandlerRegistry().getSkillDataHandler().getSkillData(Skill.DEFENSE);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDmg(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            CursedUser cursedUser = Main.getInstance().getHandlerRegistry().getCursedUserHandler().getCursedUser(player.getUniqueId());

            // UPDATE TRACKER
            cursedUser.setDefenseExp(cursedUser.getDefenseExp() + (int) e.getDamage());

            // CHECK LEVEL UP
            checkLevelUp(player, cursedUser, cursedUser.getDefenseExp(), skillData);
        }
    }

    @EventHandler
    public void onCursedUserDmg(CursedUserDamageEvent e) {
        if (e.getCursedUser().getSkillLevel(skillData.getSkill()) > 0) {
            int dmg = e.getDamage() - (int) skillData.getDefenseAmt(e.getCursedUser().getSkillLevel(skillData.getSkill()));
            if (dmg < 0)
                dmg = 0;
            e.setDamage(dmg);
        }
    }

}
