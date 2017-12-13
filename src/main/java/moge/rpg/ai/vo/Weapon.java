package moge.rpg.ai.vo;

import java.util.Map;

public class Weapon {

    public Weapon(Map<String, Object> receiveData) {
        this.name = (String) receiveData.get("name");
        this.str = (int) receiveData.get("str");
        this.hp = (int) receiveData.get("hp");
        this.agi = (int) receiveData.get("agi");
    }

    private String name;
    private int str;
    private int hp;
    private int agi;

    public String getName() {
        return name;
    }

    public int getStr() {
        return str;
    }

    public int getHp() {
        return hp;
    }

    public int getAgi() {
        return agi;
    }
}
