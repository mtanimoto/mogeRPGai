package moge.rpg.ai.vo;

import java.util.Map;

public class LevelupVo {

    @SuppressWarnings("unchecked")
    public LevelupVo(Map<String, Object> receiveData) {
        //        this.levelup = (int) receiveData.get("levelup");
        this.player = new PlayerVo((Map<String, Object>) receiveData.get("player"));
    }

    //    private int levelup;
    private PlayerVo player;

    //    public int getLevelup() {
    //        return levelup;
    //    }

    public PlayerVo getPlayer() {
        return player;
    }

    public String upWhich() {
        int hp = player.getMaxhp();
        int agi = player.getMaxagi();

        return agi - hp < 2 ? "AGI" : "HP";
    }
}
