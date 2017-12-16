package moge.rpg.ai.algorithm;

import java.util.ArrayDeque;
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

    //マンハッタン距離を求める
    private int getManhattanDistance(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    private final int n;
    private final int m;
    private final int[][] grid;

    private Queue<String> paths = new ArrayDeque<>(); //移動経路(戻値用);
    private Queue<Boolean> useHammers = new ArrayDeque<>();
    private boolean isUsedHammer;

    /**
     * A*(A-star, エースター)アルゴリズムでダンジョンを探索する。
     *
     * @param n    横幅(マス数を指定)
     * @param m    縦幅(マス数を指定)
     * @param grid 移動コスト(距離)の記録
     * @param isUsedHammer 使用済ハンマーか
     */
    public MazeShortestAstar(int n, int m, int[][] grid, boolean isUsedHammer) {
        this.n = n;
        this.m = m;
        this.grid = gridCopy(grid);
        this.isUsedHammer = isUsedHammer;
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
     *
     * @param sx スタート位置(横軸)
     * @param sy スタート位置(縦軸)
     * @param gx ゴール位置(横軸)
     * @param gy ゴール位置(縦軸)
     */
    public void astar(int sx, int sy, int gx, int gy) {

        //A*(A-star) 探索
        Queue<Position> q = new PriorityQueue<>();

        Position p = new Position(sx, sy);
        p.estimate = getManhattanDistance(sx, sy, gx, gy); //推定値
        q.add(p);

        while (!q.isEmpty()) {
            p = q.poll();
            useHammers.poll();
            if (p.cost > grid[p.y][p.x]) {
                continue;
            }
            if (p.y == gy && p.x == gx) { //ゴールに到達
                paths = p.path; //移動経路(戻値用)
                break;
            }

            boolean isShortcut = false;
            for (int i = 0; i < dx.length; i++) {
                int nx = p.x + dx[i];
                int ny = p.y + dy[i];
                if (nx < 0 || m <= nx || ny < 0 || n <= ny) { //範囲外
                    continue;
                }

                // 以下条件の場合、ハンマーを使ってショートカットする
                // ・今いる階でハンマーを使っていない
                // ・自分の位置から目的地まで2回行動で行ける距離
                // ・1回行動目が壁
                isShortcut = isShortcut(p, gx, gy, i, nx, ny);

                if (isShortcut || grid[ny][nx] > grid[p.y][p.x] + 1) {
                    grid[ny][nx] = grid[p.y][p.x] + 1;

                    Position p2 = new Position(nx, ny);
                    p2.cost = grid[ny][nx]; //移動コスト(スタートからの移動量)
                    p2.estimate = getManhattanDistance(nx, ny, gx, gy) + p2.cost; //推定値
                    p2.path = new ArrayDeque<>(p.path);
                    p2.path.add(dir[i]); //移動経路(移動方向の記録)
                    q.add(p2);
                    useHammers.add(isShortcut);
                }
            }
        }
    }

    private boolean isShortcut(Position p, int gx, int gy, int i, int nx, int ny) {
        if (!isUsedHammer) {
            int absXY = (i % 2) == 0 ? Math.abs(p.y - gy) : Math.abs(p.x - gx);
            if (absXY == 2 && grid[ny][nx] == -1) {
                isUsedHammer = true;
                return true;
            }
        }
        return false;
    }

    /**
     * 次の移動方向を取得
     *
     * @return 移動方向
     */
    public String getNextPath() {
        return paths.poll();
    }

    /**
     * 次の行動はハンマーを使うかを判定する
     * ただし、使用済みの場合はtrueを返す
     *
     * @return true：使用する / false:使用しない
     */
    public boolean isUsedHammer() {
        return isUsedHammer || useHammers.poll();
    }

    /**
     * 移動コストを取得する
     *
     * @return 移動コスト
     */
    public int pathsSize() {
        return paths.size();
    }

    //位置情報の構造体
    private class Position implements Comparable<Position> {
        int x; //座標
        int y;
        int cost; //移動コスト(スタートからの移動量)
        int estimate; //推定値(ゴールまでのマンハッタン距離＋移動コスト)
        Queue<String> path = new ArrayDeque<>(); //移動経路(移動方向の記録)

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