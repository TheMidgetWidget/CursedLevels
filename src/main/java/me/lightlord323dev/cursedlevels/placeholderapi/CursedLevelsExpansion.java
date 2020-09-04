package me.lightlord323dev.cursedlevels.placeholderapi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.lightlord323dev.cursedlevels.Main;
import me.lightlord323dev.cursedlevels.api.user.CursedUser;
import org.bukkit.entity.Player;

/**
 * Created by Luda on 8/20/2020.
 */
public class CursedLevelsExpansion extends PlaceholderExpansion {

    private Main main;

    public CursedLevelsExpansion(Main main) {
        this.main = main;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getAuthor() {
        return main.getDescription().getAuthors().toString();
    }

    @Override
    public String getIdentifier() {
        return "cursedlevels";
    }

    @Override
    public String getVersion() {
        return main.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {

        if (player == null) {
            return "";
        }

        CursedUser cursedUser = main.getHandlerRegistry().getCursedUserHandler().getCursedUser(player.getUniqueId());

        if (identifier.equals("health")) {
            return String.valueOf(cursedUser.getHealth());
        }

        if (identifier.equals("maxHealth")) {
            return String.valueOf(cursedUser.getMaxHealth());
        }

        if (identifier.equals("regen")) {
            return String.valueOf(cursedUser.getRegen());
        }

        if (identifier.equals("defense")) {
            return String.valueOf(cursedUser.getDefense());
        }

        if (identifier.equals("mana")) {
            return String.valueOf(cursedUser.getMana());
        }

        if (identifier.equals("speed")) {
            return String.valueOf(player.getWalkSpeed());
        }

        if (identifier.equals("luck")) {
            return String.valueOf(Main.getInstance().getEmberPlugin().getItemManager().getLuckLevelOf(player));
        }
        return null;
    }
}
