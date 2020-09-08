package me.lightlord323dev.cursedlevels.api.skill.data;

import me.lightlord323dev.cursedlevels.Main;
import me.lightlord323dev.cursedlevels.api.skill.Skill;
import me.lightlord323dev.cursedlevels.api.user.CursedUser;
import me.lightlord323dev.cursedlevels.util.ItemBuilder;
import me.lightlord323dev.cursedlevels.util.NBTApi;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Luda on 8/29/2020.
 */
public abstract class SkillData {

    private Skill skill;
    private ItemStack itemStack;
    private FileConfiguration config;
    private String rootPath, displayName, lore, guiTitle;
    private int levelCap, levelUpBase, levelUpMultiplier;
    private Map<Integer, String> messages;
    // progress bar
    private String acquired, unAcquired;
    private int numberOfCharacters;

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
        this.levelUpBase = config.getInt(rootPath + ".level-up.base");
        this.levelUpMultiplier = config.getInt(rootPath + ".level-up.multiplier");
        this.guiTitle = ChatColor.translateAlternateColorCodes('&', config.getString(rootPath + ".gui-title"));
        this.messages = new HashMap<>();
        config.getConfigurationSection(rootPath + ".rewards").getKeys(false).forEach(str -> messages.put(Integer.parseInt(str), ChatColor.translateAlternateColorCodes('&', config.getString(rootPath + ".rewards." + str))));

        // progress bar
        String disp = Main.getInstance().getHandlerRegistry().getMessageUtil().getMessage("main-menu-gui.progress-bar-character");
        this.acquired = Main.getInstance().getHandlerRegistry().getMessageUtil().getMessage("main-menu-gui.progress-bar-acquired-color") + disp;
        this.unAcquired = Main.getInstance().getHandlerRegistry().getMessageUtil().getMessage("main-menu-gui.progress-bar-unacquired-color") + disp;
        this.numberOfCharacters = Integer.parseInt(Main.getInstance().getHandlerRegistry().getMessageUtil().getMessage("main-menu-gui.progress-bar-number-of-characters"));

        loadData();
    }

    public abstract void loadData();

    protected double getBonusDouble(String path) {
        return getDouble("bonus." + path);
    }

    protected int getBonusInt(String path) {
        return config.getInt(rootPath + ".bonus." + path);
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

    public int getAmtNeededToLevelUp(int level) {
        return getPositiveGradientAmt(levelUpBase, levelUpMultiplier, level);
    }

    public double getPositiveGradientAmt(double base, double multiplier, int level) {
        if (level == 0)
            return 0;
        return multiplier * (level - 1) + base;
    }

    public int getPositiveGradientAmt(int base, int multiplier, int level) {
        return multiplier * level + base;
    }

    public double getNegativeGradientAmt(double base, double multiplier, int level) {
        return base - (multiplier * level);
    }

    public ItemStack getItemStack(CursedUser cursedUser) {
        int level = cursedUser.getSkillLevel(this.skill);
        int currentExp = cursedUser.getSkillExp(skill);
        int nextExp;
        if (level < this.levelCap)
            nextExp = getAmtNeededToLevelUp(level);
        else
            nextExp = getAmtNeededToLevelUp(level - 1);
        StringBuilder percentageBar = new StringBuilder();
        double percentage = (currentExp * 100) / (double) nextExp;
        int num = (int) (percentage * numberOfCharacters / 100);
        num--;
        for (int i = 0; i < numberOfCharacters; i++) {
            if (i < num)
                percentageBar.append(acquired);
            else
                percentageBar.append(unAcquired);
        }
        String lore = this.lore.replace("%level%", String.valueOf(level)).replace("%nextLevel%", String.valueOf(level + 1)).replace("%levelCap%", String.valueOf(levelCap)).replace("%currentExp%", String.valueOf(currentExp)).replace("%nextExp%", String.valueOf(nextExp)).replace("%percentage%", String.format("%.2f", percentage)).replace("%progressBar%", percentageBar.toString());
        String msg = getMessage(level + 1);
        if (msg != null)
            lore = lore.replace("%nextLevelMessage%", msg);
        else
            lore = lore.replace("%nextLevelMessage%", "");
        ItemStack skillItem = new ItemBuilder(this.itemStack.clone()).setDisplayName(this.displayName.replace("%level%", level + "").replace("%levelCap%", levelCap + "")).setLore(lore.split("\\n")).build();
        skillItem = new NBTApi(skillItem).setString("skillItem", this.skill.name()).getItemStack();
        return skillItem;
    }

    public String getMessage(int level) {
        if (messages.containsKey(level))
            return messages.get(level);
        return null;
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
