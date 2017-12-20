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
    private final String title;
    private final int n;
    private final int m;
    private final int[][] grid;
    private final int maplevel;
    private Queue<String> paths = new ArrayDeque<>(); //移動経路(戻値用);
    private Queue<Boolean> useHammers = new ArrayDeque<>();
    private boolean isUsedHammer;

    /**
     * A*(A-star, エースター)アルゴリズムでダンジョンを探索する。
     *
     * @param title        探索対象(階段とか宝箱とか)
     * @param n            横幅(マス数を指定)
     * @param m            縦幅(マス数を指定)
     * @param grid         移動コスト(距離)の記録
     * @param isUsedHammer 使用済ハンマーか
     */
    public MazeShortestAstar(String title, int n, int m, int[][] grid, boolean isUsedHammer, int maplevel) {
        this.title = title;
        this.n = n;
        this.m = m;
        this.grid = gridCopy(grid);
        this.isUsedHammer = isUsedHammer;
        this.maplevel = maplevel;
    }

    //マンハッタン距離を求める
    private int getManhattanDistance(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
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
    public void astar(int sx, int sy, int gx, int gy, int hammer, int heal) {

        //A*(A-star) 探索
        Queue<Position> q = new PriorityQueue<>();

        Position p = new Position(sx, sy);
        p.estimate = getManhattanDistance(sx, sy, gx, gy); //推定値
        q.add(p);

        while (!q.isEmpty()) {
            p = q.poll();

            if (p.cost > grid[p.y][p.x]) {
                continue;
            }
            if (p.y == gy && p.x == gx) { //ゴールに到達
                paths = p.path; //移動経路(戻値用)
                useHammers = p.useHammers;
                break;
            }

            boolean isShortcut;
            for (int i = 0; i < dx.length; i++) {
                int nx = p.x + dx[i];
                int ny = p.y + dy[i];
                if (nx < 0 || n <= nx || ny < 0 || m <= ny) { //範囲外
                    continue;
                }

                isShortcut = isShortcut(p, gx, gy, i, nx, ny, hammer);

                if (isShortcut || grid[ny][nx] > grid[p.y][p.x] + 1) {
                    grid[ny][nx] = grid[p.y][p.x] + 1;

                    Position p2 = new Position(nx, ny);
                    p2.cost = grid[ny][nx]; //移動コスト(スタートからの移動量)
                    p2.estimate = getManhattanDistance(nx, ny, gx, gy) + p2.cost; //推定値
                    p2.path.addAll(p.path);
                    p2.path.add(dir[i]); //移動経路(移動方向の記録)
                    p2.useHammers.addAll(p.useHammers);
                    p2.useHammers.add(isShortcut);
                    q.add(p2);

                }
            }
        }
    }

    /**
     * ハンマーを使ってショートカットするかを判定する
     *
     * @param p      ポジション
     * @param gx     目的地の位置(横軸)
     * @param gy     目的地の位置(縦軸)
     * @param i      上下左右どちらに進むかを制御している変数
     * @param nx     次に進む予定の横軸座標
     * @param ny     次に進む予定の縦軸座標
     * @param hammer ハンマーの数
     * @return true:ショートカットする / false:しない
     */
    private boolean isShortcut(Position p, int gx, int gy, int i, int nx, int ny, int hammer) {
        if (!(title.equals("kaidan") && maplevel > 49)) {
            if (isUsedHammer) return false;
        }
        if (hammer == 0) return false;

        int absXY = (i % 2) == 0 ? Math.abs(p.y - gy) : Math.abs(p.x - gx);
        if (absXY == 2 && grid[ny][nx] == -1) {
            isUsedHammer = true;
            return true;
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
        Queue<Boolean> useHammers = new ArrayDeque<>();

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