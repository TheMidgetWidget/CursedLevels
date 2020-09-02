package me.lightlord323dev.cursedlevels.api.skill;

/**
 * Created by Luda on 8/27/2020.
 */
public class CursedSkill {

    private Skill skill;
    private int level;
    private int exp;

    public CursedSkill(Skill skill) {
        this.skill = skill;
        this.level = 0;
        this.exp = 0;
    }

    public Skill getSkill() {
        return skill;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }
}
