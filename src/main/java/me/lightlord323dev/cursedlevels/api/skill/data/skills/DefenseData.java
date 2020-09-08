package me.lightlord323dev.cursedlevels.api.skill.data.skills;

import me.lightlord323dev.cursedlevels.api.skill.Skill;
import me.lightlord323dev.cursedlevels.api.skill.data.SkillData;

/**
 * Created by Luda on 8/30/2020.
 */
public class DefenseData extends SkillData {

    private double defenseBase, defenseMultiplier;

    public DefenseData() {
        super(Skill.DEFENSE);
    }

    @Override
    public void loadData() {
        this.defenseBase = getBonusDouble("defense-base");
        this.defenseMultiplier = getBonusDouble("defense-multiplier");
    }

    public double getDefenseAmt(int level) {
        return defenseMultiplier * level + defenseBase;
    }

    public double getDefenseBase() {
        return defenseBase;
    }
}
