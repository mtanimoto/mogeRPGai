package moge.rpg.ai.action;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

import moge.rpg.ai.algorithm.MazeShortestAstar;
import moge.rpg.ai.vo.MapVo;

public class MapAction implements Action {

    private MapVo vo;

    /** ダンジョンの横幅 */
    private static final int X_LENGTH = 11;

    /** ダンジョンの縦幅 */
    private static final int Y_LENGTH = 11;

    private Queue<String> shortestPath = new ArrayDeque<>();

    @Override
    public Action load(Map<String, Object> receiveData) {
        vo = new MapVo(receiveData);
        return this;
    }

    @Override
    public String execute() {
        if (vo.getPlayer().needHeal()) return "HEAL";
        if (shortestPath.size() > 0) return shortestPath.poll();

        // 自分の位置
        Map<String, Integer> myPos = coordinateToMap(
                Arrays.asList(vo.getPlayer().getPosX(), vo.getPlayer().getPosY()));

        // 目的地
        Map<String, Integer> destination = null;

        // 宝箱の位置
        List<Map<String, Integer>> itemsPos = coordinateToMapList(vo.getItems());
        if (itemsPos.size() == 0) {
            // 階段の位置
            List<List<Integer>> kaidan = vo.getKaidan();
            destination = coordinateToMap(kaidan.get(0));
        } else {
            destination = itemsPos.get(0);
        }

        // 外壁の位置
        List<Map<String, Integer>> wallsPos = coordinateToMapList(vo.getWalls());
        // 内壁の位置
        List<Map<String, Integer>> blocksPos = coordinateToMapList(vo.getBlocks());

        // ダンジョンを組み立てる
        String[] dungeon = assembleDungeon(myPos, destination, wallsPos, blocksPos);

        // 自分の位置から階段までの最短経路を探索する
        MazeShortestAstar msa = new MazeShortestAstar(X_LENGTH, Y_LENGTH, dungeon);
        shortestPath = msa.astar();

        // 探索結果
        return shortestPath.poll();
    }

    /**
     * 座標(x,y)が引数に与えられたリストの中にあるか探す。
     * @param coordinates 座標リスト
     * @param x 見つけたいx軸
     * @param y 見つけたいy軸
     * @return true あり / false なし
     */
    private boolean isLooking(List<Map<String, Integer>> coordinates, int x, int y) {
        for (Map<String, Integer> coordinate : coordinates) {
            if (isLookingCoordinate(coordinate, x, y))
                return true;
        }
        return false;
    }

    /**
     * 引数の座標が探している座標(x,y)なのか判定する。
     * @param coordinate 座標
     * @param x 見つけたいx軸
     * @param y 見つけたいy軸
     * @return true 一致する / false 一致しない
     */
    private boolean isLookingCoordinate(Map<String, Integer> coordinate, int x, int y) {
        boolean isX = coordinate.get("x") == x;
        boolean isY = coordinate.get("y") == y;
        return isX && isY;
    }

    /**
     * 座標リストをマップに変換する。
     * @param coordinates 座標リスト(get(0)→x軸、get(1)→y軸)
     * @return マップ(key→x,y)
     */
    private Map<String, Integer> coordinateToMap(List<Integer> coordinates) {
        Map<String, Integer> coordinate = new HashMap<>();
        coordinate.put("x", coordinates.get(0));
        coordinate.put("y", coordinates.get(1));
        return coordinate;
    }

    /**
     * 座標リストをマップに変換し、リストに詰め込み直し返却する。
     * @param targets 座標リストのリスト
     * @return マップリスト
     */
    private List<Map<String, Integer>> coordinateToMapList(List<List<Integer>> targets) {
        List<Map<String, Integer>> coordinates = new ArrayList<>();
        targets.forEach(coordinate -> {
            coordinates.add(coordinateToMap(coordinate));
        });
        return coordinates;
    }

    /**
     * ダンジョンを組み立てる。
     *
     * <dl>
     *  <dt>
     *    <dd>こんな感じになる。これを1行ずつStringで1行ずつ配列に持っている</dd>
     *  </dt>
     *  <dt><dd>■■■■■■■■■■■</dd></dt>
     *  <dt><dd>■　　　　　　　　　■</dd></dt>
     *  <dt><dd>■　□□□□□　□□■</dd></dt>
     *  <dt><dd>■　　　　　□　　　■</dd></dt>
     *  <dt><dd>■　□□□□□　□　■</dd></dt>
     *  <dt><dd>■　□Ｓ　　□　□　■</dd></dt>
     *  <dt><dd>■□□□□　□□□　■</dd></dt>
     *  <dt><dd>■　　　　　□　　　■</dd></dt>
     *  <dt><dd>■　□□□□□　□　■</dd></dt>
     *  <dt><dd>■　　　　　　　□Ｇ■</dd></dt>
     *  <dt><dd>■■■■■■■■■■■</dd></dt>
     * </dl>
     * @param myPos 自分の座標
     * @param kaidanPos 階段の座標
     * @param wallsPos 外壁の座標
     * @param blocksPos 内壁の座標
     * @return n×mのダンジョン(説明の絵を参照)
     */
    private String[] assembleDungeon(Map<String, Integer> myPos, Map<String, Integer> kaidanPos,
            List<Map<String, Integer>> wallsPos, List<Map<String, Integer>> blocksPos) {
        List<StringBuilder> sbs = new ArrayList<>();
        for (int y = 0; y < Y_LENGTH; y++) {
            StringBuilder sb = new StringBuilder();
            for (int x = 0; x < X_LENGTH; x++) {
                if (isLookingCoordinate(myPos, x, y)) {
                    sb.append("Ｓ");
                    continue;
                }
                if (isLookingCoordinate(kaidanPos, x, y)) {
                    sb.append("Ｇ");
                    continue;
                }
                if (isLooking(wallsPos, x, y)) {
                    sb.append("■");
                    continue;
                }
                if (isLooking(blocksPos, x, y)) {
                    sb.append("□");
                    continue;
                }
                sb.append("　");
            }
            sbs.add(sb);
        }
        //        sbs.forEach(System.out::println);

        List<String> list = sbs.stream().map(sb -> sb.toString()).collect(Collectors.toList());
        return (String[]) list.toArray(new String[list.size()]);
    }

}
