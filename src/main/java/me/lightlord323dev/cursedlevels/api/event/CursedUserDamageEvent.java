package me.lightlord323dev.cursedlevels.api.event;

import me.lightlord323dev.cursedlevels.api.user.CursedUser;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Luda on 9/1/2020.
 */
public class CursedUserDamageEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean isCancelled;
    private CursedUser cursedUser;
    private int damage;

    public CursedUserDamageEvent(CursedUser cursedUser, int damage) {
        this.isCancelled = false;
        this.cursedUser = cursedUser;
        this.damage = damage;
    }

    public boolean isCancelled() {
        return this.isCancelled;
    }

    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public CursedUser getCursedUser() {
        return cursedUser;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}
