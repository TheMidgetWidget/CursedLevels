package me.lightlord323dev.cursedlevels.handler;

import me.lightlord323dev.cursedlevels.Main;
import me.lightlord323dev.cursedlevels.api.handler.Handler;
import me.lightlord323dev.cursedlevels.api.user.CursedUser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Created by Luda on 9/4/2020.
 */
public class CustomEffectStrengthHandler implements Handler, Listener {


    @EventHandler
    public void onDmg(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            CursedUser user = Main.getInstance().getHandlerRegistry().getCursedUserHandler().getCursedUser(e.getDamager().getUniqueId());
            e.setDamage(e.getDamage() * user.getStrength());
        }
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onUnload() {

    }
}
