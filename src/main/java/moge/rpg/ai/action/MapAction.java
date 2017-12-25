package moge.rpg.ai.action;

import moge.rpg.ai.algorithm.MazeShortestAstar;
import moge.rpg.ai.vo.PositionList;

import java.util.*;

public class MapAction implements Action {

    private moge.rpg.ai.vo.Map vo;

    private int floorN;

    @Override
    public Action load(Map<String, Object> receiveData) {
        vo = new moge.rpg.ai.vo.Map(receiveData);
        if (floorN < vo.getPlayer().getMapLevel()) {
            floorN = vo.getPlayer().getMapLevel();
        }
        return this;
    }

    @Override
    public String execute() {
        if (vo.getPlayer().needHeal()) return "HEAL";

        return searchRoute();
    }

    /**
     * 探索する
     *
     * @return 次の移動経路
     */
    private String searchRoute() {
        // ダンジョンを組み立てる
        int[][] dungeon = assembleDungeon();

        // 探索対象の座標をまとめる
        List<PositionList> searchTargets = getRouteCandidate();

        // 自分の位置
        Map<String, Integer> myPos = coordinateToMap("myPos",
                Arrays.asList(vo.getPlayer().getPosX(), vo.getPlayer().getPosY()));

        // 自分の位置から宝箱・階段までの最短経路を探索する
        List<MazeShortestAstar> mazeList = new ArrayList<>();
        for (PositionList<Map<String, Integer>> searchPos2 : searchTargets) {
            String title = searchPos2.getTitle();
            for (Map<String, Integer> searchPos : searchPos2) {
                MazeShortestAstar maze = new MazeShortestAstar(title, myPos, searchPos, dungeon, vo.getPlayer(), false);
                maze.astar();
                mazeList.add(maze);
            }
        }

        // 探索結果
        mazeList.sort(Comparator.comparingInt(o -> o.pathsSize()));
        MazeShortestAstar result = mazeList.get(0);

        MazeShortestAstar maze = new MazeShortestAstar(result.getTitle(), result.getMyPos(), result.getSearchPos(), dungeon, vo.getPlayer(), true);
        maze.astar();
        if (result.pathsSize() - maze.pathsSize() >= 10) {
            result = maze;
        }
        return result.getNextPath();
    }

    private List<PositionList> getRouteCandidate() {
        List<PositionList> posList = new ArrayList<>();

        // ハツネツの位置
        PositionList<Map<String, Integer>> ha2Pos = coordinateToMapList("ha2", vo.getHa2());
        posList.add(ha2Pos);

        // ラスボスの位置
        PositionList<Map<String, Integer>> bossPos = coordinateToMapList("boss", vo.getBoss());
        posList.add(bossPos);

        if (ha2Pos.isEmpty() && bossPos.isEmpty()) {
            if (vo.getPlayer().getMapLevel() > 65) {
                // 階段の位置
                PositionList<Map<String, Integer>> kaidanPos = coordinateToMapList("kaidan", vo.getKaidan());
                posList.add(kaidanPos);
                return posList;
            }

            // 宝箱の位置
            PositionList<Map<String, Integer>> itemsPos = coordinateToMapList("itmes", vo.getItems());
            posList.add(itemsPos);

            // 階段の位置
            PositionList<Map<String, Integer>> kaidanPos = coordinateToMapList("kaidan", vo.getKaidan());
            posList.add(kaidanPos);
        }

        // 宝箱と階段の座標をまとめる
        return posList;
    }


    /**
     * 座標(x,y)が引数に与えられたリストの中にあるか探す。
     *
     * @param coordinates 座標リスト
     * @param x           見つけたいx軸
     * @param y           見つけたいy軸
     * @return true あり / false なし
     */
    private boolean isLooking(PositionList<Map<String, Integer>> coordinates, int x, int y) {
        for (Map<String, Integer> coordinate : coordinates) {
            if (isLookingCoordinate(coordinate, x, y))
                return true;
        }
        return false;
    }

    /**
     * 引数の座標が探している座標(x,y)なのか判定する。
     *
     * @param coordinate 座標
     * @param x          見つけたいx軸
     * @param y          見つけたいy軸
     * @return true 一致する / false 一致しない
     */
    private boolean isLookingCoordinate(Map<String, Integer> coordinate, int x, int y) {
        boolean isX = coordinate.get("x") == x;
        boolean isY = coordinate.get("y") == y;
        return isX && isY;
    }

    /**
     * 座標リストをマップに変換する。
     *
     * @param coordinates 座標リスト(get(0)→x軸、get(1)→y軸)
     * @return マップ(key → x, y)
     */
    private Map<String, Integer> coordinateToMap(String title, List<Integer> coordinates) {
        Map<String, Integer> coordinate = new HashMap<>();
        coordinate.put("x", coordinates.get(0));
        coordinate.put("y", coordinates.get(1));
        return coordinate;
    }

    /**
     * 座標リストをマップに変換し、リストに詰め込み直し返却する。
     *
     * @param targets 座標リストのリスト
     * @return マップリスト
     */
    private PositionList<Map<String, Integer>> coordinateToMapList(String title, List<List<Integer>> targets) {
        PositionList<Map<String, Integer>> coordinates = new PositionList(title);
        targets.forEach(coordinate -> {
            coordinates.add(coordinateToMap(title, coordinate));
        });
        return coordinates;
    }

    /**
     * ダンジョンの状態を配列で表す。
     *
     * <pre>
     * gridの1次元目がy（縦)の情報、2次元目がx(横)の情報
     * </pre>
     *
     * @return n×mのダンジョン(説明の絵を参照)
     */
    private int[][] assembleDungeon() {
        // 自分の位置
        Map<String, Integer> myPos = coordinateToMap("myPos",
                Arrays.asList(vo.getPlayer().getPosX(), vo.getPlayer().getPosY()));

        // 外壁の位置
        PositionList<Map<String, Integer>> wallsPos = coordinateToMapList("walls", vo.getWalls());
        // 内壁の位置
        PositionList<Map<String, Integer>> blocksPos = coordinateToMapList("blocks", vo.getBlocks());

        // X,Yの辺の長さを取得
        int xLength = getMaxX(wallsPos);
        int yLength = getMaxY(wallsPos);

        int[][] grid = new int[yLength][xLength];

        for (int y = 0; y < yLength; y++) {
            for (int x = 0; x < xLength; x++) {
                if (isLookingCoordinate(myPos, x, y)) {
                    grid[y][x] = 0;
                    continue;
                }
                if (isLooking(wallsPos, x, y)) {
                    grid[y][x] = -2;
                    continue;
                }
                if (isLooking(blocksPos, x, y)) {
                    grid[y][x] = -1;
                    continue;
                }
                grid[y][x] = Integer.MAX_VALUE;
            }
        }
        return grid;
    }

    private int getMaxX(List<Map<String, Integer>> wallsPos) {
        Optional<Integer> max = wallsPos.stream().map(pos -> pos.get("x")).max(Comparator.naturalOrder());
        return max.get() + 1;
    }

    private int getMaxY(List<Map<String, Integer>> wallsPos) {
        Optional<Integer> max = wallsPos.stream().map(pos -> pos.get("y")).max(Comparator.naturalOrder());
        return max.get() + 1;
    }

}
