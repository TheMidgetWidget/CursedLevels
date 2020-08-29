package me.lightlord323dev.cursedlevels.api.skill.data;

import me.lightlord323dev.cursedlevels.Main;
import me.lightlord323dev.cursedlevels.api.skill.Skill;
import me.lightlord323dev.cursedlevels.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Luda on 8/29/2020.
 */
public abstract class SkillData {

    private Skill skill;
    private ItemStack itemStack;
    private FileConfiguration config;
    private String rootPath;

    public SkillData(Skill skill) {
        this.skill = skill;
    }

    public void onLoad() {
        config = Main.getInstance().getHandlerRegistry().getSkillDataHandler().getSkillFile().getConfig();
        rootPath = "skills." + skill.toString().toLowerCase();
        itemStack = new ItemBuilder(config.getString(rootPath + ".display-item.material"))
                .setDisplayName(ChatColor.translateAlternateColorCodes('&', config.getString(rootPath + ".display-item.name")))
                .setLore(ChatColor.translateAlternateColorCodes('&', config.getString(rootPath + ".display-item.lore")))
                .build();
        loadData();
    }

    public abstract void loadData();

    protected double getBonusDouble(String path) {
        return getDouble("bonus." + path);
    }

    protected String getBonusString(String path) {
        return getString("bonus." + path);
    }

    protected double getDouble(String path) {
        return config.getDouble(rootPath + "." + path);
    }

    protected String getString(String path) {
        return config.getString(rootPath + "." + path);
    }

    public double getPositiveGradientAmt(double base, double multiplier, int level) {
        return multiplier * level + base;
    }

    public double getNegativeGradientAmt(double base, double multiplier, int level) {
        return base - (multiplier * level);
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public Skill getSkill() {
        return skill;
    }
}
