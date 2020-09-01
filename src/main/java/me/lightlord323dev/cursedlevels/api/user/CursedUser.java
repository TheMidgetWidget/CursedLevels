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
    private long lastInCombat, lastSentLevelUp;
    private int health, maxHealth;

    // skill trackers
    private int blocksMined, combatExp, foragingExp, fishingExp, farmingExp, blacksmithingExp, defenseExp;

    public CursedUser(UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.skills = new ArrayList<>();
        for (Skill skill : Skill.values()) {
            skills.add(new CursedSkill(skill));
        }
        this.lastInCombat = 0;
        this.lastSentLevelUp = 0;
        this.health = 20;
        this.maxHealth = 20;

        this.blocksMined = 0;
        this.combatExp = 0;
        this.foragingExp = 0;
        this.fishingExp = 0;
        this.farmingExp = 0;
        this.blacksmithingExp = 0;
        this.defenseExp = 0;
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

    public int getBlocksMined() {
        return blocksMined;
    }

    public void setBlocksMined(int blocksMined) {
        this.blocksMined = blocksMined;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        if (health > maxHealth)
            health = maxHealth;
        this.health = health;
    }

    public int getCombatExp() {
        return combatExp;
    }

    public void setCombatExp(int combatExp) {
        this.combatExp = combatExp;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getForagingExp() {
        return foragingExp;
    }

    public void setForagingExp(int foragingExp) {
        this.foragingExp = foragingExp;
    }

    public int getFishingExp() {
        return fishingExp;
    }

    public void setFishingExp(int fishingExp) {
        this.fishingExp = fishingExp;
    }

    public int getFarmingExp() {
        return farmingExp;
    }

    public void setFarmingExp(int farmingExp) {
        this.farmingExp = farmingExp;
    }

    public int getBlacksmithingExp() {
        return blacksmithingExp;
    }

    public void setBlacksmithingExp(int blacksmithingExp) {
        this.blacksmithingExp = blacksmithingExp;
    }

    public int getDefenseExp() {
        return defenseExp;
    }

    public void setDefenseExp(int defenseExp) {
        this.defenseExp = defenseExp;
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
}
