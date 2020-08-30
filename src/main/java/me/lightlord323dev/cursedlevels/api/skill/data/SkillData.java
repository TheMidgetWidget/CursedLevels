package me.lightlord323dev.cursedlevels.api.skill.data;

import me.lightlord323dev.cursedlevels.Main;
import me.lightlord323dev.cursedlevels.api.skill.Skill;
import me.lightlord323dev.cursedlevels.util.ItemBuilder;
import me.lightlord323dev.cursedlevels.util.NBTApi;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created by Luda on 8/29/2020.
 */
public abstract class SkillData {

    private Skill skill;
    private ItemStack itemStack;
    private FileConfiguration config;
    private String rootPath, displayName, lore, guiTitle;
    private int levelCap;

    public SkillData(Skill skill) {
        this.skill = skill;
    }

    public void onLoad() {
        config = Main.getInstance().getHandlerRegistry().getSkillDataHandler().getSkillFile().getConfig();
        rootPath = "skills." + skill.toString().toLowerCase();
        itemStack = new ItemBuilder(config.getString(rootPath + ".display-item.material")).build();
        this.displayName = ChatColor.translateAlternateColorCodes('&', config.getString(rootPath + ".display-item.name"));
        this.lore = ChatColor.translateAlternateColorCodes('&', config.getString(rootPath + ".display-item.lore"));
        this.levelCap = config.getInt(rootPath + ".level-cap");
        this.guiTitle = ChatColor.translateAlternateColorCodes('&', config.getString(rootPath + ".gui-title"));
        loadData();
    }

    public abstract void loadData();

    protected double getBonusDouble(String path) {
        return getDouble("bonus." + path);
    }

    protected List<String> getStringList(String path) {
        return config.getStringList(rootPath + "." + path);
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

    public ItemStack getItemStack(int level) {
        ItemStack skillItem = new ItemBuilder(this.itemStack.clone()).setDisplayName(this.displayName.replace("%level%", level + "").replace("%levelCap%", levelCap + "")).setLore(lore.replace("%level%", level + "").replace("%levelCap%", levelCap + "")).build();
        skillItem = new NBTApi(skillItem).setString("skillItem", this.skill.toString()).getItemStack();
        return skillItem;
    }

    public Skill getSkill() {
        return skill;
    }

    public int getLevelCap() {
        return levelCap;
    }

    public String getGuiTitle() {
        return guiTitle;
    }
}
