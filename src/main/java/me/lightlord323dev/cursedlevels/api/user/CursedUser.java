package me.lightlord323dev.cursedlevels.api.user;

import me.lightlord323dev.cursedlevels.api.skill.CursedSkill;
import me.lightlord323dev.cursedlevels.api.skill.Skill;

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
        for (Skill skill : Skill.values()) {
            skills.add(new CursedSkill(skill));
        }
    }

    /**
     * adds 1 level to the current skill level
     * @param skill
     */
    public void addLevel(Skill skill) {
        CursedSkill cs = this.skills.stream().filter(cursedSkill -> cursedSkill.getSkill() == skill).findAny().orElse(null);
        if (cs != null)
            cs.setLevel(cs.getLevel() + 1);
    }

    /**
     * gets specified skill level for user
     *
     * @param skill
     * @return -1 if skill not found
     */
    public int getSkillLevel(Skill skill) {
        CursedSkill cs = this.skills.stream().filter(cursedSkill -> cursedSkill.getSkill() == skill).findAny().orElse(null);
        if (cs != null)
            return cs.getLevel();
        return -1;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }
}
