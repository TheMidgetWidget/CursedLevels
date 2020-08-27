package me.lightlord323dev.cursedlevels.api.user;

import me.lightlord323dev.cursedlevels.api.skill.CursedSkill;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Luda on 8/27/2020.
 */
public class CursedUser {

    private UUID uniqueId;
    private List<CursedSkill> skills;

    public CursedUser(UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.skills = new ArrayList<>();
        // TODO add all skills
    }

    public UUID getUniqueId() {
        return uniqueId;
    }
}
