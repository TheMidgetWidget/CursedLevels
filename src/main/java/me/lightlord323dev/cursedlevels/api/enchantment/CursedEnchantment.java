package me.lightlord323dev.cursedlevels.api.enchantment;

import me.lightlord323dev.cursedlevels.Main;
import org.bukkit.entity.Player;

/**
 * Created by Luda on 9/8/2020.
 */
public abstract class CursedEnchantment {

    private CursedEnchantmentType type;

    public CursedEnchantment(CursedEnchantmentType type) {
        this.type = type;
    }

    public void onEquip(Player player, int level, boolean held) {
        if (held)
            Main.getInstance().getHandlerRegistry().getEnchantmentHandler().addToEquipped(player);
    }

    public void onRemove(Player player, int level, boolean held) {
        if (held)
            Main.getInstance().getHandlerRegistry().getEnchantmentHandler().removeFromEquip(player);
    }

    public CursedEnchantmentType getType() {
        return type;
    }
}
