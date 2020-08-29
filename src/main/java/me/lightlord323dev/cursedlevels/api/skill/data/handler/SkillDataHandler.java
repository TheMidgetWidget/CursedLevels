package me.lightlord323dev.cursedlevels.api.skill.data.handler;

import me.lightlord323dev.cursedlevels.Main;
import me.lightlord323dev.cursedlevels.api.handler.Handler;
import me.lightlord323dev.cursedlevels.api.skill.Skill;
import me.lightlord323dev.cursedlevels.api.skill.data.SkillData;
import me.lightlord323dev.cursedlevels.api.skill.data.skills.MiningData;
import me.lightlord323dev.cursedlevels.util.file.AbstractFile;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Luda on 8/29/2020.
 */
public class SkillDataHandler implements Handler {

    private List<SkillData> skillData;

    private AbstractFile skillFile;

    @Override
    public void onLoad() {
        skillFile = new AbstractFile(Main.getInstance(), "skillgui.yml", true);
        // ALL SKILLS MUST BE REGISTERED HERE
        skillData = Arrays.asList(
                new MiningData()
        );
        skillData.forEach(SkillData::onLoad);
    }

    @Override
    public void onUnload() {}

    public SkillData getSkillData(Skill skill) {
        return this.skillData.stream().filter(skillObject -> skillObject.getSkill().equals(skill)).findAny().orElse(null);
    }

    public AbstractFile getSkillFile() {
        return skillFile;
    }
}
