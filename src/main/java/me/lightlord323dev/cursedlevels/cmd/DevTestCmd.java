package me.lightlord323dev.cursedlevels.cmd;

import me.lightlord323dev.cursedlevels.api.gui.GUIItem;
import me.lightlord323dev.cursedlevels.api.gui.skillgui.SkillGUI;
import me.lightlord323dev.cursedlevels.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Luda on 8/28/2020.
 */
public class DevTestCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if (!sender.hasPermission("!cursedlevels.dev"))
            return true;


        if (args[0].equalsIgnoreCase("skillsgui")) {

            List<GUIItem> list = new ArrayList<>();

            for (int i = 1; i < 40;  i++) {
                list.add(new GUIItem(new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)4)).setDisplayName(i + "").build(), 0));
            }

            SkillGUI skillGUI = new SkillGUI(ChatColor.RED+"SKILL NAME", list);
            int translation = 0;
            if (args.length > 1)
                translation = Integer.parseInt(args[1]);
            ((Player)sender).openInventory(skillGUI.getInventory(translation));
        }

        return true;
    }
}
