package me.lightlord323dev.cursedlevels.api.skill.data.skills.mining;


import org.bukkit.Material;

/**
 * Created by Luda on 9/3/2020.
 */
public class MiningBlock {

    private Material material;
    private int exp;

    public MiningBlock(String str) {
        String[] arr = str.split(";");
        this.material = Material.matchMaterial(arr[0]);
        this.exp = Integer.parseInt(arr[1]);
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }
}
