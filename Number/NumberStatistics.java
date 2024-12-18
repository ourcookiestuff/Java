import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class NumberStatistics implements Statistics {
    private int sideLength;
    private final Map<Integer, Set<Position>> numberPositions = new HashMap<>();

    @Override
    public void sideLength(int length) {
        this.sideLength = length;
    }

    @Override
    public void addNumbers(Map<Integer, Set<Position>> numberPositions) {
        this.numberPositions.putAll(numberPositions);
    }

    @Override
    public Map<Integer, Map<Integer, Integer>> neighbours(Position position, int maxDistanceSquared) {
        Map<Integer, Map<Integer, Integer>> result = new HashMap<>();

        int col1 = position.col();
        int row1 = position.row();

        for (Map.Entry<Integer, Set<Position>> entry : numberPositions.entrySet()) {
            int number = entry.getKey();
            Set<Position> positions = entry.getValue();

            Map<Integer, Integer> distanceCountMap = new HashMap<>();
            for (Position pos : positions) {
                int col2 = pos.col();
                int row2 = pos.row();

                int colDiff = Math.abs(col1 - col2);
                int rowDiff = Math.abs(row1 - row2);
                colDiff = Math.min(colDiff, sideLength - colDiff);
                rowDiff = Math.min(rowDiff, sideLength - rowDiff);

                int distanceSquared = colDiff * colDiff + rowDiff * rowDiff;

                if (distanceSquared <= maxDistanceSquared) {
                    distanceCountMap.put(distanceSquared, distanceCountMap.getOrDefault(distanceSquared, 0) + 1);
                }
            }

            result.put(number, distanceCountMap);
        }

        return result;
    }


}
