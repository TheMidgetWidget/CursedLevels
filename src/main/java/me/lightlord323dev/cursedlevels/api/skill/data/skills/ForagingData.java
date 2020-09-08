package me.lightlord323dev.cursedlevels.api.skill.data.skills;

import me.lightlord323dev.cursedlevels.api.skill.Skill;
import me.lightlord323dev.cursedlevels.api.skill.data.SkillData;
import me.lightlord323dev.cursedlevels.api.skill.data.skills.mining.MiningBlock;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luda on 8/30/2020.
 */
public class ForagingData extends SkillData {

    private double speedBase, speedMultiplier, doubleWoodBase, doubleWoodMultiplier;
    private List<MiningBlock> blocks;

    public ForagingData() {
        super(Skill.FORAGING);
    }

    @Override
    public void loadData() {
        this.speedBase = getBonusDouble("speed-base");
        this.speedMultiplier = getBonusDouble("speed-multiplier");
        this.doubleWoodBase = getBonusDouble("double-wood-base");
        this.doubleWoodMultiplier = getBonusDouble("double-wood-multiplier");
        this.blocks = new ArrayList<>();
        getStringList("blocks").forEach(str -> blocks.add(new MiningBlock(str)));
    }

    public float getSpeed(int level) {
        return (float) (speedBase * (speedMultiplier * level + 1));
    }

    public double getDoubleWoodChance(int level) {
        return getPositiveGradientAmt(doubleWoodBase, doubleWoodMultiplier, level) / 100;
    }

    public int getBlockExp(Block block) {
        if (blocks.size() == 0)
            return 1;
        MiningBlock miningBlock = this.blocks.stream().filter(b -> b.getMaterial() == block.getType() && b.getDurability() == (short) block.getData()).findAny().orElse(null);
        return miningBlock == null ? -1 : miningBlock.getExp();
    }
}
