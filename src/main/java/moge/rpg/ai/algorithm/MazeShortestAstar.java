package moge.rpg.ai.algorithm;

import moge.rpg.ai.vo.Player;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * ダンジョン探索クラス
 *
 * @see "http://fantom1x.blog130.fc2.com/blog-entry-192.html"
 */
public class MazeShortestAstar {

    //４方向探索用
    private static final int[] dx = {0, 1, 0, -1};
    private static final int[] dy = {-1, 0, 1, 0};
    private static final String[] dir = {"UP", "RIGHT", "DOWN", "LEFT"};
    private final String title;
    private final int[][] grid;
    private Position p;
    private boolean useHammer;
    private Map<String, Integer> myPos;
    private Map<String, Integer> searchPos;
    private Player player;

    /**
     * A*(A-star, エースター)アルゴリズムでダンジョンを探索する。
     *
     * @param title     探索対象(階段とか宝箱とか)
     * @param myPos     自分の現在地
     * @param searchPos 目的地
     * @param grid      移動コスト(距離)の記録
     * @param player    プレイヤーの情報
     * @param useHammer ハンマーを使って探索するか
     */
    public MazeShortestAstar(String title, Map<String, Integer> myPos, Map<String, Integer> searchPos, int[][] grid, Player player, boolean useHammer) {
        this.title = title;
        this.myPos = myPos;
        this.searchPos = searchPos;
        this.grid = gridCopy(grid);
        this.player = player;
        this.useHammer = useHammer;
    }

    public Map<String, Integer> getMyPos() {
        return myPos;
    }

    public Map<String, Integer> getSearchPos() {
        return searchPos;
    }

    //マンハッタン距離を求める
    private int getManhattanDistance(int x, int y) {
        return Math.abs(x - searchPos.get("x")) + Math.abs(y - searchPos.get("y"));
    }

    /**
     * gridをディープコピーする
     *
     * @param grid コピー元
     * @return dst コピー後
     */
    private int[][] gridCopy(int[][] grid) {
        int[][] dst = new int[grid.length][];
        for (int i = 0; i < grid.length; i++) {
            dst[i] = new int[grid[i].length];
            System.arraycopy(grid[i], 0, dst[i], 0, grid[i].length);
        }
        return dst;
    }

    /**
     * A*(A-star)探索アルゴリズム
     */
    public void astar() {
        Queue<Position> q = new PriorityQueue<>();

        Position p = new Position(myPos.get("x"), myPos.get("y"));
        p.estimate = getManhattanDistance(myPos.get("x"), myPos.get("y")); //推定値
        q.add(p);

        int iUseHammer = 0;
        while (!q.isEmpty()) {
            p = q.poll();

            if (p.cost > grid[p.y][p.x]) {
                continue;
            }

            if (p.y == searchPos.get("y") &&
                    p.x == searchPos.get("x")) { //ゴールに到達
                this.p = p;
                break;
            }

            boolean isShortcut = false;
            for (int i = 0; i < dx.length; i++) {
                int nx = p.x + dx[i];
                int ny = p.y + dy[i];
                if (nx < 0 || grid[0].length <= nx || ny < 0 || grid.length <= ny) { //範囲外
                    continue;
                }

                isShortcut = isShortcut(p, nx, ny, iUseHammer);

                if (isShortcut || grid[ny][nx] > grid[p.y][p.x] + 1) {
                    grid[ny][nx] = grid[p.y][p.x] + 1;

                    Position p2 = new Position(nx, ny);
                    p2.cost = grid[ny][nx]; //移動コスト(スタートからの移動量)
                    p2.estimate = getManhattanDistance(nx, ny) + p2.cost; //推定値
                    p2.path.addAll(p.path);
                    p2.path.add(dir[i]); //移動経路(移動方向の記録)
                    p2.useHammer.addAll(p.useHammer);
                    p2.useHammer.add(isShortcut);
                    q.add(p2);
                    if (isShortcut) ++iUseHammer;
                }
            }
        }
    }

    /**
     * ハンマーを使ってショートカットするかを判定する
     *
     * @param p  ポジション
     * @param nx 次に進む予定の横軸座標
     * @param ny 次に進む予定の縦軸座標
     */
    private boolean isShortcut(Position p, int nx, int ny, int useHammer) {

        if (!this.useHammer) return false;
        if (useHammer > 2) return false;
        if (player.getHammer() == 0) return false;

        return true;
    }

    /**
     * 次の移動方向を取得
     *
     * @return 移動方向
     */
    public String getNextPath() {
        return p.path.poll();
    }

    /**
     * 移動コストを取得する
     *
     * @return 移動コスト
     */
    public int pathsSize() {
        return p.estimate;
    }

    /**
     * 探索対象を取得
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    //位置情報の構造体
    private class Position implements Comparable<Position> {
        int x; //座標
        int y;
        int cost; //移動コスト(スタートからの移動量)
        int estimate; //推定値(ゴールまでのマンハッタン距離＋移動コスト)
        Queue<String> path = new ArrayDeque<>(); //移動経路(移動方向の記録)
        Queue<Boolean> useHammer = new ArrayDeque<>();

        //コンストラクタ
        private Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        //比較関数
        @Override
        public int compareTo(Position o) {
            return this.estimate - o.estimate; //推定値で小さい順
        }
    }
}