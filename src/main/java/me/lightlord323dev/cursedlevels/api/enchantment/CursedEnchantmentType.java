package me.lightlord323dev.cursedlevels.api.enchantment;

import me.lightlord323dev.cursedlevels.Main;
import org.bukkit.ChatColor;

/**
 * Created by Luda on 9/8/2020.
 */
public enum CursedEnchantmentType {

    HEALTH(getDefinedName("health"), getDefinedAddLore("health"), getDefinedSubLore("health")),
    MANA(getDefinedName("mana"), getDefinedAddLore("mana"), getDefinedSubLore("mana")),
    STRENGTH(getDefinedName("strength"), getDefinedAddLore("strength"), getDefinedSubLore("strength")),
    LUCK(getDefinedName("luck"), getDefinedAddLore("luck"), getDefinedSubLore("luck")),
    DEFENSE(getDefinedName("defense"), getDefinedAddLore("defense"), getDefinedSubLore("defense")),
    REGEN(getDefinedName("regen"), getDefinedAddLore("regen"), getDefinedSubLore("regen")),
    MANA_REGEN(getDefinedName("mana_regen"), getDefinedAddLore("mana_regen"), getDefinedSubLore("mana_regen"));

    String displayName, addLore, subLore;

    CursedEnchantmentType(String displayName, String addLore, String subLore) {
        this.displayName = displayName;
        this.addLore = addLore.replace("%displayName%", this.displayName);
        this.subLore = subLore.replace("%displayName%", this.displayName);
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getLore(int level) {
        if (level < 0)
            return subLore.replace("%level%", String.valueOf(Math.abs(level)));
        else
            return addLore.replace("%level%", String.valueOf(Math.abs(level)));
    }

    private static String getDefinedName(String path) {
        return ChatColor.translateAlternateColorCodes('&', Main.getInstance().getEnchantmentsFile().getConfig().getString("enchantments." + path + ".display-name"));
    }

    private static String getDefinedAddLore(String path) {
        return ChatColor.translateAlternateColorCodes('&', Main.getInstance().getEnchantmentsFile().getConfig().getString("enchantments." + path + ".add-lore"));
    }

    private static String getDefinedSubLore(String path) {
        return ChatColor.translateAlternateColorCodes('&', Main.getInstance().getEnchantmentsFile().getConfig().getString("enchantments." + path + ".subtract-lore"));
    }
}
