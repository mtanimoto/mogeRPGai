package moge.rpg.ai.action;

import moge.rpg.ai.vo.Battle;
import moge.rpg.ai.vo.Monsters;

import java.util.Map;

public class BattleAction implements Action {

    private Battle vo;

    @Override
    public Action load(Map<String, Object> receiveData) {
        this.vo = new Battle(receiveData);
        return this;
    }

    @Override
    public String execute() {
        if (vo.getPlayer().needHeal()) return "HEAL";
        Monsters monsters = vo.getMonsters();
        return monsters.findAttackTarget();
    }

}
