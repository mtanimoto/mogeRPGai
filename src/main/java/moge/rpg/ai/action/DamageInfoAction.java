package moge.rpg.ai.action;

import java.util.Map;

public class DamageInfoAction extends BattleAction {

    @Override
    public Action load(Map<String, Object> receiveData) {
        super.load(receiveData);
        return this;
    }

    @Override
    public String execute() {
        // 何もしなくて良いとあるのに何かしなくちゃいけないので無難なコマンドにする
        return "SWING";
    }

}
