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

    /**
     * A*(A-star, エースター)アルゴリズムでダンジョンを探索する。
     *
     * @param n    横幅(マス数を指定)
     * @param m    縦幅(マス数を指定)
     * @param grid 移動コスト(距離)の記録
     */
    public MazeShortestAstar(int n, int m, int[][] grid) {
        this.n = n;
        this.m = m;

        // ディープコピーして使用
        int[][] dst = new int[grid.length][];
        for (int i = 0; i < grid.length; i++) {
            dst[i] = new int[grid[i].length];
            System.arraycopy(grid[i], 0, dst[i], 0, grid[i].length);
        }
        this.grid = dst;
    }

    //A*(A-star)探索アルゴリズム
    public Queue<String> astar(int sx, int sy, int gx, int gy) {
        Queue<String> path = new ArrayDeque<>(); //移動経路(戻値用);
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
                path = p.path; //移動経路(戻値用)
                break;
            }

            for (int i = 0; i < dx.length; i++) {
                int nx = p.x + dx[i];
                int ny = p.y + dy[i];
                if (nx < 0 || m <= nx || ny < 0 || n <= ny) { //範囲外
                    continue;
                }
                if (grid[ny][nx] > grid[p.y][p.x] + 1) {
                    grid[ny][nx] = grid[p.y][p.x] + 1;

                    Position p2 = new Position(nx, ny);
                    p2.cost = grid[ny][nx]; //移動コスト(スタートからの移動量)
                    p2.estimate = getManhattanDistance(nx, ny, gx, gy) + p2.cost; //推定値
                    p2.path = new ArrayDeque<>(p.path);
                    p2.path.add(dir[i]); //移動経路(移動方向の記録)
                    q.add(p2);
                }
            }
        }

        return path;
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