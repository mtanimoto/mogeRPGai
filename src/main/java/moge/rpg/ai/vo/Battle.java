package moge.rpg.ai.vo;

import java.util.List;
import java.util.Map;

public class Battle {

    @SuppressWarnings("unchecked")
    public Battle(Map<String, Object> receiveData) {
        this.monsters = new Monsters((List<Map<String, Object>>) receiveData.get("monsters"));
        this.player = new Player((Map<String, Object>) receiveData.get("player"));
    }

    private Monsters monsters;
    private Player player;

    public Monsters getMonsters() {
        return monsters;
    }

    public Player getPlayer() {
        return player;
    }

}
