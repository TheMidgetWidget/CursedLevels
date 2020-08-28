package me.lightlord323dev.cursedlevels.handler;

import me.lightlord323dev.cursedlevels.api.handler.Handler;
import me.lightlord323dev.cursedlevels.util.NBTApi;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Created by Luda on 8/28/2020.
 */
public class CursedGUIHandler implements Handler, Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player) || e.getClickedInventory() == null || e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR)
            return;
        if (new NBTApi(e.getCurrentItem()).hasKey("clClickable"))
            e.setCancelled(true);
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onUnload() {

    }
}
