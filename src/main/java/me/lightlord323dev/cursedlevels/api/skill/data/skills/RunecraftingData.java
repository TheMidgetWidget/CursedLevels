package me.lightlord323dev.cursedlevels.api.skill.data.skills;

import me.lightlord323dev.cursedlevels.api.skill.Skill;
import me.lightlord323dev.cursedlevels.api.skill.data.SkillData;

/**
 * Created by Luda on 8/30/2020.
 */
public class RunecraftingData extends SkillData {

    private double wisdomBase, wisdomMultiplier;

    public RunecraftingData() {
        super(Skill.RUNECRAFTING);
    }

    @Override
    public void loadData() {
        this.wisdomBase = getBonusDouble("wisdom-base");
        this.wisdomMultiplier = getBonusDouble("wisdom-multiplier");
    }

    public double getWisdomAmt(int level) {
        return getPositiveGradientAmt(wisdomBase, wisdomMultiplier, level);
    }
}
