package me.lightlord323dev.cursedlevels.enchantment;

import me.lightlord323dev.cursedlevels.Main;
import me.lightlord323dev.cursedlevels.api.enchantment.CursedEnchantment;
import me.lightlord323dev.cursedlevels.api.enchantment.CursedEnchantmentType;
import me.lightlord323dev.cursedlevels.api.user.CursedUser;
import org.bukkit.entity.Player;

/**
 * Created by Luda on 9/11/2020.
 */
public class DefenseEnchantment extends CursedEnchantment {

    public DefenseEnchantment() {
        super(CursedEnchantmentType.DEFENSE);
    }

    @Override
    public void onEquip(Player player, int level, boolean held) {
        super.onEquip(player, level, held);
        CursedUser user = Main.getInstance().getHandlerRegistry().getCursedUserHandler().getCursedUser(player.getUniqueId());
        user.setDefense(user.getDefense() + level);
    }

    @Override
    public void onRemove(Player player, int level, boolean held) {
        super.onRemove(player, level, held);
        CursedUser user = Main.getInstance().getHandlerRegistry().getCursedUserHandler().getCursedUser(player.getUniqueId());
        user.setDefense(user.getDefense() - level);
    }
}
