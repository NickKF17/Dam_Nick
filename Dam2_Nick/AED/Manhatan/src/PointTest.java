import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PointTest {

    @Test
    void mismoPuntoTieneDistanciaCero() {
        Point a = new Point(3, 4);
        Point b = new Point(3, 4);

        assertEquals(0, Point.manhattanDistance(a, b));
    }

    @Test
    void distanciaHorizontal() {
        Point a = new Point(0, 0);
        Point b = new Point(5, 0);

        assertEquals(5, Point.manhattanDistance(a, b));
    }

    @Test
    void distanciaVertical() {
        Point a = new Point(0, 0);
        Point b = new Point(0, 7);

        assertEquals(7, Point.manhattanDistance(a, b));
    }

    @Test
    void distanciaEnDiagonalSumaAmbosEjes() {
        Point a = new Point(1, 2);
        Point b = new Point(4, 6);

        assertEquals(7, Point.manhattanDistance(a, b));
    }

    @Test
    void funcionaConCoordenadasNegativas() {
        Point a = new Point(-3, -2);
        Point b = new Point(2, 3);

        assertEquals(10, Point.manhattanDistance(a, b));
    }

    @Test
    void laDistanciaEsLaMismaEnAmbosSentidos() {
        Point a = new Point(1, 9);
        Point b = new Point(-4, 3);

        assertEquals(Point.manhattanDistance(a, b), Point.manhattanDistance(b, a));
    }
}