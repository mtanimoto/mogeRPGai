package moge.rpg.ai;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import moge.rpg.ai.action.Action;
import moge.rpg.ai.action.BattleAction;
import moge.rpg.ai.action.DamageInfoAction;
import moge.rpg.ai.action.EquipAction;
import moge.rpg.ai.action.LevelupAction;
import moge.rpg.ai.action.MapAction;

public class ActionManager {

    private Action action;

    private Map<String, Action> ma = new ConcurrentHashMap<String, Action>() {
        {
            put("map", new MapAction());
            put("battle", new BattleAction());
            put("damage-info", new DamageInfoAction());
            put("equip", new EquipAction());
            put("levelup", new LevelupAction());
        }
    };

    private String nowKey;

    void load(Map<String, Object> receiveData) {

        boolean canSearch = false;
        canSearch = canSearch || searchKey(receiveData, "map");
        canSearch = canSearch || searchKey(receiveData, "battle");
        canSearch = canSearch || searchKey(receiveData, "damage-info");
        canSearch = canSearch || searchKey(receiveData, "equip");
        canSearch = canSearch || searchKey(receiveData, "levelup");

        if (canSearch) {
            action.load(receiveData);
            return;
        }
        throw new RuntimeException("想定外のJSONを受信しました");
    }

    String execute() {
        String ret = action.execute();
        ma.put(nowKey, action);
        return ret;
    }

    private boolean searchKey(Map<String, Object> receiveData, String key) {
        if (receiveData.containsKey(key)) {
            action = ma.get(key);
            nowKey = key;
            return true;
        }
        return false;
    }
}
