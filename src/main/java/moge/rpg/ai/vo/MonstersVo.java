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

    public String findAttackTarget() {
        // hpが2以下のモンスターが2匹以上いたら、SWINGする
        long hp2orLess = monsters.stream().filter(m -> m.getHp() <= 2).count();
        if (hp2orLess > 1) return "SWING";

        // hpが6以下のモンスターが2匹以上いたら、DOUBLEする
        List<MonsterVo> hp3orLessMonsters = monsters.stream()
                .filter(m -> m.getHp() <= 6).collect(java.util.stream.Collectors.toList());
        if (hp3orLessMonsters.size() > 1)
            return "DOUBLE " + hp3orLessMonsters.get(0).getNumber() + " " + hp3orLessMonsters.get(1).getNumber();

        return "STAB " + monsters.get(0).getNumber();
    }
}
