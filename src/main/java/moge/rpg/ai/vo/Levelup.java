package moge.rpg.ai.vo;

import java.util.Map;

public class Levelup {

    private Player player;

    @SuppressWarnings("unchecked")
    public Levelup(Map<String, Object> receiveData) {
        this.player = new Player((Map<String, Object>) receiveData.get("player"));
    }

    public Player getPlayer() {
        return player;
    }

    public String upWhich() {
        int hp = player.getMaxhp();
        int agi = player.getMaxagi();
        int str = player.getStr();

        if (str < hp && str < agi) return "STR";
        return agi - hp < 2 ? "AGI" : "HP";
    }
}
