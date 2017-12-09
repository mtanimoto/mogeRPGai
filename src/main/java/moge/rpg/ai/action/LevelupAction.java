package moge.rpg.ai.action;

import java.util.Map;

import moge.rpg.ai.vo.LevelupVo;

public class LevelupAction implements Action {
    private LevelupVo vo;

    @Override
    public Action load(Map<String, Object> receiveData) {
        this.vo = new LevelupVo(receiveData);
        return this;
    }

    @Override
    public String execute() {
        return vo.upWhich();
    }

}
