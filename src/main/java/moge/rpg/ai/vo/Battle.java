package moge.rpg.ai.vo;

import java.util.List;
import java.util.Map;

public class Battle {

    private Monsters monsters;
    private Player player;

    @SuppressWarnings("unchecked")
    public Battle(Map<String, Object> receiveData) {
        this.monsters = new Monsters((List<Map<String, Object>>) receiveData.get("monsters"));
        this.player = new Player((Map<String, Object>) receiveData.get("player"));
    }

    public Monsters getMonsters() {
        return monsters;
    }

    public Player getPlayer() {
        return player;
    }

    public String findAttackTarget() {
        List<Monster> monsters = this.monsters.getAllMonster();

        // hpが2以下のモンスターが1匹以上いたら、SWINGする
        int hp2orLess = (int) monsters.stream().filter(m -> m.getHp() <= 2).count();
        if (hp2orLess > 0) return SWING();

        // hpが5以下のモンスターが1匹以上いたら、DOUBLEする
        int hp5orLessCount = (int) monsters.stream().filter(m -> m.getHp() <= 5).count();
        if (monsters.size() > 1 && hp5orLessCount > 0) {
            int firstNumber = -1;
            int secondNumber = -1;
            for (Monster monster : monsters) {
                int hp = monster.getHp();
                if (hp <= 5) {
                    if (firstNumber < 0) firstNumber = monster.getNumber();
                    if (secondNumber < 0) secondNumber = monster.getNumber();
                }
            }
            return DOUBLE(firstNumber, secondNumber);
        }

        // レベルが高い順にソート
        return STUB(monsters.get(0).getNumber());
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
