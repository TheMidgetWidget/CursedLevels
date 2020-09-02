package me.lightlord323dev.cursedlevels.handler;

import com.google.gson.reflect.TypeToken;
import me.lightlord323dev.cursedlevels.Main;
import me.lightlord323dev.cursedlevels.api.handler.Handler;
import me.lightlord323dev.cursedlevels.api.user.CursedUser;
import me.lightlord323dev.cursedlevels.util.file.AbstractFile;
import me.lightlord323dev.cursedlevels.util.file.GsonUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by Luda on 8/31/2020.
 */
public class CursedUserHandler implements Handler, Listener {

    private List<CursedUser> cursedUsers;

    @Override
    public void onLoad() {
        this.cursedUsers = new ArrayList<>();
        int min = Main.getInstance().getSettingsFile().getConfig().getInt("auto-save-timer");
        Main.getInstance().getExecutorService().scheduleAtFixedRate(() -> saveUserData(), min, min, TimeUnit.MINUTES);
    }

    @Override
    public void onUnload() {
        saveUserData();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Main.getInstance().getExecutorService().schedule(() -> {
            AbstractFile abstractFile = new AbstractFile(Main.getInstance(), e.getPlayer().getUniqueId().toString() + ".json", "users", false, false);
            if (!abstractFile.getFile().exists()) {
                cursedUsers.add(new CursedUser(e.getPlayer().getUniqueId()));
            } else {
                cursedUsers.add(GsonUtil.loadObject(new TypeToken<CursedUser>() {
                }, abstractFile.getFile()));
            }
        }, 0, TimeUnit.MILLISECONDS);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Main.getInstance().getExecutorService().schedule(() -> {
            AbstractFile abstractFile = new AbstractFile(Main.getInstance(), e.getPlayer().getUniqueId().toString() + ".json", "users", false);
            CursedUser cursedUser = getCursedUser(e.getPlayer().getUniqueId());
            if (cursedUser != null) {
                GsonUtil.saveObject(cursedUser, abstractFile.getFile());
                cursedUsers.remove(cursedUser);
            }
        }, 0, TimeUnit.MILLISECONDS);
    }

    public CursedUser getCursedUser(UUID uuid) {
        return cursedUsers.stream().filter(cursedUser -> cursedUser.getUniqueId().toString().equalsIgnoreCase(uuid.toString())).findAny().orElse(null);
    }

    private void saveUserData() {
        cursedUsers.forEach(cursedUser -> GsonUtil.saveObject(cursedUser, new AbstractFile(Main.getInstance(), cursedUser.getUniqueId().toString() + ".json", "users", false).getFile()));
    }

}
