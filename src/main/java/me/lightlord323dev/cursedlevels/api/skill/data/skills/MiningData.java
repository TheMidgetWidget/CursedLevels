package me.lightlord323dev.cursedlevels.api.skill.data.skills;

import me.lightlord323dev.cursedlevels.api.skill.Skill;
import me.lightlord323dev.cursedlevels.api.skill.data.SkillData;

/**
 * Created by Luda on 8/29/2020.
 */
public class MiningData extends SkillData {

    private double dmgMultiplier, doubleOreBase, doubleOreMultiplier;

    public MiningData() {
        super(Skill.MINING);
    }

    @Override
    public void loadData() {
        dmgMultiplier = getBonusDouble("damage-multiplier");
        doubleOreBase = getBonusDouble("double-ore-base");
        doubleOreMultiplier = getBonusDouble("double-ore-multiplier");
    }

    public double getAppliedDamage(double damage, int level) {
        return getPositiveGradientAmt(damage, this.dmgMultiplier, level);
    }

    public double getDoubleOreChance(int level) {
        return getPositiveGradientAmt(this.doubleOreBase, this.doubleOreMultiplier, level) / 100;
    }

}
