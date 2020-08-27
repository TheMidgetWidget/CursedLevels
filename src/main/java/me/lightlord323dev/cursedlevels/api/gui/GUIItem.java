package me.lightlord323dev.cursedlevels.api.gui;

import me.lightlord323dev.cursedlevels.util.NBTApi;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Luda on 8/27/2020.
 */
public class GUIItem {

    private ItemStack itemStack;
    private int index;

    public GUIItem(ItemStack itemStack, int index) {
        this.itemStack = new NBTApi(itemStack).setBoolean("clClickable", true).getItemStack();
        this.index = index;
    }

    public ItemStack getItemStack() {
        return itemStack.clone();
    }

    public int getIndex() {
        return index;
    }
}
