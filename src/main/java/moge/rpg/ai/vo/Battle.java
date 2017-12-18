package moge.rpg.ai.vo;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Battle {

    private static final String STAB = "";

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

    /**
     * ソートルール
     */
    private static final Comparator<Monster> SORT_RULE = (m1, m2) -> {
        boolean isBoss = m1.getName().equals("ハツネツ") || m1.getName().equals("もげぞう");
        int level1 = m1.getLevel();
        int level2 = m2.getLevel();
        if (isBoss) level1 -= 100;

        int leveldiff = level2 - level1;
        int hpdiff = m2.getHp() - m1.getHp();
        return leveldiff == 0 ? hpdiff : leveldiff;
    };

    public String findAttackTarget() {
        List<Monster> monster = monsters.getAllMonster();

        // マッチョオークが強すぎるので優先して倒す
        List<Monster> oak = monster.stream().filter(m -> m.getName().equals("オーク"))
                .sorted(SORT_RULE)
                .collect(Collectors.toList());
        if (!oak.isEmpty()) return STUB(oak.get(0).getNumber());

        // hpが2以下のモンスターが1匹以上いたら、SWINGする
        int hp2orLess = (int) monster.stream().filter(m -> m.getHp() <= 2).count();
        if (hp2orLess > 0) return SWING();

        // hpが5以下のモンスターが1匹以上いたら、DOUBLEする
        List<Monster> hp3orLessMonsters = monster.stream()
                .filter(m -> m.getHp() <= 5).collect(Collectors.toList());
        if (hp3orLessMonsters.size() > 0) {
            int firstNumber = hp3orLessMonsters.get(0).getNumber();
            int secondNumber = hp3orLessMonsters.size() == 1 ? firstNumber : hp3orLessMonsters.get(1).getNumber();
            return DOUBLE(firstNumber, secondNumber);
        }

        // レベルが高い順にソート
        List<Monster> monstersSorted = monster.stream()
                .sorted(SORT_RULE)
                .collect(Collectors.toList());

        return STUB(monstersSorted.get(0).getNumber());
    }

    private String STUB(int number) {
        return String.format("STAB %s", String.valueOf(number));
    }

    private String DOUBLE(int number1, int number2) {
        return String.format("DOUBLE %s %s", String.valueOf(number1), String.valueOf(number2));
    }

    private String SWING() {
        return "SWING";
    }

}
