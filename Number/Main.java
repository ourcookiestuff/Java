import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        NumberStatistics statistics = new NumberStatistics();

        // Ustaw długość boku płaszczyzny
        statistics.sideLength(10);

        // Przykładowe pozycje liczb
        Map<Integer, Set<Position>> numberPositions = new HashMap<>();
        Set<Position> positions = new HashSet<>();
        positions.add(new Position(2, 3));
        positions.add(new Position(3, 5));
        numberPositions.put(1, positions);

        // Dodaj liczby do analizy
        statistics.addNumbers(numberPositions);

        // Wykonaj analizę sąsiedztwa dla wybranej pozycji
        Position positionToAnalyze = new Position(2, 3);
        int maxDistanceSquared = 8;
        Map<Integer, Map<Integer, Integer>> result = statistics.neighbours(positionToAnalyze, maxDistanceSquared);

        // Wyświetl wyniki
        for (Map.Entry<Integer, Map<Integer, Integer>> entry : result.entrySet()) {
            int number = entry.getKey();
            Map<Integer, Integer> distanceCountMap = entry.getValue();
            System.out.println("Number " + number + " results: " + distanceCountMap);
        }
    }
}
