import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class MyMadSet implements MadSet {

    private DistanceMeasure distanceMeasure;
    private double minDistanceAllowed;
    private List<Point> points;

    public MyMadSet() {
        this.points = new ArrayList<>();
    }

    @Override
    public void setDistanceMeasure(DistanceMeasure measure) throws TooCloseException {
        this.distanceMeasure = measure;
        checkMinDistance();
    }

    @Override
    public void setMinDistanceAllowed(double minAllowed) throws TooCloseException {
        this.minDistanceAllowed = minAllowed;
        checkMinDistance();
    }

    @Override
    public void addPoint(Point point) throws TooCloseException {
        checkMinDistance();

        List<Point> removedPoints = new ArrayList<>();

        for (Point existingPoint : points) {
            double distance = distanceMeasure.distance(existingPoint, point);
            if (distance <= minDistanceAllowed) {
                removedPoints.add(existingPoint);
            }
        }

        if (!removedPoints.isEmpty()) {
            points.removeAll(removedPoints);
            removedPoints.add(point); 
            throw new TooCloseException(removedPoints);
        }

        points.add(point);
    }

    @Override
    public List<Point> getPoints() {
        return new ArrayList<>(points);
    }

    @Override
    public List<Point> getSortedPoints(Point referencePoint) {
        DistanceMeasure measure = this.distanceMeasure;
        List<Point> sortedPoints = new ArrayList<>(points);

        for (int i = 0; i < sortedPoints.size() - 1; i++) {
            for (int j = 0; j < sortedPoints.size() - i - 1; j++) {
                double distanceA = measure.distance(referencePoint, sortedPoints.get(j));
                double distanceB = measure.distance(referencePoint, sortedPoints.get(j + 1));

                if (distanceA > distanceB) {
                    Point temp = sortedPoints.get(j);
                    sortedPoints.set(j, sortedPoints.get(j + 1));
                    sortedPoints.set(j + 1, temp);
                }
            }
        }

        return sortedPoints;
    }

    private void checkMinDistance() throws TooCloseException {
        List<Point> removedPoints = new ArrayList<>();

        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                double distance = distanceMeasure.distance(points.get(i), points.get(j));
                if (distance <= minDistanceAllowed) {
                    removedPoints.add(points.get(i));
                    removedPoints.add(points.get(j));
                }
            }
        }

        if (!removedPoints.isEmpty()) {
            points.removeAll(removedPoints);
            throw new TooCloseException(new ArrayList<>(removedPoints));
        }
    }

}