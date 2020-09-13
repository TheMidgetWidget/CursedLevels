package me.lightlord323dev.cursedlevels.api.gui;

import me.lightlord323dev.cursedlevels.util.ItemBuilder;
import me.lightlord323dev.cursedlevels.util.NBTApi;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created by Luda on 8/27/2020.
 */
public class CursedGUI {

    private int size;
    private String title;
    private boolean fill, fillFirstRow;
    private List<GUIItem> items;

    public CursedGUI(int rows, String title, boolean fill, List<GUIItem> items) {
        this.size = rows * 9;
        if (size > 54)
            size = 54;
        this.title = title;
        this.fill = fill;
        this.fillFirstRow = false;
        this.items = items;
    }

    public CursedGUI(int rows, String title, boolean fill, List<GUIItem> items, boolean fillFirstRow) {
        this.size = rows * 9;
        if (size > 54)
            size = 54;
        this.title = title;
        this.fill = fill;
        this.items = items;
        this.fillFirstRow = fillFirstRow;
    }

    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(null, size, title);
        ItemStack filler = new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7)).setDisplayName(" ").build();
        filler = new NBTApi(filler).setBoolean("clClickable", true).getItemStack();
        if (fill) {
            for (int i = 0; i < size; i++)
                inventory.setItem(i, filler);
        }
        if (fillFirstRow) {
            for (int i = 0; i < 9; i++)
                inventory.setItem(i, filler);
        }
        if (items != null) {
            items.forEach(guiItem -> {
                if (guiItem.getIndex() < size)
                    inventory.setItem(guiItem.getIndex(), guiItem.getItemStack());
            });
        }
        return inventory;
    }

    public Inventory getInventory(ItemStack sides) {
        Inventory inventory = Bukkit.createInventory(null, size, title);
        ItemStack filler = new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7)).setDisplayName(" ").build();
        filler = new NBTApi(filler).setBoolean("clClickable", true).getItemStack();
        GUIItem sideItem = new GUIItem(new ItemBuilder(sides).setDisplayName(" ").build(), 0);
        if (fill) {
            for (int i = 0; i < size; i++)
                inventory.setItem(i, filler);
        }
        if (fillFirstRow) {
            for (int i = 0; i < 9; i++)
                inventory.setItem(i, filler);
        }
        for (int i = 0; i < size; i += 8) {
            if (i == 0)
                inventory.setItem(i, sideItem.getItemStack());
            else {
                for (int j = 0; j < 2; j++) {
                    if (i + j < size)
                        inventory.setItem(i + j, sideItem.getItemStack());
                    else
                        break;
                }
                i++;
            }
        }
        if (items != null) {
            items.forEach(guiItem -> {
                if (guiItem.getIndex() < size)
                    inventory.setItem(guiItem.getIndex(), guiItem.getItemStack());
            });
        }
        return inventory;
    }

    public int getSize() {
        return size;
    }

    public String getTitle() {
        return title;
    }

    public boolean isFill() {
        return fill;
    }

    public List<GUIItem> getItems() {
        return items;
    }
}
