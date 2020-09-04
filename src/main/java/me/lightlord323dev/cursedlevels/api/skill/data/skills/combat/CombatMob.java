package me.lightlord323dev.cursedlevels.api.skill.data.skills.combat;

import org.bukkit.entity.EntityType;

/**
 * Created by Luda on 9/3/2020.
 */
public class CombatMob {

    private EntityType entity;
    private String boss;
    private int exp;

    public CombatMob(String str, boolean boss) {
        String[] arr = str.split(";");
        if (!boss) {
            this.entity = EntityType.valueOf(arr[0]);
            this.exp = Integer.parseInt(arr[1]);
        } else {
            this.boss = arr[0];
            this.exp = Integer.parseInt(arr[1]);
        }
    }

    public EntityType getEntity() {
        return entity;
    }

    public int getExp() {
        return exp;
    }

    public String getBoss() {
        return boss;
    }
}
