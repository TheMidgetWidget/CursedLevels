package me.lightlord323dev.cursedlevels.api.skill;

/**
 * Created by Luda on 8/27/2020.
 */
public enum Skill {

    MINING,
    COMBAT,
    FORAGING,
    FISHING,
    FARMING,
    CRAFTSMANSHIP,
    BLACKSMITHING,
    DEFENSE,
    RUNECRAFTING,;


    @Override
    public String toString() {
        String str = super.toString();
        return str.substring(0, 1) + str.substring(1).toLowerCase();
    }
}
