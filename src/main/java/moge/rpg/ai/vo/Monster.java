package moge.rpg.ai.vo;

import java.util.Map;

public class Monster implements Comparable<Monster> {

    private String name;
    private int number;
    private int level;
    private int hp;

    public Monster(Map<String, Object> monsterData) {
        this.name = (String) monsterData.get("name");
        this.number = (int) monsterData.get("number");
        this.level = (int) monsterData.get("level");
        this.hp = (int) monsterData.get("hp");
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public int getLevel() {
        return level;
    }

    public int getHp() {
        return hp;
    }

    @Override
    public int compareTo(Monster o) {
        int leveldiff = this.level - o.getLevel();
        if (leveldiff == 0) {
            int hpdiff = this.hp - o.getHp();
            return hpdiff;
        }
        return leveldiff;
    }
}
