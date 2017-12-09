package moge.rpg.ai.vo;

import java.util.Map;

public class EquipVo {

    @SuppressWarnings("unchecked")
    public EquipVo(Map<String, Object> receiveData) {
        this.now = new WeaponVo((Map<String, Object>) receiveData.get("now"));
        this.discover = new WeaponVo((Map<String, Object>) receiveData.get("discover"));
    }

    private WeaponVo now;
    private WeaponVo discover;

    public WeaponVo getNow() {
        return now;
    }

    public WeaponVo getDiscover() {
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
