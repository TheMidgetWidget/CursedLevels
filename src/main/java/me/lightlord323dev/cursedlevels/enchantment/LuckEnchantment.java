package me.lightlord323dev.cursedlevels.enchantment;

import me.lightlord323dev.cursedlevels.Main;
import me.lightlord323dev.cursedlevels.api.enchantment.CursedEnchantment;
import me.lightlord323dev.cursedlevels.api.enchantment.CursedEnchantmentType;
import org.bukkit.entity.Player;

/**
 * Created by Luda on 9/11/2020.
 */
public class LuckEnchantment extends CursedEnchantment {

    public LuckEnchantment() {
        super(CursedEnchantmentType.LUCK);
    }

    @Override
    public void onEquip(Player player, int level, boolean held) {
        super.onEquip(player, level, held);
        Main.getInstance().getEmberPlugin().getItemManager().setLuckLevels(player.getUniqueId(), Main.getInstance().getEmberPlugin().getItemManager().getLuckLevelOf(player) + level);
    }

    @Override
    public void onRemove(Player player, int level, boolean held) {
        super.onRemove(player, level, held);
        Main.getInstance().getEmberPlugin().getItemManager().setLuckLevels(player.getUniqueId(), Main.getInstance().getEmberPlugin().getItemManager().getLuckLevelOf(player) - level);
    }

}
