package moge.rpg.ai.vo;

import java.util.Map;

public class Equip {

    @SuppressWarnings("unchecked")
    public Equip(Map<String, Object> receiveData) {
        this.now = new Weapon((Map<String, Object>) receiveData.get("now"));
        this.discover = new Weapon((Map<String, Object>) receiveData.get("discover"));
    }

    private Weapon now;
    private Weapon discover;

    public Weapon getNow() {
        return now;
    }

    public Weapon getDiscover() {
        return discover;
    }

    public String isEquip() {
        int strdiff = now.getStr() - discover.getStr();
        int hpdiff = now.getHp() - discover.getHp();
        int agidiff = now.getAgi() - discover.getAgi();

        // 全部足した値が同等以上であれば装備する
        int sum = strdiff + hpdiff + agidiff;

        return sum < 0 ? "YES" : "NO";
    }
}
