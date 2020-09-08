package me.lightlord323dev.cursedlevels.cmd;

import me.lightlord323dev.cursedlevels.Main;
import me.lightlord323dev.cursedlevels.api.user.CursedUser;
import me.lightlord323dev.cursedlevels.util.MessageUtil;
import n3kas.ae.api.AEAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * Created by Luda on 9/4/2020.
 */
public class LevelsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        // /levels player add;rem health;mana;luck;regen;defense amt

        if (!sender.hasPermission("cursedlevels.admin"))
            return true;

        if (args.length == 4) {

            Player player = Bukkit.getPlayer(args[0]);
            if (player == null) {
                MessageUtil.error(sender, "Player not found.");
                return true;
            }

            CursedUser cursedUser = Main.getInstance().getHandlerRegistry().getCursedUserHandler().getCursedUser(player.getUniqueId());

            if (args[1].equalsIgnoreCase("rem") || args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("set")) {

                if (args[1].equalsIgnoreCase("set")) {

                    if (!isFloat(args[3])) {
                        MessageUtil.error(sender, "Amount must be between 0 and 1.");
                        return true;
                    }

                    float speed = Float.parseFloat(args[3]);

                    if (speed > 1)
                        speed = 1f;
                    if (speed < 0)
                        speed = 0.2f;

                    player.setWalkSpeed(speed);
                } else {
                    if (!isInt(args[3])) {
                        MessageUtil.error(sender, "Amount must be a valid integer.");
                        return true;
                    }
                    int amt = Integer.parseInt(args[3]);

                    if (args[1].equalsIgnoreCase("rem")) {
                        amt *= -1;
                    }

                    switch (args[2]) {
                        case "health":
                            cursedUser.setMaxHealth(cursedUser.getMaxHealth() + amt);
                            break;
                        case "mana":
                            cursedUser.setMaxMana(cursedUser.getMaxMana() + amt);
                            break;
                        case "manaregen":
                            cursedUser.setManaregen(cursedUser.getManaregen() + amt);
                            break;
                        case "luck":
                            Main.getInstance().getEmberPlugin().getItemManager().setLuckLevels(player.getUniqueId(), Main.getInstance().getEmberPlugin().getItemManager().getLuckLevelOf(player) + amt);
                            break;
                        case "regen":
                            cursedUser.setRegen(cursedUser.getRegen() + amt);
                            break;
                        case "defense":
                            cursedUser.setDefense(cursedUser.getDefense() + amt);
                            break;
                        case "strength":
                            cursedUser.setStrength(cursedUser.getStrength() + (amt / 100.0));
                            break;
                        default:
                            MessageUtil.error(sender, "skill not found");
                            return false;
                    }
                }

                MessageUtil.success(sender, "Skill values updated for user.");
                return true;
            }
        }

        MessageUtil.error(sender, "Usage:\n/levels <player> add;rem health;regen;mana;manaregen;luck;defense;strength <amt>\n/levels <player> set speed <speed>");
        return true;
    }

    private boolean isInt(String str) {
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private boolean isFloat(String str) {
        try {
            Float.parseFloat(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

}
