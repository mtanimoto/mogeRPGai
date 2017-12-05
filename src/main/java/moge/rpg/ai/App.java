package moge.rpg.ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * Hello world!
 *
 */
public class App {

    /** テスト用のダンジョン */
    private static final String testDungeon = "{\"map\":1,\"player\":{\"hp\":30,\"maxhp\":30,\"str\":30,\"maxstr\":30,\"agi\":30,\"maxagi\":30,\"level\":1,\"exp\":0,\"heal\":2,\"hammer\":5,\"map-level\":1,\"buki\":[\"なし\",0,0,0],\"pos\":{\"x\":3,\"y\":5}},\"blocks\":[[8,9],[8,8],[6,8],[5,8],[4,8],[3,8],[2,8],[6,7],[8,6],[7,6],[6,6],[4,6],[3,6],[2,6],[1,6],[8,5],[6,5],[2,5],[8,4],[6,4],[5,4],[4,4],[3,4],[2,4],[6,3],[9,2],[8,2],[6,2],[5,2],[4,2],[3,2],[2,2]],\"walls\":[[10,10],[9,10],[8,10],[7,10],[6,10],[5,10],[4,10],[3,10],[2,10],[1,10],[0,10],[10,9],[0,9],[10,8],[0,8],[10,7],[0,7],[10,6],[0,6],[10,5],[0,5],[10,4],[0,4],[10,3],[0,3],[10,2],[0,2],[10,1],[0,1],[10,0],[9,0],[8,0],[7,0],[6,0],[5,0],[4,0],[3,0],[2,0],[1,0],[0,0]],\"items\":[[7,5],[1,5],[5,3],[9,1]],\"boss\":[],\"kaidan\":[[9,9]],\"events\":[],\"ha2\":[]}";

    /** ダンジョンの横幅 */
    private static final int X_LENGTH = 11;

    /** ダンジョンの縦幅 */
    private static final int Y_LENGTH = 11;

    /**
     * メインメソッド
     *
     * @param args
     *            引数
     */
    public static void main(String[] args) {
        App main = new App();
        System.out.println("hogehuga");
        try {
            Map<String, Object> jsonMap = JsonParse.fromJsonToMap(testDungeon);
            if (jsonMap.containsKey("map")) {
                main.mapAction(jsonMap);
            } else {

            }
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    /**
     * マップアクション
     *
     * @param jsonMap
     */
    @SuppressWarnings("unchecked")
    void mapAction(Map<String, Object> jsonMap) {
        jsonMap.forEach((key, value) -> System.out.println(key + ":" + value));

        // 自分の位置
        Map<String, Integer> myPos = coordinateToMap(Arrays.asList((int) jsonMap.get("x"), (int) jsonMap.get("y")));
        // 階段の位置
        List<List<Integer>> kaidan = (List<List<Integer>>) jsonMap.get("kaidan");
        Map<String, Integer> kaidanPos = coordinateToMap(kaidan.get(0));
        // 外壁の位置
        List<List<Integer>> walls = (List<List<Integer>>) jsonMap.get("walls");
        List<Map<String, Integer>> wallsPos = coordinateToMapList(walls);
        // 内壁の位置
        List<List<Integer>> blocks = (List<List<Integer>>) jsonMap.get("blocks");
        List<Map<String, Integer>> blocksPos = coordinateToMapList(blocks);

        // ダンジョンを組み立てる
        String[] dungeon = assembleDungeon(myPos, kaidanPos, wallsPos, blocksPos);

        // 自分の位置から階段までの最短経路を探索する
        MazeShortestAstar msa = new MazeShortestAstar(X_LENGTH, Y_LENGTH, dungeon);
        Queue<String> shortestPath = msa.astar();

        // 探索結果
        System.out.println(shortestPath);
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
     *  <dt><dd>■　　　　　□　　　■</dd></dt>
     *  <dt><dd>■　□　□□□　□　■</dd></dt>
     *  <dt><dd>■　□　□Ｓ□　□　■</dd></dt>
     *  <dt><dd>■　□　□　□　□　■</dd></dt>
     *  <dt><dd>■　□　□　　　□　■</dd></dt>
     *  <dt><dd>■　□□□□□□□　■</dd></dt>
     *  <dt><dd>■　　　　　□　　　■</dd></dt>
     *  <dt><dd>■　□　□□□　□□■</dd></dt>
     *  <dt><dd>■　□　　　　　　Ｇ■</dd></dt>
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
        for (int x = 0; x < X_LENGTH; x++) {
            StringBuilder sb = new StringBuilder();
            for (int y = 0; y < Y_LENGTH; y++) {
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
        sbs.forEach(System.out::println);

        List<String> list = sbs.stream().map(sb -> sb.toString()).collect(Collectors.toList());
        return (String[]) list.toArray(new String[list.size()]);
    }

}
