public class Point {

    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static int manhattanDistance(Point a, Point b) {
        int distanciaX = Math.abs(a.x - b.x);
        int distanciaY = Math.abs(a.y - b.y);
        return distanciaX + distanciaY;
    }
}