package me.lightlord323dev.cursedlevels.api.skill.data.skills;

import me.lightlord323dev.cursedlevels.api.skill.Skill;
import me.lightlord323dev.cursedlevels.api.skill.data.SkillData;
import me.lightlord323dev.cursedlevels.util.ItemBuilder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Luda on 8/30/2020.
 */
public class FishingData extends SkillData {

    private double betterItemBase, getBetterItemMultiplier;
    private List<ItemStack> itemPool;

    public FishingData() {
        super(Skill.FISHING);
    }

    @Override
    public void loadData() {
        this.betterItemBase = getBonusDouble("better-item-base");
        this.getBetterItemMultiplier = getBonusDouble("better-item-multiplier");
        this.itemPool = new ArrayList<>();
        getStringList("item-pool").forEach(str -> {
            ItemStack itemStack = new ItemBuilder(str).build();
            if (itemStack != null)
                itemPool.add(itemStack);
        });
    }

    public double getBetterItemChance(int level) {
        return getPositiveGradientAmt(betterItemBase, getBetterItemMultiplier, level) / 100;
    }

    public ItemStack getRandomItem() {
        return itemPool.get(ThreadLocalRandom.current().nextInt(0, itemPool.size()));
    }
}
