package me.lightlord323dev.cursedlevels.api.skill.data.skills.combat;

import me.lightlord323dev.cursedlevels.api.skill.Skill;
import me.lightlord323dev.cursedlevels.api.skill.data.SkillData;
import org.bukkit.entity.LivingEntity;
import org.mineacademy.boss.api.Boss;
import org.mineacademy.boss.api.BossAPI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luda on 8/30/2020.
 */
public class CombatData extends SkillData {

    private double doubleDamageBase, doubleDamageMultiplier;
    private int healthIncrement, entityExp, playerExp;
    private List<CombatMob> mobs, bosses;

    public CombatData() {
        super(Skill.COMBAT);
    }

    @Override
    public void loadData() {
        this.healthIncrement = getBonusInt("health-increment");
        this.doubleDamageBase = getBonusDouble("double-damage-base");
        this.doubleDamageMultiplier = getBonusDouble("double-damage-multiplier");
        this.entityExp = getBonusInt("entity-exp");
        this.playerExp = getBonusInt("player-exp");
        this.mobs = new ArrayList<>();
        this.bosses = new ArrayList<>();
        getStringList("bossmobs").forEach(str -> bosses.add(new CombatMob(str, true)));
        getStringList("entities").forEach(str -> mobs.add(new CombatMob(str, false)));
    }

    public int getAddedHealth(int level) {
        return level * healthIncrement;
    }

    public double getDoubleDamageChance(int level) {
        return getPositiveGradientAmt(doubleDamageBase, doubleDamageMultiplier, level) / 100;
    }

    public int getEntityExp(LivingEntity en) {
        if (bosses.size() == 0)
            return 1;
        Boss boss = BossAPI.getBoss(en);
        if (boss != null) {
            CombatMob entity = this.bosses.stream().filter(combatMob -> combatMob.getBoss().equalsIgnoreCase(boss.getName())).findAny().orElse(null);
            return entity == null ? -1 : entity.getExp();
        }
        if (mobs.size() == 0)
            return 1;
        CombatMob entity = this.mobs.stream().filter(combatMob -> combatMob.getEntity() == en.getType()).findAny().orElse(null);
        return entity == null ? -1 : entity.getExp();
    }

    public int getEntityExp() {
        return entityExp;
    }

    public int getPlayerExp() {
        return playerExp;
    }
}
