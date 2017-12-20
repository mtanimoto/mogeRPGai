package moge.rpg.ai.vo;

import java.util.List;

public class Map<S, I extends Number> {

    @SuppressWarnings("unchecked")
    public Map(java.util.Map<String, Object> receiveData) {
        this.map = (int) receiveData.get("map");
        this.blocks = (List<List<Integer>>) receiveData.get("blocks");
        this.walls = (List<List<Integer>>) receiveData.get("walls");
        this.items = (List<List<Integer>>) receiveData.get("items");
        this.kaidan = (List<List<Integer>>) receiveData.get("kaidan");
        this.boss = (List<List<Integer>>) receiveData.get("boss");
        this.events = (List<List<Integer>>) receiveData.get("events");
        this.ha2 = (List<List<Integer>>) receiveData.get("ha2");
        this.player = new Player((java.util.Map<String, Object>) receiveData.get("player"));
    }

    private int map;
    private List<List<Integer>> blocks;
    private List<List<Integer>> walls;
    private List<List<Integer>> items;
    private List<List<Integer>> kaidan;
    private List<List<Integer>> boss;
    private List<List<Integer>> events;
    private List<List<Integer>> ha2;
    private Player player;

    public int getMap() {
        return map;
    }

    public List<List<Integer>> getBlocks() {
        return blocks;
    }

    public List<List<Integer>> getWalls() {
        return walls;
    }

    public List<List<Integer>> getItems() {
        return items;
    }

    public List<List<Integer>> getKaidan() {
        return kaidan;
    }

    public List<List<Integer>> getBoss() {
        return boss;
    }

    public List<List<Integer>> getEvents() {
        return events;
    }

    public List<List<Integer>> getHa2() {
        return ha2;
    }

    public Player getPlayer() {
        return player;
    }
}
