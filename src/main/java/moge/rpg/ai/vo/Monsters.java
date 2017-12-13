package moge.rpg.ai.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public String findAttackTarget() {
        // hpが2以下のモンスターが2匹以上いたら、SWINGする
        long hp2orLess = monsters.stream().filter(m -> m.getHp() <= 2).count();
        if (hp2orLess > 1) return "SWING";

        // hpが6以下のモンスターが2匹以上いたら、DOUBLEする
        List<Monster> hp3orLessMonsters = monsters.stream()
                .filter(m -> m.getHp() <= 6).collect(Collectors.toList());
        if (hp3orLessMonsters.size() > 1)
            return "DOUBLE " + hp3orLessMonsters.get(0).getNumber() + " " + hp3orLessMonsters.get(1).getNumber();

        return "STAB " + monsters.get(0).getNumber();
    }
}
