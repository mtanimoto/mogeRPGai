package moge.rpg.ai;

import java.util.Map;

/**
 * Hello world!
 *
 */
public class App {

    /** テスト用のマップ */
    private static final String readMap = "{\"map\":1,\"player\":{\"hp\":30,\"maxhp\":30,\"str\":30,\"maxstr\":30,\"agi\":30,\"maxagi\":30,\"level\":1,\"exp\":0,\"heal\":2,\"hammer\":5,\"map-level\":1,\"buki\":[\"なし\",0,0,0],\"pos\":{\"x\":3,\"y\":5}},\"blocks\":[[8,9],[8,8],[6,8],[5,8],[4,8],[3,8],[2,8],[6,7],[8,6],[7,6],[6,6],[4,6],[3,6],[2,6],[1,6],[8,5],[6,5],[2,5],[8,4],[6,4],[5,4],[4,4],[3,4],[2,4],[6,3],[9,2],[8,2],[6,2],[5,2],[4,2],[3,2],[2,2]],\"walls\":[[10,10],[9,10],[8,10],[7,10],[6,10],[5,10],[4,10],[3,10],[2,10],[1,10],[0,10],[10,9],[0,9],[10,8],[0,8],[10,7],[0,7],[10,6],[0,6],[10,5],[0,5],[10,4],[0,4],[10,3],[0,3],[10,2],[0,2],[10,1],[0,1],[10,0],[9,0],[8,0],[7,0],[6,0],[5,0],[4,0],[3,0],[2,0],[1,0],[0,0]],\"items\":[[7,5],[1,5],[5,3],[9,1]],\"boss\":[],\"kaidan\":[[9,9]],\"events\":[],\"ha2\":[]}";

    /**
     * メインメソッド
     * 
     * @param args
     *            引数
     */
    public static void main(String[] args) {
        System.out.println("hogehuga");
        try {
            Map<String, Object> jsonMap = JsonParse.fromJsonToMap(readMap);
            if (jsonMap.containsKey("map")) {
                mapAction(jsonMap);
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
    static void mapAction(Map<String, Object> jsonMap) {
        jsonMap.forEach((key, value) -> System.out.println(key + ":" + value));
    }

}
