package me.lightlord323dev.cursedlevels.enchantment.handler;

import com.google.gson.reflect.TypeToken;
import me.lightlord323dev.cursedlevels.Main;
import me.lightlord323dev.cursedlevels.api.enchantment.CursedEnchantment;
import me.lightlord323dev.cursedlevels.api.enchantment.CursedEnchantmentType;
import me.lightlord323dev.cursedlevels.api.handler.Handler;
import me.lightlord323dev.cursedlevels.enchantment.*;
import me.lightlord323dev.cursedlevels.util.NBTApi;
import me.lightlord323dev.cursedlevels.util.file.AbstractFile;
import me.lightlord323dev.cursedlevels.util.file.GsonUtil;
import n3kas.ae.utils.ArmorEquipEvent;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * Created by Luda on 9/8/2020.
 */
public class EnchantmentHandler implements Handler, Listener {

    private List<CursedEnchantment> cursedEnchantments;
    private List<String> holdingEnchant;

    private AbstractFile dataFile;

    @Override
    public void onLoad() {
        this.cursedEnchantments = Arrays.asList(
                new HealthEnchantment(),
                new ManaEnchantment(),
                new StrengthEnchantment(),
                new LuckEnchantment(),
                new DefenseEnchantment(),
                new RegenEnchantment(),
                new ManaRegenEnchantment()
        );
        dataFile = new AbstractFile(Main.getInstance(), "enchantmentdata.json", "data", false);

        holdingEnchant = GsonUtil.loadObject(new TypeToken<List<String>>() {
        }, dataFile.getFile());
        if (holdingEnchant == null)
            holdingEnchant = new ArrayList<>();
    }

    @Override
    public void onUnload() {
        GsonUtil.saveObject(holdingEnchant, dataFile.getFile());
    }

    @EventHandler
    public void onChangeItem(PlayerItemHeldEvent e) {

        ItemStack prevItem = e.getPlayer().getInventory().getItem(e.getPreviousSlot());
        ItemStack currentItem = e.getPlayer().getInventory().getItem(e.getNewSlot());

        // UNEQUIP
        if (prevItem != null && !isArmor(prevItem)) {
            getEnchantments(prevItem).entrySet().forEach(entry -> entry.getKey().onRemove(e.getPlayer(), entry.getValue(), true));
        }

        // EQUIP
        if (currentItem != null && !isArmor(currentItem)) {
            getEnchantments(currentItem).entrySet().forEach(entry -> entry.getKey().onEquip(e.getPlayer(), entry.getValue(), true));
        }
    }

    @EventHandler
    public void onArmorWear(ArmorEquipEvent e) {
        // UNEQUIP
        if (e.getOldArmorPiece() != null && e.getOldArmorPiece().getType() != Material.AIR) {
            getEnchantments(e.getOldArmorPiece()).entrySet().forEach(entry -> entry.getKey().onRemove(e.getPlayer(), entry.getValue(), false));
        }

        // EQUIP
        if (e.getNewArmorPiece() != null && e.getNewArmorPiece().getType() != Material.AIR) {
            getEnchantments(e.getNewArmorPiece()).entrySet().forEach(entry -> entry.getKey().onEquip(e.getPlayer(), entry.getValue(), false));
        }
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent e) {
        ItemStack itemStack = e.getItem().getItemStack();
        ItemStack heldItem = e.getPlayer().getItemInHand();
        if (heldItem != null && itemStack.isSimilar(heldItem)) {
            if (!isArmor(heldItem))
                getEnchantments(heldItem).entrySet().forEach(entry -> entry.getKey().onEquip(e.getPlayer(), entry.getValue(), true));
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null)
            return;

        if (e.getClick() == ClickType.SHIFT_LEFT || e.getClick() == ClickType.SHIFT_RIGHT) {
            Map<CursedEnchantment, Integer> enchants = getEnchantments(e.getCurrentItem());
            if (e.getSlot() == e.getWhoClicked().getInventory().getHeldItemSlot()) {
                if (enchants.size() == 0 || isArmor(e.getCurrentItem()))
                    return;
                if (e.getWhoClicked().getOpenInventory().getTopInventory().getType() != InventoryType.CRAFTING) { // TODO THIS SHIT DOESNT WORK
                    if (e.getWhoClicked().getOpenInventory().getTopInventory().firstEmpty() != -1) {
                        enchants.forEach((cursedEnchantment, level) -> cursedEnchantment.onRemove(((Player) e.getWhoClicked()), level, true));
                        return;
                    }
                }
                if (e.getWhoClicked().getInventory().firstEmpty() != -1)
                    enchants.forEach((cursedEnchantment, level) -> cursedEnchantment.onRemove(((Player) e.getWhoClicked()), level, true));
            } else {
                if ((isHeld(((Player) e.getWhoClicked())) || canBeShiftClicked(((Player) e.getWhoClicked()))) && enchants.size() > 0) { // TODO SOMETIMES ITEMS ARE ADDED AT THE END OF HOTBAR
                    if (isArmor(e.getCurrentItem()))
                        return;
                    if (e.getClickedInventory().getType() == InventoryType.PLAYER) {
                        InventoryType type = e.getWhoClicked().getOpenInventory().getTopInventory().getType();
                        if (e.getSlot() < 9 || ((type == InventoryType.CHEST || type == InventoryType.DISPENSER || type == InventoryType.DROPPER || type == InventoryType.ENDER_CHEST || type == InventoryType.HOPPER) && e.getWhoClicked().getOpenInventory().getTopInventory().firstEmpty() != -1))
                            return;
                    }
                    e.setCancelled(true);
                    e.getWhoClicked().setItemInHand(e.getCurrentItem());
                    e.setCurrentItem(new ItemStack(Material.AIR));
                    enchants.forEach((cursedEnchantment, level) -> cursedEnchantment.onEquip(((Player) e.getWhoClicked()), level, true));
                }
            }
            return;
        }

        if (e.getClick() == ClickType.NUMBER_KEY) {
            if (e.getHotbarButton() >= 0 && e.getHotbarButton() == e.getWhoClicked().getInventory().getHeldItemSlot() || e.getSlot() == e.getWhoClicked().getInventory().getHeldItemSlot()) {
                ItemStack hotbar = e.getWhoClicked().getInventory().getItem(e.getHotbarButton());
                ItemStack slot = e.getCurrentItem();

                if (slot != null && slot.getType() != Material.AIR) {
                    if (isArmor(slot))
                        return;
                    getEnchantments(slot).forEach((cursedEnchantment, level) -> {
                        if (e.getHotbarButton() == e.getWhoClicked().getInventory().getHeldItemSlot())
                            cursedEnchantment.onEquip(((Player) e.getWhoClicked()), level, true);
                        else
                            cursedEnchantment.onRemove(((Player) e.getWhoClicked()), level, true);
                    });
                }
                if (hotbar != null && hotbar.getType() != Material.AIR) {
                    if (isArmor(hotbar))
                        return;
                    getEnchantments(hotbar).forEach((cursedEnchantment, level) -> {
                        if (e.getHotbarButton() == e.getWhoClicked().getInventory().getHeldItemSlot())
                            cursedEnchantment.onRemove(((Player) e.getWhoClicked()), level, true);
                        else
                            cursedEnchantment.onEquip(((Player) e.getWhoClicked()), level, true);
                    });
                }
            }
            return;
        }

        if (e.getSlot() == e.getWhoClicked().getInventory().getHeldItemSlot() && (e.getAction().toString().startsWith("PICKUP") || e.getAction().toString().startsWith("PLACE") || e.getAction().toString().startsWith("DROP"))) {
            if (e.getAction().toString().startsWith("PICKUP") || e.getAction().toString().startsWith("DROP")) {
                if (isArmor(e.getCurrentItem()))
                    return;
                getEnchantments(e.getCurrentItem()).forEach((cursedEnchantment, level) -> cursedEnchantment.onRemove(((Player) e.getWhoClicked()), level, true));
            } else {
                if (isArmor(e.getCursor()))
                    return;
                getEnchantments(e.getCursor()).forEach((cursedEnchantment, level) -> cursedEnchantment.onEquip(((Player) e.getWhoClicked()), level, true));
            }
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent e) {
        boolean replaced = false;
        for (Integer integer : e.getRawSlots()) {
            if (integer == e.getWhoClicked().getInventory().getHeldItemSlot())
                replaced = true;
        }
        if (replaced)
            getEnchantments(e.getCursor()).forEach((cursedEnchantment, level) -> cursedEnchantment.onEquip(((Player) e.getWhoClicked()), level, true));
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (this.holdingEnchant.contains(e.getPlayer().getUniqueId().toString()) && e.getPlayer().getItemInHand().getType() == Material.AIR)
            getEnchantments(e.getItemDrop().getItemStack()).forEach((cursedEnchantment, level) -> cursedEnchantment.onRemove(e.getPlayer(), level, true));
    }

    @EventHandler
    public void onPickUp(PlayerPickupItemEvent e) {
        ItemStack itemStack = e.getItem().getItemStack();

        if (isHeld(e.getPlayer())) {
            Map<CursedEnchantment, Integer> enchants = getEnchantments(itemStack);
            enchants.forEach((cursedEnchantment, level) -> cursedEnchantment.onEquip(e.getPlayer(), level, true));
        }
    }

    public ItemStack applyEnchantment(Player player, ItemStack itemStack, CursedEnchantmentType cursedEnchantmentType, int level) {
        ItemMeta meta = itemStack.getItemMeta();
        List<String> lore = new ArrayList<>();
        String enchantLore = cursedEnchantmentType.getLore(level);
        boolean addEnchant = true;
        if (meta.hasLore()) {
            for (String str : meta.getLore()) {
                if (str.contains(cursedEnchantmentType.getDisplayName())) {
                    lore.add(enchantLore);
                    addEnchant = false;
                } else {
                    lore.add(str);
                }
            }
        }

        if (addEnchant) {
            lore.add(enchantLore);
        } else {
            getEnchantments(itemStack).forEach(((cursedEnchantment, integer) -> {
                if (cursedEnchantment.getType() == cursedEnchantmentType) {
                    cursedEnchantment.onRemove(player, integer, !isArmor(itemStack));
                }
            }));
        }

        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        return addGlow(new NBTApi(itemStack).setInt(cursedEnchantmentType.toString(), level).getItemStack());
    }

    public ItemStack applyEnchantment(ItemStack itemStack, CursedEnchantmentType cursedEnchantmentType, int level, String upgrade) {
        ItemMeta meta = itemStack.getItemMeta();
        List<String> lore = new ArrayList<>();
        String enchantLore = cursedEnchantmentType.getLore(level);
        boolean addEnchant = true;
        if (meta.hasLore()) {
            for (String str : meta.getLore()) {
                if (str.contains(cursedEnchantmentType.getDisplayName())) {
                    lore.add(enchantLore + " " + upgrade);
                    addEnchant = false;
                } else {
                    lore.add(str);
                }
            }
        }

        if (addEnchant)
            lore.add(enchantLore + " " + upgrade);

        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        return addGlow(new NBTApi(itemStack).setInt(cursedEnchantmentType.toString(), level).getItemStack());
    }

    public Map<CursedEnchantment, Integer> getEnchantments(ItemStack itemStack) {
        Map<CursedEnchantment, Integer> enchantments = new HashMap<>();
        if (itemStack != null && itemStack.getType() != Material.AIR && itemStack.getItemMeta() != null && itemStack.getItemMeta().hasLore()) {
            itemStack.getItemMeta().getLore().forEach(str -> {
                for (CursedEnchantmentType cursedEnchantmentType : CursedEnchantmentType.values()) {
                    if (str.contains(cursedEnchantmentType.getDisplayName())) {
                        CursedEnchantment cursedEnchantment = this.cursedEnchantments.stream().filter(enchant -> enchant.getType() == cursedEnchantmentType).findAny().orElse(null);
                        if (cursedEnchantment != null)
                            enchantments.put(cursedEnchantment, new NBTApi(itemStack).getInt(cursedEnchantment.getType().toString()));
                    }
                }
            });
        }
        return enchantments;
    }

    public CursedEnchantment getEnchantment(CursedEnchantmentType cursedEnchantmentType) {
        return this.cursedEnchantments.stream().filter(cursedEnchantment -> cursedEnchantment.getType() == cursedEnchantmentType).findAny().orElse(null);
    }

    public void addToEquipped(Player player) {
        if (!this.holdingEnchant.contains(player.getUniqueId().toString()))
            this.holdingEnchant.add(player.getUniqueId().toString());
    }

    public void removeFromEquip(Player player) {
        this.holdingEnchant.remove(player.getUniqueId().toString());
    }

    private boolean isArmor(ItemStack itemStack) {
        String typeNameString = itemStack.getType().name();
        return typeNameString.endsWith("_HELMET")
                || typeNameString.endsWith("_CHESTPLATE")
                || typeNameString.endsWith("_LEGGINGS")
                || typeNameString.endsWith("_BOOTS");
    }

    private boolean isHeld(Player player) {
        if (player.getItemInHand().getType() != Material.AIR)
            return false;
        boolean isHeld = true;
        for (int i = 0; i < player.getInventory().getHeldItemSlot() + 1; i++) {
            ItemStack itemStack = player.getInventory().getItem(i);
            if (i != player.getInventory().getHeldItemSlot() && (itemStack == null || itemStack.getType() == Material.AIR)) {
                isHeld = false;
                break;
            }
        }
        return isHeld;
    }

    private ItemStack addGlow(ItemStack item) {
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = null;
        if (!nmsStack.hasTag()) {
            tag = new NBTTagCompound();
            nmsStack.setTag(tag);
        }
        if (tag == null) tag = nmsStack.getTag();
        NBTTagList ench = new NBTTagList();
        tag.set("ench", ench);
        nmsStack.setTag(tag);
        return CraftItemStack.asCraftMirror(nmsStack);
    }

    private boolean canBeShiftClicked(Player player) {
        if (player.getItemInHand().getType() != Material.AIR)
            return false;
        boolean isHeld = true;
        for (int i = 8; i >= 0; i--) {
            ItemStack itemStack = player.getInventory().getItem(i);
            if (i != player.getInventory().getHeldItemSlot() && (itemStack == null || itemStack.getType() == Material.AIR) && i > player.getInventory().getHeldItemSlot()) {
                isHeld = false;
                break;
            }
        }
        return isHeld;
    }
}
