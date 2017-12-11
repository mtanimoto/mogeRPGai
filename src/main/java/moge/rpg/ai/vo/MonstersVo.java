package moge.rpg.ai.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MonstersVo {

    private List<MonsterVo> monsters;

    public MonstersVo(List<Map<String, Object>> monstersData) {
        monsters = new ArrayList<>();
        for (Map<String, Object> monsterData : monstersData) {
            MonsterVo monster = new MonsterVo(monsterData);
            if (monster.getHp() <= 0) continue;
            monsters.add(monster);
        }
    }

    public int findAttackTarget() {
        return monsters.get(0).getNumber();
    }
}
