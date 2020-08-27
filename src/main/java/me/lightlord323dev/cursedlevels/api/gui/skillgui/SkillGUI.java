package me.lightlord323dev.cursedlevels.api.gui.skillgui;

import me.lightlord323dev.cursedlevels.api.gui.GUIItem;
import me.lightlord323dev.cursedlevels.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Luda on 8/27/2020.
 */
public class SkillGUI {

    private String title;
    private List<GUIItem> controls, items;

    public SkillGUI(String title, List<GUIItem> items) {
        this.controls = Arrays.asList(
                new GUIItem(new ItemBuilder(Material.SIGN).setDisplayName(ChatColor.GREEN + "UP").build(), 7),
                new GUIItem(new ItemBuilder(Material.SIGN).setDisplayName(ChatColor.RED + "DOWN").build(), 8)
        );
        this.items = items;
        this.title = title;
    }

    public Inventory getInventory() {
        return getInventory(0);
    }

    public Inventory getInventory(int translation) {
        Inventory inventory = Bukkit.createInventory(null, 54, this.title);
        int index = 9;
        boolean line = false, reverse = false;
        for (int i = translation * 9; i < this.items.size(); i++) {
            if (!line) {
                inventory.setItem(index, this.items.get(i).getItemStack());
                index += 9;
                line = true;
                if (i != 0)
                    reverse = !reverse;
            } else {
                inventory.setItem(index, this.items.get(i).getItemStack());
                if (reverse) {
                    if (index % 9 == 0)
                        line = false;
                    index--;
                } else {
                    if ((index + 1) % 9 == 0)
                        line = false;
                    index++;
                }
            }
        }
        return inventory;
    }
}
