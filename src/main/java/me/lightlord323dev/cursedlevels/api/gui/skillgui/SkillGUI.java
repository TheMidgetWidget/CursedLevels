package me.lightlord323dev.cursedlevels.api.gui.skillgui;

import me.lightlord323dev.cursedlevels.Main;
import me.lightlord323dev.cursedlevels.api.gui.CursedGUI;
import me.lightlord323dev.cursedlevels.api.gui.GUIItem;
import me.lightlord323dev.cursedlevels.api.skill.Skill;
import me.lightlord323dev.cursedlevels.api.skill.data.SkillData;
import me.lightlord323dev.cursedlevels.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Luda on 8/27/2020.
 */
public class SkillGUI extends CursedGUI {

    private String title, ownerUUID;
    private Skill skill;
    private List<GUIItem> controls, items;

    public SkillGUI(Player owner, Skill skill, String title, List<GUIItem> items) {
        super(6, title, false, null, true);
        this.controls = Arrays.asList(
                new GUIItem(new ItemBuilder(Material.SIGN).setDisplayName(ChatColor.GREEN + "UP").build(), 7),
                new GUIItem(new ItemBuilder(Material.SIGN).setDisplayName(ChatColor.RED + "DOWN").build(), 8)
        );
        this.items = items;
        this.title = title;
        SkillData skillData = Main.getInstance().getHandlerRegistry().getSkillDataHandler().getSkillData(skill);
        this.items.forEach(guiItem -> {
            String msg = skillData.getMessage(guiItem.getIndex());
            if (msg != null) {
                String[] lore = msg.split("\\n");
                guiItem.setItemStack(new ItemBuilder(guiItem.getItemStack()).setLore(lore).build());
            }
        });
        this.ownerUUID = owner.getUniqueId().toString();
        Main.getInstance().getHandlerRegistry().getSkillGUIHandler().cacheActiveSkillGUI(this);
        this.skill = skill;
    }

    @Override
    public Inventory getInventory() {
        return getInventory(0);
    }

    public Inventory getInventory(int translation) {

        if (translation < 0)
            return null;

        Inventory inventory = super.getInventory();
        int index = (translation + 1) % 4 <= 2 ? 9 : 17, start = 0;
        boolean line = false, reverse = false;

        // TRANSLATION HANDLING
        for (int i = 1; i <= translation; i++) {
            if (!line) {
                start++;
                line = true;
                if (i != 1)
                    reverse = !reverse;
            } else {
                start += 9;
                line = false;
            }
        }

        // TRANSLATION OUT OF BOUNDS
        if (start >= this.items.size())
            return null;

        if (reverse && line)
            index = 17;

        // ZIGZAG HANDLER
        for (int i = start; i < this.items.size(); i++) {
            if (index >= 54)
                break;
            if (!line) {
                inventory.setItem(index, this.items.get(i).getItemStack());
                index += 9;
                line = true;
                if (i != 0)
                    reverse = !reverse;
            } else {
                inventory.setItem(index, this.items.get(i).getItemStack());
                if (reverse) {
                    if (index % 9 == 0) {
                        line = false;
                        index += 9;
                    } else
                        index--;
                } else {
                    if ((index + 1) % 9 == 0) {
                        line = false;
                        index += 9;
                    } else
                        index++;
                }
            }
        }

//        Main.getInstance().getHandlerRegistry().getSkillDataHandler().getSkillData(skill).getItemStack(Main.getInstance().getHandlerRegistry().getCursedUserHandler().getCursedUser(UUID.fromString(ownerUUID)));

        // CONTROLS
        this.controls.get(0).addIntegerValue("clScroll", (translation - 1));
        this.controls.get(1).addIntegerValue("clScroll", (translation + 1));
        this.controls.forEach(guiItem -> inventory.setItem(guiItem.getIndex(), guiItem.getItemStack()));

        return inventory;
    }

    public GUIItem getSkillLevelItem(int level) {
        return this.items.stream().filter(guiItem -> guiItem.getIndex() == level).findAny().orElse(null);
    }

    @Override
    public String getTitle() {
        return title;
    }

    public String getOwnerUUID() {
        return ownerUUID;
    }

    public Skill getSkill() {
        return skill;
    }
}
