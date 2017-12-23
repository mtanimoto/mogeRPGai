package moge.rpg.ai.vo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Monsters {

    private List<Monster> monsters;

    public Monsters(List<Map<String, Object>> monstersData) {
        monsters = new ArrayList<>();
        for (Map<String, Object> monsterData : monstersData) {
            Monster monster = new Monster(monsterData);
            if (monster.getHp() <= 0) continue;
            monsters.add(monster);
        }
    }

    public Monster getMonster(int i) {
        return monsters.get(i);
    }

    public List<Monster> getAllMonster() {
        Collections.sort(monsters);
        return monsters;
    }
}
