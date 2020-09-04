package me.lightlord323dev.cursedlevels.handler.skill;

import me.clip.placeholderapi.PlaceholderAPI;
import me.lightlord323dev.cursedlevels.Main;
import me.lightlord323dev.cursedlevels.api.handler.Handler;
import me.lightlord323dev.cursedlevels.api.skill.data.SkillData;
import me.lightlord323dev.cursedlevels.api.user.CursedUser;
import me.lightlord323dev.cursedlevels.handler.CursedUserHealthHandler;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

/**
 * Created by Luda on 8/31/2020.
 */
public class SkillHandler implements Handler, Listener {

    private String expMsg, levelUpMsg;
    private Sound levelUp, gainExp;

    @Override
    public void onLoad() {
        this.levelUpMsg = Main.getInstance().getHandlerRegistry().getMessageUtil().getMessage("action-bar.level-up");
        this.expMsg = Main.getInstance().getHandlerRegistry().getMessageUtil().getMessage("action-bar.gain-exp");
        this.levelUp = Sound.valueOf(Main.getInstance().getSettingsFile().getConfig().getString("level-up-sound"));
        this.gainExp = Sound.valueOf(Main.getInstance().getSettingsFile().getConfig().getString("exp-gain-sound"));
    }

    @Override
    public void onUnload() {
    }

    protected void checkLevelUp(Player player, CursedUser cursedUser, int currentExp, SkillData skill) {
        int level = cursedUser.getSkillLevel(skill.getSkill()), levelUpExp = skill.getAmtNeededToLevelUp(level);
        if (levelUpExp <= currentExp) {
            if (level >= skill.getLevelCap())
                return;
            cursedUser.addLevel(skill.getSkill());
            String msg = levelUpMsg.replace("%skill%", skill.getSkill().toString()).replace("%level%", (level + 1) + "");
            msg = PlaceholderAPI.setPlaceholders(player, msg);
            cursedUser.setLastSentLevelUp(System.currentTimeMillis());
            CursedUserHealthHandler.sendActionBar(player, msg);
            if (levelUp != null)
                player.playSound(player.getLocation(), levelUp, 1f, 1f);
            String chatMsg = skill.getMessage(level);
            if (chatMsg != null)
                player.sendMessage(chatMsg);
            checkLevelUp(player, cursedUser, currentExp, skill);
        }
    }

    protected void sendExpNotification(Player player, CursedUser cursedUser, int exp, SkillData skill) {
        if (System.currentTimeMillis() - cursedUser.getLastSentLevelUp() >= 3000) {
            cursedUser.setLastSentExp(System.currentTimeMillis());
            int level = cursedUser.getSkillLevel(skill.getSkill()), levelCap = skill.getLevelCap();
            int nextExp;
            if (level < levelCap)
                nextExp = skill.getAmtNeededToLevelUp(level);
            else
                nextExp = skill.getAmtNeededToLevelUp(level - 1);
            CursedUserHealthHandler.sendActionBar(player, expMsg.replace("%exp%", exp + "").replace("%skill%", skill.getSkill().toString()).replace("%currentExp%", cursedUser.getSkillExp(skill.getSkill()) + "").replace("%nextExp%", nextExp + ""));
            if (gainExp != null)
                player.playSound(player.getLocation(), gainExp, 1f, 1f);
        }
    }
}
