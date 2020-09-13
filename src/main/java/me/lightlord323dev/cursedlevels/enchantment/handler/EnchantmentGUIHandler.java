package me.lightlord323dev.cursedlevels.enchantment.handler;

import me.lightlord323dev.cursedlevels.Main;
import me.lightlord323dev.cursedlevels.api.enchantment.CursedEnchantment;
import me.lightlord323dev.cursedlevels.api.enchantment.CursedEnchantmentType;
import me.lightlord323dev.cursedlevels.api.enchantment.CursedUpgrade;
import me.lightlord323dev.cursedlevels.api.gui.CursedGUI;
import me.lightlord323dev.cursedlevels.api.gui.GUIItem;
import me.lightlord323dev.cursedlevels.api.handler.Handler;
import me.lightlord323dev.cursedlevels.util.ItemBuilder;
import me.lightlord323dev.cursedlevels.util.NBTApi;
import me.lightlord323dev.cursedlevels.util.file.AbstractFile;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Luda on 9/12/2020.
 */
public class EnchantmentGUIHandler implements Handler, Listener {

    private List<CursedUpgrade> upgrades;
    private AbstractFile upgradesFile;
    private ItemStack enchantButton;

    private Sound success, error;

    @Override
    public void onLoad() {

        upgradesFile = new AbstractFile(Main.getInstance(), "upgrades.yml", true);

        upgrades = new ArrayList<>();
        FileConfiguration config = upgradesFile.getConfig();
        config.getKeys(false).forEach(upgrade -> {

            Map<CursedEnchantmentType, Integer> map = new HashMap<>();
            config.getStringList(upgrade + ".effects").forEach(str -> {
                String[] arr = str.split(";");
                CursedEnchantmentType type = CursedEnchantmentType.valueOf(arr[0].toUpperCase());
                int amt = Integer.parseInt(arr[1]);
                map.put(type, amt);
            });

            upgrades.add(new CursedUpgrade(
                    ChatColor.translateAlternateColorCodes('&', config.getString(upgrade + ".display-name")),
                    ChatColor.translateAlternateColorCodes('&', config.getString(upgrade + ".format")),
                    config.getDouble(upgrade + ".price"),
                    config.getDouble(upgrade + ".chance"),
                    map
            ));
        });

        FileConfiguration settingsConfig = Main.getInstance().getSettingsFile().getConfig();

        this.enchantButton = new ItemBuilder(settingsConfig.getString("enchant-gui-button.material")).setDisplayName(ChatColor.translateAlternateColorCodes('&', settingsConfig.getString("enchant-gui-button.display-name"))).setLore(ChatColor.translateAlternateColorCodes('&', settingsConfig.getString("enchant-gui-button.lore")).split("\\n")).build();

        if (!settingsConfig.getString("upgrades.successful-sound").equalsIgnoreCase("none"))
            success = Sound.valueOf(settingsConfig.getString("upgrades.successful-sound").toUpperCase());
        if (!settingsConfig.getString("upgrades.error-sound").equalsIgnoreCase("none"))
            error = Sound.valueOf(settingsConfig.getString("upgrades.error-sound").toUpperCase());
    }

    @Override
    public void onUnload() {
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getClickedInventory() != null && e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.ENCHANTMENT_TABLE) {
            if (new NBTApi(e.getCurrentItem()).hasKey("clEnchant")) {
                ItemStack itemStack = e.getClickedInventory().getItem(13);
                if (isEnchantable(itemStack)) {
                    double chance = ThreadLocalRandom.current().nextInt(0, 401) / 400.0;
                    Object[] arr = upgrades.stream().filter(cursedUpgrade -> chance <= (cursedUpgrade.getChance() / 100.0)).toArray();
                    if (arr.length == 0) {
                        e.getWhoClicked().sendMessage(Main.getInstance().getHandlerRegistry().getMessageUtil().getMessage("upgrades.error-no-roll"));
                        if (error != null)
                            ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), error, 1, 1);
                        return;
                    }
                    CursedUpgrade upgrade = (CursedUpgrade) arr[ThreadLocalRandom.current().nextInt(0, arr.length)];
                    if (Main.getEcon().getBalance((Player) e.getWhoClicked()) >= upgrade.getPrice()) {
                        ItemStack enchantedItem = itemStack.clone();
                        Map<CursedEnchantment, Integer> cursedEnchantments = Main.getInstance().getHandlerRegistry().getEnchantmentHandler().getEnchantments(itemStack);
                        for (Map.Entry<CursedEnchantmentType, Integer> entry : upgrade.getEffects().entrySet()) {
                            int level = entry.getValue();
                            Map.Entry<CursedEnchantment, Integer> ench = cursedEnchantments.entrySet().stream().filter(en -> en.getKey().getType() == entry.getKey()).findAny().orElse(null);
                            if (ench != null)
                                level += ench.getValue();
                            enchantedItem = Main.getInstance().getHandlerRegistry().getEnchantmentHandler().applyEnchantment(enchantedItem, entry.getKey(), level, upgrade.getFormat(entry.getValue()));
                        }
                        e.getClickedInventory().setItem(13, enchantedItem);
                        Main.getEcon().withdrawPlayer(((Player) e.getWhoClicked()), upgrade.getPrice());
                        e.getWhoClicked().sendMessage(Main.getInstance().getHandlerRegistry().getMessageUtil().getMessage("upgrades.successful-upgrade").replace("%upgrade%", upgrade.getName()));
                        if (success != null)
                            ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), success, 1, 1);
                    } else {
                        e.getWhoClicked().sendMessage(Main.getInstance().getHandlerRegistry().getMessageUtil().getMessage("upgrades.error-no-money").replace("%upgrade%", upgrade.getName()));
                        if (error != null)
                            ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), error, 1, 1);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getPlayer().hasMetadata("clEnchantInv")) {
            ItemStack itemStack = e.getInventory().getItem(13);
            if (itemStack != null && itemStack.getType() != Material.AIR)
                e.getPlayer().getWorld().dropItemNaturally(e.getPlayer().getEyeLocation(), itemStack);
            e.getPlayer().removeMetadata("clEnchantInv", Main.getInstance());
        }
    }

    public void openEnchantGUI(Player player) {
        CursedGUI cursedGUI = new CursedGUI(5, Main.getInstance().getHandlerRegistry().getMessageUtil().getMessage("enchant-gui.title"), true, Arrays.asList(new GUIItem(enchantButton.clone(), 31).addIntegerValue("clEnchant", 0)));
        Inventory inv = cursedGUI.getInventory(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 10));
        inv.setItem(13, new ItemStack(Material.AIR));
        player.openInventory(inv);
        player.setMetadata("clEnchantInv", new FixedMetadataValue(Main.getInstance(), 0));
    }

    private boolean isEnchantable(ItemStack itemStack) {
        return itemStack != null && itemStack.getType() != Material.AIR && (isArmor(itemStack) || itemStack.getType().toString().endsWith("SWORD") || itemStack.getType() == Material.BOW);
    }

    private boolean isArmor(ItemStack itemStack) {
        String typeNameString = itemStack.getType().name();
        return typeNameString.endsWith("_HELMET")
                || typeNameString.endsWith("_CHESTPLATE")
                || typeNameString.endsWith("_LEGGINGS")
                || typeNameString.endsWith("_BOOTS");
    }
}
