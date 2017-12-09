package moge.rpg.ai.vo;

import java.util.List;
import java.util.Map;

public class BattleVo {

    @SuppressWarnings("unchecked")
    public BattleVo(Map<String, Object> receiveData) {
        //        this.battle = (int) receiveData.get("battle");
        this.monsters = new MonstersVo((List<Map<String, Object>>) receiveData.get("monsters"));
        this.player = new PlayerVo((Map<String, Object>) receiveData.get("player"));
    }

    //    private int battle;
    private MonstersVo monsters;
    private PlayerVo player;

    //    public int getBattle() {
    //        return battle;
    //    }

    public MonstersVo getMonsters() {
        return monsters;
    }

    public PlayerVo getPlayer() {
        return player;
    }

}
