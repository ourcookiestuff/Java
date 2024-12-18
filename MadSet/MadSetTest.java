import java.util.List;

public class MadSetTest {
    public static void main(String[] args) {
        try {
            testMyMadSet();
        } catch (TooCloseException e) {
            e.printStackTrace();
        }
    }

    private static void testMyMadSet() {
        try {
            MyMadSet madSet = new MyMadSet();
            madSet.setMinDistanceAllowed(2.0);

            madSet.setDistanceMeasure((a, b) -> Math.sqrt(Math.pow(a.x() - b.x(), 2) + Math.pow(a.y() - b.y(), 2)));

            Point point1 = new Point(1, 1);
            Point point2 = new Point(4, 4);
            Point point3 = new Point(5, 5);

            madSet.addPoint(point1);
            printPoints(madSet.getPoints());

            madSet.addPoint(point2);
            printPoints(madSet.getPoints());

            // This should throw TooCloseException
            madSet.addPoint(point3);

            // Since TooCloseException was thrown, this line won't be reached
            printPoints(madSet.getPoints());
        } catch (TooCloseException e) {
            System.out.println("TooCloseException");
            List<Point> removedPoints = e.removePoints();
            System.out.println("Removed Points: " + removedPoints);
        }
    }

    private static void printPoints(List<Point> points) {
        System.out.println("Current Points: " + points);
    }
}
