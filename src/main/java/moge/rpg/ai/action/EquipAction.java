package moge.rpg.ai.action;

import moge.rpg.ai.vo.Equip;

import java.util.Map;

public class EquipAction implements Action {
    private Equip vo;

    @Override
    public Action load(Map<String, Object> receiveData) {
        this.vo = new Equip(receiveData);
        return this;
    }

    @Override
    public String execute() {
        return vo.isEquip();
    }

}
