package me.lightlord323dev.cursedlevels.api.enchantment;

import java.util.Map;

/**
 * Created by Luda on 9/11/2020.
 */
public class CursedUpgrade {

    private String name, format;
    private double price, chance;
    private Map<CursedEnchantmentType, Integer> effects;

    public CursedUpgrade(String name, String format, double price, double chance, Map<CursedEnchantmentType, Integer> effects) {
        this.name = name;
        this.format = format;
        this.price = price;
        this.chance = chance;
        this.effects = effects;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public double getChance() {
        return chance;
    }

    public String getFormat(int level) {
        String out;
        if (level > 0)
            out = format.replace("%level%", "+" + level);
        else
            out = format.replace("%level%", String.valueOf(level));
        return out.replace("%displayName%", name);
    }

    public Map<CursedEnchantmentType, Integer> getEffects() {
        return effects;
    }
}
