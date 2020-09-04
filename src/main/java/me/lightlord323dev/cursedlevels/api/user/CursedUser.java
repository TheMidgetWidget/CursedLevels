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
    private long lastInCombat, lastSentLevelUp, lastSentExp;
    private int health, maxHealth;

    // gained effects
    private int defense, mana, regen;
    private double strength;

    public CursedUser(UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.skills = new ArrayList<>();
        for (Skill skill : Skill.values()) {
            skills.add(new CursedSkill(skill));
        }
        this.lastInCombat = 0;
        this.lastSentLevelUp = 0;
        this.lastSentExp = 0;
        this.health = 20;
        this.maxHealth = 20;

        // gained effects
        this.defense = 0;
        this.mana = 0;
        this.regen = 0;
        this.strength = 1;
    }

    /**
     * adds 1 level to the current skill level
     *
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

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        if (health > maxHealth)
            health = maxHealth;
        this.health = health;
    }

    public int getSkillExp(Skill skill) {
        CursedSkill cs = this.skills.stream().filter(cursedSkill -> cursedSkill.getSkill() == skill).findAny().orElse(null);
        if (cs != null)
            return cs.getExp();
        return -1;
    }

    public void setSkillExp(Skill skill, int exp) {
        CursedSkill cs = this.skills.stream().filter(cursedSkill -> cursedSkill.getSkill() == skill).findAny().orElse(null);
        if (cs != null)
            cs.setExp(exp);
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public long getLastInCombat() {
        return lastInCombat;
    }

    public void setLastInCombat(long lastInCombat) {
        this.lastInCombat = lastInCombat;
    }

    public long getLastSentLevelUp() {
        return lastSentLevelUp;
    }

    public void setLastSentLevelUp(long lastSentLevelUp) {
        this.lastSentLevelUp = lastSentLevelUp;
    }

    public long getLastSentExp() {
        return lastSentExp;
    }

    public void setLastSentExp(long lastSentExp) {
        this.lastSentExp = lastSentExp;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getRegen() {
        return regen;
    }

    public void setRegen(int regen) {
        this.regen = regen;
    }

    public double getStrength() {
        return strength;
    }

    public void setStrength(double strength) {
        this.strength = strength;
    }
}
