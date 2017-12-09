package moge.rpg.ai.action;

import java.util.Map;

import moge.rpg.ai.vo.BattleVo;
import moge.rpg.ai.vo.MonstersVo;

public class BattleAction implements Action {

    private BattleVo vo;

    @Override
    public Action load(Map<String, Object> receiveData) {
        this.vo = new BattleVo(receiveData);
        return this;
    }

    @Override
    public String execute() {
        if (vo.getPlayer().needHeal()) return "HEAL";
        MonstersVo monsters = vo.getMonsters();
        return "STAB " + monsters.findAttackTarger();
    }

}