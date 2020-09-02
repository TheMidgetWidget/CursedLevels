package me.lightlord323dev.cursedlevels.api.skill.data.skills;

import me.lightlord323dev.cursedlevels.api.skill.Skill;
import me.lightlord323dev.cursedlevels.api.skill.data.SkillData;

/**
 * Created by Luda on 8/30/2020.
 */
public class ForagingData extends SkillData {

    private double speedMultiplier, doubleWoodBase, doubleWoodMultiplier;

    public ForagingData() {
        super(Skill.FORAGING);
    }

    @Override
    public void loadData() {
        this.speedMultiplier = getBonusDouble("speed-multiplier");
        this.doubleWoodBase = getBonusDouble("double-wood-base");
        this.doubleWoodMultiplier = getBonusDouble("double-wood-multiplier");
    }

    public double getSpeedMultiplier() {
        return speedMultiplier;
    }

    public double getDoubleWoodChance(int level) {
        return getPositiveGradientAmt(doubleWoodBase, doubleWoodMultiplier, level) / 100;
    }
}
