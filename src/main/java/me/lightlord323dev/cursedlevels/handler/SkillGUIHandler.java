package me.lightlord323dev.cursedlevels.handler;

import me.lightlord323dev.cursedlevels.Main;
import me.lightlord323dev.cursedlevels.api.gui.GUIItem;
import me.lightlord323dev.cursedlevels.api.gui.skillgui.SkillGUI;
import me.lightlord323dev.cursedlevels.api.handler.Handler;
import me.lightlord323dev.cursedlevels.api.user.CursedUser;
import me.lightlord323dev.cursedlevels.util.MessageUtil;
import me.lightlord323dev.cursedlevels.util.NBTApi;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luda on 8/28/2020.
 */
public class SkillGUIHandler implements Handler, Listener {

    private List<SkillGUI> activeGUI;

    @Override
    public void onLoad() {
        this.activeGUI = new ArrayList<>();
    }

    @Override
    public void onUnload() {

    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player) || e.getClickedInventory() == null || e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR)
            return;

        Player player = (Player) e.getWhoClicked();
        SkillGUI skillGUI = getActiveSkillGUI(player);

        if (skillGUI != null)
            e.setCancelled(true);


        NBTApi nbtApi = new NBTApi(e.getCurrentItem());

        if (nbtApi.hasKey("clScroll")) {
            int translation = nbtApi.getInt("clScroll");
            Inventory inv = skillGUI.getInventory(translation);
            if (inv == null) {
                MessageUtil.error(player, Main.getInstance().getHandlerRegistry().getMessageUtil().getMessage("skills-gui.scroll-error"));
            } else {
                player.openInventory(inv);
                cacheActiveSkillGUI(skillGUI); // cache again because it gets decached when inventory closes
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player))
            return;
        SkillGUI skillGUI = getActiveSkillGUI(((Player) e.getPlayer()));
        if (skillGUI != null)
            decacheActiveSkillGUI(skillGUI);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        SkillGUI skillGUI = getActiveSkillGUI((e.getPlayer()));
        if (skillGUI != null)
            decacheActiveSkillGUI(skillGUI);
    }


    public void cacheActiveSkillGUI(SkillGUI skillGUI) {
        if (!this.activeGUI.contains(skillGUI))
            this.activeGUI.add(skillGUI);
    }

    private void decacheActiveSkillGUI(SkillGUI skillGUI) {
        if (this.activeGUI.contains(skillGUI))
            this.activeGUI.remove(skillGUI);
    }

    private SkillGUI getActiveSkillGUI(Player player) {
        return activeGUI.stream().filter(skillGUI -> skillGUI.getOwnerUUID().equalsIgnoreCase(player.getUniqueId().toString())).findAny().orElse(null);
    }
}
