package moge.rpg.ai.vo;

import java.util.Map;

public class Levelup {

    @SuppressWarnings("unchecked")
    public Levelup(Map<String, Object> receiveData) {
        this.player = new Player((Map<String, Object>) receiveData.get("player"));
    }

    private Player player;

    public Player getPlayer() {
        return player;
    }

    public String upWhich() {
        int hp = player.getMaxhp();
        int agi = player.getMaxagi();

        return agi - hp < 2 ? "AGI" : "HP";
    }
}
