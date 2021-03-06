package moge.rpg.ai;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class App {

    private static final String testMap = "{\"map\":1,\"player\":{\"hp\":30,\"maxhp\":30,\"str\":30,\"maxstr\":30,\"agi\":30,\"maxagi\":30,\"level\":1,\"exp\":0,\"heal\":2,\"hammer\":5,\"map-level\":1,\"buki\":[\"なし\",0,0,0],\"pos\":{\"x\":3,\"y\":5}},\"blocks\":[[8,9],[8,8],[6,8],[5,8],[4,8],[3,8],[2,8],[6,7],[8,6],[7,6],[6,6],[4,6],[3,6],[2,6],[1,6],[8,5],[6,5],[2,5],[8,4],[6,4],[5,4],[4,4],[3,4],[2,4],[6,3],[9,2],[8,2],[6,2],[5,2],[4,2],[3,2],[2,2]],\"walls\":[[10,10],[9,10],[8,10],[7,10],[6,10],[5,10],[4,10],[3,10],[2,10],[1,10],[0,10],[10,9],[0,9],[10,8],[0,8],[10,7],[0,7],[10,6],[0,6],[10,5],[0,5],[10,4],[0,4],[10,3],[0,3],[10,2],[0,2],[10,1],[0,1],[10,0],[9,0],[8,0],[7,0],[6,0],[5,0],[4,0],[3,0],[2,0],[1,0],[0,0]],\"items\":[[7,5],[1,5],[5,3],[9,1]],\"boss\":[],\"kaidan\":[[9,9]],\"events\":[],\"ha2\":[]}\n";
    private static final String test50Map = "{\"map\":1,\"player\":{\"hp\":24,\"maxhp\":44,\"str\":44,\"maxstr\":44,\"agi\":15,\"maxagi\":46,\"level\":12,\"exp\":72,\"heal\":12,\"hammer\":15,\"map-level\":50,\"buki\":[\"さんごのつるぎ\",14,0,4],\"pos\":{\"x\":7,\"y\":11}},\"blocks\":[[10,11],[6,11],[4,11],[16,10],[14,10],[12,10],[11,10],[10,10],[8,10],[7,10],[6,10],[4,10],[2,10],[16,9],[14,9],[12,9],[8,9],[2,9],[16,8],[14,8],[13,8],[12,8],[10,8],[9,8],[8,8],[7,8],[6,8],[5,8],[4,8],[3,8],[2,8],[16,7],[10,7],[2,7],[16,6],[15,6],[14,6],[13,6],[12,6],[11,6],[10,6],[8,6],[6,6],[5,6],[4,6],[2,6],[14,5],[10,5],[8,5],[6,5],[2,5],[17,4],[16,4],[14,4],[12,4],[10,4],[8,4],[6,4],[5,4],[4,4],[2,4],[16,3],[12,3],[10,3],[8,3],[4,3],[16,2],[15,2],[14,2],[13,2],[12,2],[10,2],[9,2],[8,2],[7,2],[6,2],[4,2],[3,2],[2,2],[4,1]],\"walls\":[[18,12],[17,12],[16,12],[15,12],[14,12],[13,12],[12,12],[11,12],[10,12],[9,12],[8,12],[7,12],[6,12],[5,12],[4,12],[3,12],[2,12],[1,12],[0,12],[18,11],[0,11],[18,10],[0,10],[18,9],[0,9],[18,8],[0,8],[18,7],[0,7],[18,6],[0,6],[18,5],[0,5],[18,4],[0,4],[18,3],[0,3],[18,2],[0,2],[18,1],[0,1],[18,0],[17,0],[16,0],[15,0],[14,0],[13,0],[12,0],[11,0],[10,0],[9,0],[8,0],[7,0],[6,0],[5,0],[4,0],[3,0],[2,0],[1,0],[0,0]],\"items\":[[5,11],[13,9],[7,9],[5,5],[17,3],[3,1]],\"boss\":[],\"kaidan\":[[9,3]],\"events\":[],\"ha2\":[[11,11]]}";
    private static final String testBattle = "{\"battle\":1,\"monsters\":[{\"name\":\"オーク\",\"number\":9,\"level\":6,\"hp\":3},{\"name\":\"スライム\",\"number\":8,\"level\":10,\"hp\":18},{\"name\":\"ヒドラ\",\"number\":7,\"level\":3,\"hp\":3},{\"name\":\"ヒドラ\",\"number\":6,\"level\":8,\"hp\":8},{\"name\":\"オーク\",\"number\":5,\"level\":5,\"hp\":13},{\"name\":\"ヒドラ\",\"number\":4,\"level\":5,\"hp\":5},{\"name\":\"オーク\",\"number\":3,\"level\":16,\"hp\":9},{\"name\":\"ヒドラ\",\"number\":2,\"level\":8,\"hp\":8},{\"name\":\"ブリガンド\",\"number\":1,\"level\":4,\"hp\":17},{\"name\":\"ハツネツエリア\",\"number\":0,\"level\":50,\"hp\":220}],\"player\":{\"hp\":34,\"maxhp\":46,\"str\":28,\"maxstr\":52,\"agi\":10,\"maxagi\":47,\"level\":13,\"exp\":86,\"heal\":3,\"hammer\":36,\"map-level\":50,\"buki\":[\"ゲイボルグ\",22,5,5],\"pos\":{\"x\":15,\"y\":7}}}";
    private static final String testBattle2 = "{\"map\":1,\"player\":{\"hp\":61,\"maxhp\":61,\"str\":42,\"maxstr\":42,\"agi\":52,\"maxagi\":63,\"level\":19,\"exp\":127,\"heal\":7,\"hammer\":29,\"map-level\":76,\"buki\":[\"ハツネツの剣\",12,12,12],\"pos\":{\"x\":9,\"y\":9}},\"blocks\":[[9,8],[8,8],[7,8],[6,8],[4,8],[2,8],[1,8],[6,7],[4,7],[8,6],[6,6],[5,6],[4,6],[3,6],[2,6],[8,5],[2,5],[9,4],[8,4],[7,4],[6,4],[5,4],[4,4],[2,4],[6,3],[2,3],[8,2],[6,2],[4,2],[3,2],[2,2],[8,1]],\"walls\":[[10,10],[9,10],[8,10],[7,10],[6,10],[5,10],[4,10],[3,10],[2,10],[1,10],[0,10],[10,9],[0,9],[10,8],[0,8],[10,7],[0,7],[10,6],[0,6],[10,5],[0,5],[10,4],[0,4],[10,3],[0,3],[10,2],[0,2],[10,1],[0,1],[10,0],[9,0],[8,0],[7,0],[6,0],[5,0],[4,0],[3,0],[2,0],[1,0],[0,0]],\"items\":[],\"boss\":[],\"kaidan\":[[5,7]],\"events\":[],\"ha2\":[]}";

    private static final ObjectMapper mapper = new ObjectMapper();

    private static final Logger logger = Logger.getLogger();

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws IOException {
        try {
            System.out.println("つばくろ");

            ActionManager am = new ActionManager();
            while (true) {
                BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));

                while (buffer.ready()) {
                    String line = buffer.readLine();
                    logger.info(line);
                    Map<String, Object> data = mapper.readValue(line, HashMap.class);
                    if (am.load(data)) {
                        String output = am.execute();
                        System.out.println(output);
                    }
                }
            }
//            Map<String, Object> data = mapper.readValue(testBattle2, HashMap.class);
//            am.load(data);
//            String output = am.execute();
//            System.out.println(output);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            logger.close();
            System.exit(0);
        }
    }
}
