package moge.rpg.ai.action;

import java.util.Map;

import moge.rpg.ai.vo.EquipVo;

public class EquipAction implements Action {
    private EquipVo vo;

    @Override
    public Action load(Map<String, Object> receiveData) {
        this.vo = new EquipVo(receiveData);
        return this;
    }

    @Override
    public String execute() {
        return vo.isEquip();
    }

}
