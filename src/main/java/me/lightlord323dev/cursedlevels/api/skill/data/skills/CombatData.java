package me.lightlord323dev.cursedlevels.api.skill.data.skills;

import me.lightlord323dev.cursedlevels.api.skill.Skill;
import me.lightlord323dev.cursedlevels.api.skill.data.SkillData;

/**
 * Created by Luda on 8/30/2020.
 */
public class CombatData extends SkillData {

    private double healthIncrement, doubleDamageBase, doubleDamageMultiplier;

    public CombatData() {
        super(Skill.COMBAT);
    }

    @Override
    public void loadData() {
        this.healthIncrement = getBonusDouble("health-increment");
        this.doubleDamageBase = getBonusDouble("double-damage-base");
        this.doubleDamageMultiplier = getBonusDouble("double-damage-multiplier");
    }

    public double getAddedHealth(int level) {
        return level * healthIncrement;
    }

    public double getDoubleDamageChance(int level) {
        return getPositiveGradientAmt(doubleDamageBase, doubleDamageMultiplier, level) / 100;
    }
}
