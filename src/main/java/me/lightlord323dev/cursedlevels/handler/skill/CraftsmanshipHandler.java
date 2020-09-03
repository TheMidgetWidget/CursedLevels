package me.lightlord323dev.cursedlevels.handler.skill;

import me.brook.embercore.EmberPlugin;
import me.lightlord323dev.cursedlevels.Main;
import me.lightlord323dev.cursedlevels.api.skill.Skill;
import me.lightlord323dev.cursedlevels.api.skill.data.skills.CraftsmanshipData;
import me.lightlord323dev.cursedlevels.api.user.CursedUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;

/**
 * Created by Luda on 9/2/2020.
 */
public class CraftsmanshipHandler extends SkillHandler {

    private CraftsmanshipData skillData;

    private EmberPlugin emberPlugin;

    @Override
    public void onLoad() {
        super.onLoad();
        skillData = (CraftsmanshipData) Main.getInstance().getHandlerRegistry().getSkillDataHandler().getSkillData(Skill.CRAFTSMANSHIP);
        if (Bukkit.getPluginManager().getPlugin("EmberCore") != null)
            emberPlugin = (EmberPlugin) Bukkit.getPluginManager().getPlugin("EmberCore");
    }

    @EventHandler
    public void onCraft(CraftItemEvent e) {
        Player player = (Player) e.getWhoClicked();

        CursedUser cursedUser = Main.getInstance().getHandlerRegistry().getCursedUserHandler().getCursedUser(player.getUniqueId());

        int exp = 1;

        // TRACKER UPDATE
        cursedUser.setSkillExp(skillData.getSkill(), cursedUser.getSkillExp(skillData.getSkill()) + exp);
        sendExpNotification(player, cursedUser, exp, skillData);

        int level = cursedUser.getSkillLevel(skillData.getSkill());

        // LEVELUP CHECK
        checkLevelUp(player, cursedUser, cursedUser.getSkillExp(skillData.getSkill()), skillData);

        if (cursedUser.getSkillLevel(skillData.getSkill()) > level) {
            emberPlugin.getItemManager().setLuckLevels(player.getUniqueId(), emberPlugin.getItemManager().getLuckLevelOf(player) + (int) skillData.getLuckIncrement(cursedUser.getSkillLevel(skillData.getSkill())));
        }
    }

}
