package me.lightlord323dev.cursedlevels.api.skill.data.skills;

import me.lightlord323dev.cursedlevels.api.skill.Skill;
import me.lightlord323dev.cursedlevels.api.skill.data.SkillData;

/**
 * Created by Luda on 8/30/2020.
 */
public class FarmingData extends SkillData {

    private double regenBase, regenMultiplier, doubleCropBase, doubleCropMultiplier;

    public FarmingData() {
        super(Skill.FARMING);
    }

    @Override
    public void loadData() {
        this.regenBase = getBonusDouble("regen-base");
        this.regenMultiplier = getBonusDouble("regen-multiplier");
        this.doubleCropBase = getBonusDouble("double-crop-base");
        this.doubleCropMultiplier = getBonusDouble("double-crop-multiplier");
    }

    public double getRegenAmt(int level) {
        return getPositiveGradientAmt(regenBase, regenMultiplier, level);
    }

    public double getDoubleCropChance(int level) {
        return getPositiveGradientAmt(doubleCropBase, doubleCropMultiplier, level);
    }

    public double getRegenBase() {
        return regenBase;
    }
}
