package me.lightlord323dev.cursedlevels.handler.skill;

import me.lightlord323dev.cursedlevels.Main;
import me.lightlord323dev.cursedlevels.api.skill.Skill;
import me.lightlord323dev.cursedlevels.api.skill.data.skills.BlacksmithingData;
import me.lightlord323dev.cursedlevels.api.user.CursedUser;
import org.bukkit.Material;
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
        super.onLoad();
        skillData = (BlacksmithingData) Main.getInstance().getHandlerRegistry().getSkillDataHandler().getSkillData(Skill.BLACKSMITHING);
    }

    @EventHandler
    public void onAnvilUse(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player && e.getClickedInventory() != null && e.getClickedInventory().getType() == InventoryType.ANVIL && e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR && e.getSlot() == 2) {

            Player player = (Player) e.getWhoClicked();

            CursedUser cursedUser = Main.getInstance().getHandlerRegistry().getCursedUserHandler().getCursedUser(player.getUniqueId());

            int exp = 1;

            // TRACKER UPDATE
            cursedUser.setSkillExp(skillData.getSkill(), cursedUser.getSkillExp(skillData.getSkill()) + exp);
            sendExpNotification(player, cursedUser, exp, skillData);

            int prevLevel = cursedUser.getSkillLevel(skillData.getSkill());

            // LEVELUP CHECK
            checkLevelUp(player, cursedUser, cursedUser.getSkillExp(skillData.getSkill()), skillData);
            int level = cursedUser.getSkillLevel(skillData.getSkill());

            if (prevLevel < level) {
                int prevRegen = (int) skillData.getManaRegenAmt(prevLevel);
                cursedUser.setManaregen(cursedUser.getManaregen() - prevRegen + (int) skillData.getManaRegenAmt(level));
            }
        }
    }

}
