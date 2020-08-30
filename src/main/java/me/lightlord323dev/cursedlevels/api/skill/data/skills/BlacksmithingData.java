package me.lightlord323dev.cursedlevels.api.skill.data.skills;

import me.lightlord323dev.cursedlevels.api.skill.Skill;
import me.lightlord323dev.cursedlevels.api.skill.data.SkillData;

/**
 * Created by Luda on 8/30/2020.
 */
public class BlacksmithingData extends SkillData {

    private double manaRegenBase, manaRegenMultiplier;

    public BlacksmithingData() {
        super(Skill.BLACKSMITHING);
    }

    @Override
    public void loadData() {
        this.manaRegenBase = getBonusDouble("mana-regen-base");
        this.manaRegenMultiplier = getBonusDouble("mana-regen-multiplier");
    }

    public double getManaRegenAmt(int level) {
        return getPositiveGradientAmt(manaRegenBase, manaRegenMultiplier, level);
    }
}
