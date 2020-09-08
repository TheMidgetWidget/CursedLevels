package me.lightlord323dev.cursedlevels.api.skill.data.skills.mining;

import me.lightlord323dev.cursedlevels.api.skill.Skill;
import me.lightlord323dev.cursedlevels.api.skill.data.SkillData;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luda on 8/29/2020.
 */
public class MiningData extends SkillData {

    private double dmgMultiplier, doubleOreBase, doubleOreMultiplier;
    private List<MiningBlock> blocks;

    public MiningData() {
        super(Skill.MINING);
    }

    @Override
    public void loadData() {
        dmgMultiplier = getBonusDouble("strength-multiplier") / 100.0;
        doubleOreBase = getBonusDouble("double-ore-base");
        doubleOreMultiplier = getBonusDouble("double-ore-multiplier");
        blocks = new ArrayList<>();
        getStringList("blocks").forEach(str -> blocks.add(new MiningBlock(str)));
    }

    public double getAppliedDamage(double damage, int level) {
        if (level == 0)
            return damage;
        return getPositiveGradientAmt(damage, this.dmgMultiplier, level);
    }

    public double getDmgMultiplier() {
        return dmgMultiplier;
    }

    public double getDoubleOreChance(int level) {
        return getPositiveGradientAmt(this.doubleOreBase, this.doubleOreMultiplier, level) / 100;
    }

    public int getBlockExp(Block block) {
        if (blocks.size() == 0)
            return 1;
        MiningBlock miningBlock = this.blocks.stream().filter(b -> b.getMaterial() == block.getType() && b.getDurability() == (short) block.getData()).findAny().orElse(null);
        return miningBlock == null ? -1 : miningBlock.getExp();
    }
}
