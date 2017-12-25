package moge.rpg.ai.vo;

import java.util.HashMap;
import java.util.Map;

public class Monster implements Comparable<Monster> {

    private static final Map<String, Integer> priorityMap = new HashMap<>();

    static {
        priorityMap.put("オーク", 1);
        priorityMap.put("ヒドラ", 2);
        priorityMap.put("スライム", 3);
        priorityMap.put("ブリガンド", 4);
        priorityMap.put("メタルヨテイチ", 5);
        priorityMap.put("ハツネツエリア", 6);
        priorityMap.put("もげぞう", 7);
    }

    private String name;
    private int number;
    private int level;
    private int hp;
    private int priority;

    public Monster(Map<String, Object> monsterData) {
        this.name = (String) monsterData.get("name");
        this.number = (int) monsterData.get("number");
        this.level = (int) monsterData.get("level");
        this.hp = (int) monsterData.get("hp");
        this.priority = priorityMap.get(name);
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

    public int getPriority() {
        return priority;
    }

    @Override
    public int compareTo(Monster o) {
        int prioritydiff = getPriority() - o.getPriority();
        if (prioritydiff != 0) {
            return prioritydiff;
        }
        int leveldiff = this.level - o.getLevel();
        if (leveldiff != 0) {
            return leveldiff;
        }
        int hpdiff = this.hp - o.getHp();
        return hpdiff;

    }

    ;
}
