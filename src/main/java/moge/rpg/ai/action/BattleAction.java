package moge.rpg.ai.action;

import moge.rpg.ai.vo.Battle;

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
        return vo.findAttackTarget();
    }

}
