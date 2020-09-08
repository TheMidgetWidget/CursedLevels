package me.lightlord323dev.cursedlevels.api.skill.data.skills.mining;


import me.lightlord323dev.cursedlevels.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Luda on 9/3/2020.
 */
public class MiningBlock {

    private ItemStack itemStack;
    private int exp;

    public MiningBlock(String str) {
        String[] arr = str.split(";");
        this.itemStack = new ItemBuilder(arr[0]).build();
        this.exp = Integer.parseInt(arr[1]);
    }

    public Material getMaterial() {
        return itemStack.getType();
    }

    public void setMaterial(Material material) {
        this.itemStack.setType(material);
    }

    public short getDurability() {
        return itemStack.getDurability();
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }
}
