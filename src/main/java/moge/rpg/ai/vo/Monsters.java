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
        // hpが2以下のモンスターが1匹以上いたら、SWINGする
        long hp2orLess = monsters.stream().filter(m -> m.getHp() <= 2).count();
        if (hp2orLess > 0) return "SWING";

        // hpが5以下のモンスターが1匹以上いたら、DOUBLEする
        List<Monster> hp3orLessMonsters = monsters.stream()
                .filter(m -> m.getHp() <= 5).collect(Collectors.toList());
        if (hp3orLessMonsters.size() > 0) {
            int firstNumber = hp3orLessMonsters.get(0).getNumber();
            int secodeNumber = hp3orLessMonsters.size() == 1 ? firstNumber : hp3orLessMonsters.get(1).getNumber();
            return "DOUBLE " + firstNumber + " " + secodeNumber;
        }

        // レベルが高い順にソート
        List<Monster> monstersSorted = monsters.stream()
                .sorted((m1, m2) -> m2.getLevel() - m1.getLevel())
                .collect(Collectors.toList());

        return "STAB " + monstersSorted.get(0).getNumber();
    }
}
