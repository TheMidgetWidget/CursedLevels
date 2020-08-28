package me.lightlord323dev.cursedlevels.api.gui.skillgui;

import me.lightlord323dev.cursedlevels.api.gui.CursedGUI;
import me.lightlord323dev.cursedlevels.api.gui.GUIItem;
import me.lightlord323dev.cursedlevels.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Luda on 8/27/2020.
 */
public class SkillGUI extends CursedGUI {

    private String title;
    private List<GUIItem> controls, items;

    public SkillGUI(String title, List<GUIItem> items) {
        super(6, title, false, null, true);
        this.controls = Arrays.asList(
                new GUIItem(new ItemBuilder(Material.SIGN).setDisplayName(ChatColor.GREEN + "UP").build(), 7),
                new GUIItem(new ItemBuilder(Material.SIGN).setDisplayName(ChatColor.RED + "DOWN").build(), 8)
        );
        this.items = items;
        this.title = title;
    }

    @Override
    public Inventory getInventory() {
        return getInventory(0);
    }

    public Inventory getInventory(int translation) {
        Inventory inventory = super.getInventory();
        int index = 9, start = 0;
        boolean line = false, reverse = false;

        for (int i = 1; i <= translation; i++) {
            if (!line) {
                start++;
                line = true;
                if (i != 1)
                    reverse = !reverse;
            } else {
                start += 9;
                line = false;
            }
        }

        if (reverse)
            index = 17;

        for (int i = start; i < this.items.size(); i++) {
            if (index >= 54)
                break;
            if (!line) {
                inventory.setItem(index, this.items.get(i).getItemStack());
                index += 9;
                line = true;
                if (i != 0)
                    reverse = !reverse;
            } else {
                inventory.setItem(index, this.items.get(i).getItemStack());
                if (reverse) {
                    if (index % 9 == 0) {
                        line = false;
                        index += 9;
                    } else
                        index--;
                } else {
                    if ((index + 1) % 9 == 0) {
                        line = false;
                        index += 9;
                    } else
                        index++;
                }
            }
        }
        this.controls.forEach(guiItem -> inventory.setItem(guiItem.getIndex(), guiItem.getItemStack()));
        return inventory;
    }
}
