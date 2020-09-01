package me.lightlord323dev.cursedlevels.handler.skill;

import me.lightlord323dev.cursedlevels.Main;
import me.lightlord323dev.cursedlevels.api.skill.Skill;
import me.lightlord323dev.cursedlevels.api.skill.data.skills.BlacksmithingData;
import me.lightlord323dev.cursedlevels.api.user.CursedUser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

/**
 * Created by Luda on 9/1/2020.
 */
public class BlacksmithingHandler extends SkillHandler {

    private BlacksmithingData skillData;

    @Override
    public void onLoad() {
        skillData = (BlacksmithingData) Main.getInstance().getHandlerRegistry().getSkillDataHandler().getSkillData(Skill.BLACKSMITHING);
    }

    @EventHandler
    public void onAnvilUse(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player && e.getClickedInventory() != null && e.getClickedInventory().getType() == InventoryType.ANVIL && e.getCurrentItem() != null && e.getSlot() == 2) {

            Player player = (Player) e.getWhoClicked();

            CursedUser cursedUser = Main.getInstance().getHandlerRegistry().getCursedUserHandler().getCursedUser(player.getUniqueId());

            // TRACKER UPDATE
            cursedUser.setBlacksmithingExp(cursedUser.getBlacksmithingExp() + 1);

            // LEVELUP CHECK
            checkLevelUp(player, cursedUser, cursedUser.getBlocksMined(), skillData);
        }
    }

}
