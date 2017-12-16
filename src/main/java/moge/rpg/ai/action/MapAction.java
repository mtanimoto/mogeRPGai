package moge.rpg.ai.action;

import moge.rpg.ai.algorithm.MazeShortestAstar;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MapAction implements Action {

    private moge.rpg.ai.vo.Map vo;

    private boolean isUsedHammer;
    private int floorN;

    @Override
    public Action load(Map<String, Object> receiveData) {
        vo = new moge.rpg.ai.vo.Map(receiveData);
        if (floorN < vo.getPlayer().getMapLevel()) {
            floorN = vo.getPlayer().getMapLevel();
            isUsedHammer = false;
        }
        return this;
    }

    @Override
    public String execute() {
        if (vo.getPlayer().needHeal()) return "HEAL";


        // 自分の位置
        Map<String, Integer> myPos = coordinateToMap(
                Arrays.asList(vo.getPlayer().getPosX(), vo.getPlayer().getPosY()));

        // 外壁の位置
        List<Map<String, Integer>> wallsPos = coordinateToMapList(vo.getWalls());
        // 内壁の位置
        List<Map<String, Integer>> blocksPos = coordinateToMapList(vo.getBlocks());

        // X,Yの辺の長さを取得
        int maxX = getMaxX(wallsPos);
        int maxY = getMaxY(wallsPos);

        // ダンジョンを組み立てる
        int[][] dungeon = assembleDungeon(myPos, wallsPos, blocksPos, maxX, maxY);

        // 宝箱の位置
        List<Map<String, Integer>> itemsPos = coordinateToMapList(vo.getItems());

        // 階段の位置
        List<Map<String, Integer>> kaidanPos = coordinateToMapList(vo.getKaidan());

        // 宝箱と階段の座標をまとめる
        List<Map<String, Integer>> searchTargets = Stream.of(itemsPos, kaidanPos).flatMap(p -> p.stream()).collect(Collectors.toList());

        // 自分の位置から宝箱・階段までの最短経路を探索する
        List<MazeShortestAstar> mazeList = new ArrayList<>();
        for (Map<String, Integer> searchPos : searchTargets) {
            int sx = myPos.get("x");
            int sy = myPos.get("y");
            int gx = searchPos.get("x");
            int gy = searchPos.get("y");
            MazeShortestAstar msa = new MazeShortestAstar(maxX, maxY, dungeon, isUsedHammer);
            msa.astar(sx, sy, gx, gy);
            mazeList.add(msa);
        }

        // 探索結果
        // 自分から一番近いところに行く
        mazeList.sort(Comparator.comparingInt(o -> o.pathsSize()));
        isUsedHammer = mazeList.get(0).isUsedHammer();
        return mazeList.get(0).getNextPath();
    }

    /**
     * 座標(x,y)が引数に与えられたリストの中にあるか探す。
     *
     * @param coordinates 座標リスト
     * @param x           見つけたいx軸
     * @param y           見つけたいy軸
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
    private Map<String, Integer> coordinateToMap(List<Integer> coordinates) {
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
    private List<Map<String, Integer>> coordinateToMapList(List<List<Integer>> targets) {
        List<Map<String, Integer>> coordinates = new ArrayList<>();
        targets.forEach(coordinate -> {
            coordinates.add(coordinateToMap(coordinate));
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
     * @param myPos     自分の座標
     * @param wallsPos  外壁の座標
     * @param blocksPos 内壁の座標
     * @param maxX      横の最長
     * @param maxY      縦の最長
     * @return n×mのダンジョン(説明の絵を参照)
     */
    private int[][] assembleDungeon(Map<String, Integer> myPos,
                                    List<Map<String, Integer>> wallsPos, List<Map<String, Integer>> blocksPos, int maxX, int maxY) {
        int[][] grid = new int[maxY][maxX];

        for (int y = 0; y < maxY; y++) {
            for (int x = 0; x < maxX; x++) {
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
