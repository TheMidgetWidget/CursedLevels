package me.lightlord323dev.cursedlevels.handler.skill;

import me.lightlord323dev.cursedlevels.Main;
import me.lightlord323dev.cursedlevels.api.skill.Skill;
import me.lightlord323dev.cursedlevels.api.skill.data.skills.CombatData;
import me.lightlord323dev.cursedlevels.api.user.CursedUser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Luda on 8/31/2020.
 */
public class CombatHandler extends SkillHandler {

    private CombatData skillData;

    @Override
    public void onLoad() {
        skillData = (CombatData) Main.getInstance().getHandlerRegistry().getSkillDataHandler().getSkillData(Skill.COMBAT);
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        if (e.getEntity().getKiller() != null) {
            CursedUser cursedUser = Main.getInstance().getHandlerRegistry().getCursedUserHandler().getCursedUser(e.getEntity().getKiller().getUniqueId());

            // TRACKER UPDATE
            if (e.getEntity() instanceof Player)
                cursedUser.setCombatExp(cursedUser.getCombatExp() + skillData.getPlayerExp());
            else
                cursedUser.setCombatExp(cursedUser.getCombatExp() + skillData.getEntityExp());

            // LEVELUP CHECK
            checkLevelUp(e.getEntity().getKiller(), cursedUser, cursedUser.getCombatExp(), skillData);

            // UPDATE HEALTH
            cursedUser.setMaxHealth(cursedUser.getMaxHealth() + skillData.getAddedHealth(cursedUser.getSkillLevel(skillData.getSkill())));
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            CursedUser cursedUser = Main.getInstance().getHandlerRegistry().getCursedUserHandler().getCursedUser(e.getDamager().getUniqueId());
            int level = cursedUser.getSkillLevel(skillData.getSkill());
            if (level > 0) {
                double chance = ThreadLocalRandom.current().nextInt(0, 401) / 400.0;
                if (chance <= skillData.getDoubleDamageChance(level)) {
                    e.setDamage(e.getDamage() * 2);
                }
            }
        }
    }

}
