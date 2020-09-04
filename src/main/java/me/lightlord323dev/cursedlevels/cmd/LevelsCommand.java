package me.lightlord323dev.cursedlevels.cmd;

import me.lightlord323dev.cursedlevels.Main;
import me.lightlord323dev.cursedlevels.api.user.CursedUser;
import me.lightlord323dev.cursedlevels.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Luda on 9/4/2020.
 */
public class LevelsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        // /levels player add;rem health;mana;luck;regen;defense amt

        if (args.length == 4) {

            Player player = Bukkit.getPlayer(args[0]);
            if (player == null) {
                MessageUtil.error(sender, "Player not found.");
                return true;
            }

            CursedUser cursedUser = Main.getInstance().getHandlerRegistry().getCursedUserHandler().getCursedUser(player.getUniqueId());

            if (!isInt(args[3])) {
                MessageUtil.error(sender, "Amount must be a valid integer.");
                return true;
            }

            int amt = Integer.parseInt(args[3]);

            switch (args[2]) {
                case "health":
                    cursedUser.setMaxHealth(cursedUser.getMaxHealth() + amt);
                    break;
                case "mana":
                    cursedUser.setMana(cursedUser.getMana() + amt);
                    break;
                case "luck":
                    Main.getInstance().getEmberPlugin().getItemManager().setLuckLevels(player.getUniqueId(), Main.getInstance().getEmberPlugin().getItemManager().getLuckLevelOf(player));
                    break;
                case "regen":
                    cursedUser.setRegen(cursedUser.getRegen() + amt);
                    break;
                case "defense":
                    cursedUser.setDefense(cursedUser.getDefense() + amt);
                    break;
                default:
                    MessageUtil.error(sender, "skill not found");
                    return false;
            }

            MessageUtil.success(sender, "Skill values updated for user.");
            return true;
        }

        MessageUtil.error(sender, "Usage:\n/levels <player> add;rem health;mana;regen;defense <amt>");
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

}
