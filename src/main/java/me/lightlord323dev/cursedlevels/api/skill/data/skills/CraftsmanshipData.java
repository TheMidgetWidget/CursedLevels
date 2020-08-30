package me.lightlord323dev.cursedlevels.api.skill.data.skills;

import me.lightlord323dev.cursedlevels.api.skill.Skill;
import me.lightlord323dev.cursedlevels.api.skill.data.SkillData;

/**
 * Created by Luda on 8/30/2020.
 */
public class CraftsmanshipData extends SkillData {

    private double luckIncrement;

    public CraftsmanshipData() {
        super(Skill.CRAFTSMANSHIP);
    }

    @Override
    public void loadData() {
        this.luckIncrement = getBonusDouble("luck-increment");
    }

    public double getLuckIncrement(int level) {
        return level * luckIncrement;
    }
}
