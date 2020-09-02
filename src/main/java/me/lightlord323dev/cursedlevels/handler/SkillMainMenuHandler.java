package me.lightlord323dev.cursedlevels.handler;

import me.lightlord323dev.cursedlevels.Main;
import me.lightlord323dev.cursedlevels.api.gui.CursedGUI;
import me.lightlord323dev.cursedlevels.api.gui.GUIItem;
import me.lightlord323dev.cursedlevels.api.gui.skillgui.SkillGUI;
import me.lightlord323dev.cursedlevels.api.handler.Handler;
import me.lightlord323dev.cursedlevels.api.skill.Skill;
import me.lightlord323dev.cursedlevels.api.skill.data.SkillData;
import me.lightlord323dev.cursedlevels.api.user.CursedUser;
import me.lightlord323dev.cursedlevels.util.ItemBuilder;
import me.lightlord323dev.cursedlevels.util.NBTApi;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luda on 8/31/2020.
 */
public class SkillMainMenuHandler implements Handler, Listener {

    // playuer head at 20
    // 13,14,15,22,23,24,31,32,33

    private ItemStack acquiredLevel, unAcquiredLevel;

    @Override
    public void onLoad() {
        FileConfiguration config = Main.getInstance().getHandlerRegistry().getSkillDataHandler().getSkillFile().getConfig();
        this.acquiredLevel = new ItemBuilder(config.getString("skill-tree.acquired-level.material")).build();
        this.unAcquiredLevel = new ItemBuilder(config.getString("skill-tree.unacquired-level.material")).build();
    }

    @Override
    public void onUnload() {

    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player) || e.getClickedInventory() == null || e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR)
            return;

        NBTApi nbtApi = new NBTApi(e.getCurrentItem());

        if (nbtApi.hasKey("skillItem")) {
            CursedUser cursedUser = Main.getInstance().getHandlerRegistry().getCursedUserHandler().getCursedUser(e.getWhoClicked().getUniqueId());
            Skill skill = Skill.valueOf(nbtApi.getString("skillItem"));
            SkillData skillData = Main.getInstance().getHandlerRegistry().getSkillDataHandler().getSkillData(skill);
            List<GUIItem> guiItems = new ArrayList<>();
            int level = cursedUser.getSkillLevel(skill), cap = skillData.getLevelCap();
            for (int i = 1; i <= cap; i++) {
                GUIItem guiItem;
                if (level < i) {
                    guiItem = new GUIItem(new ItemBuilder(unAcquiredLevel.clone()).setDisplayName(ChatColor.GREEN + "Level " + ChatColor.GOLD + String.valueOf(i)).build(), i);
                } else {
                    guiItem = new GUIItem(new ItemBuilder(acquiredLevel.clone()).setDisplayName(ChatColor.GREEN + "Level " + ChatColor.GOLD + String.valueOf(i)).build(), i);
                }
                guiItems.add(guiItem);
            }
            e.getWhoClicked().closeInventory();
            SkillGUI skillGUI = new SkillGUI(((Player) e.getWhoClicked()), skill, skillData.getGuiTitle(), guiItems);
            e.getWhoClicked().openInventory(skillGUI.getInventory());
        }
    }

    public static void openMainMenu(Player player) {
        CursedUser user = Main.getInstance().getHandlerRegistry().getCursedUserHandler().getCursedUser(player.getUniqueId());
        List<GUIItem> guiItems = new ArrayList<>();
        int index = 13, counter = 0;
        for (Skill skill : Skill.values()) {
            if (counter > 2) {
                index += 6;
                counter = 0;
            }
            SkillData skillData = Main.getInstance().getHandlerRegistry().getSkillDataHandler().getSkillData(skill);
            ItemStack itemStack = skillData.getItemStack(user);
            guiItems.add(new GUIItem(itemStack, index));
            index++;
            counter++;
        }
        CursedGUI cursedGUI = new CursedGUI(5,
                ChatColor.translateAlternateColorCodes('&', Main.getInstance().getHandlerRegistry().getMessageUtil().getMessage("main-menu-gui.title")),
                true,
                guiItems);
        player.openInventory(cursedGUI.getInventory());
    }

    public ItemStack getAcquiredLevelItem() {
        return acquiredLevel;
    }

    public ItemStack getUnAcquiredLevelItem() {
        return unAcquiredLevel;
    }
}
