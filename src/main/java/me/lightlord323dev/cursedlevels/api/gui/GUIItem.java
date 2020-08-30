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

    public GUIItem addIntegerValue(String key, int val) {
        this.itemStack = new NBTApi(itemStack).setInt(key, val).getItemStack();
        return this;
    }

    public GUIItem addStringValue(String key, String val) {
        this.itemStack = new NBTApi(itemStack).setString(key, val).getItemStack();
        return this;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack.clone();
    }

    public int getIndex() {
        return index;
    }
}
