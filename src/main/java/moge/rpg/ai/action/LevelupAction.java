package moge.rpg.ai.action;

import moge.rpg.ai.vo.Levelup;

import java.util.Map;

public class LevelupAction implements Action {
    private Levelup vo;

    @Override
    public Action load(Map<String, Object> receiveData) {
        this.vo = new Levelup(receiveData);
        return this;
    }

    @Override
    public String execute() {
        return vo.upWhich();
    }

}
