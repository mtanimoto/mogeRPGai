package moge.rpg.ai.vo;

import java.util.List;
import java.util.Map;

public class Player {
    private int hp;
    private int maxhp;
    private int str;
    private int maxstr;
    private int agi;
    private int maxagi;
    private int level;
    private int exp;
    private int heal;
    private int hammer;
    private int mapLevel;
    private List<String> buki;
    private int posX;
    private int posY;

    @SuppressWarnings("unchecked")
    public Player(Map<String, Object> playerData) {
        this.hp = (int) playerData.get("hp");
        this.maxhp = (int) playerData.get("maxhp");
        this.str = (int) playerData.get("str");
        this.agi = (int) playerData.get("agi");
        this.maxagi = (int) playerData.get("maxagi");
        this.level = (int) playerData.get("level");
        this.exp = (int) playerData.get("exp");
        this.heal = (int) playerData.get("heal");
        this.hammer = (int) playerData.get("hammer");
        this.mapLevel = (int) playerData.get("map-level");
        this.buki = (List<String>) playerData.get("buki");
        Map<String, Object> pos = (Map<String, Object>) playerData.get("pos");
        this.posX = (int) pos.get("x");
        this.posY = (int) pos.get("y");
    }

    public int getHp() {
        return hp;
    }

    public int getMaxhp() {
        return maxhp;
    }

    public int getStr() {
        return str;
    }

    public int getMaxstr() {
        return maxstr;
    }

    public int getAgi() {
        return agi;
    }

    public int getMaxagi() {
        return maxagi;
    }

    public int getLevel() {
        return level;
    }

    public int getExp() {
        return exp;
    }

    public int getHeal() {
        return heal;
    }

    public int getHammer() {
        return hammer;
    }

    public int getMapLevel() {
        return mapLevel;
    }

    public List<String> getBuki() {
        return buki;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public boolean needHeal() {
        if (getHeal() == 0) return false;

        double agiPercent = (double) getAgi() / (double) getMaxagi();
        double hpPercent = (double) getHp() / (double) getMaxhp();
        double strPercent = (double) getStr() / (double) getMaxstr();

        if (hpPercent <= 0.32) return true;
        if (hpPercent <= 0.36 && agiPercent < 0.2) return true;
        if (hpPercent <= 0.36 && strPercent < 0.2) return true;

        return false;
    }

}
