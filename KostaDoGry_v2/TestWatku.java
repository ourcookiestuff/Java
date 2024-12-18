import java.util.Map;

public class TestWatku {
    public static void main(String[] args) {
        // Tworzenie instancji WatkowyEksperymentator
        WatkowyEksperymentator eksperymentator = new WatkowyEksperymentator();

        // Ustalenie liczby wątków
        eksperymentator.dozwolonaLiczbaDzialajacychWatkow(2); // Możesz dostosować tę liczbę do swoich potrzeb

        // Przygotowanie fabryki wątków (prosta implementacja)
        ThreadFactory fabrykaWatkow = run -> new Thread(run);
        eksperymentator.fabrykaWatkow(fabrykaWatkow);

        // Przygotowanie kostki do gry (prosta implementacja)
        KostkaDoGry kostka = () -> (int) (Math.random() * 6) + 1;

        // Rzucanie kostką i badanie wyników
        int taskId1 = eksperymentator.kostkaDoZbadania(kostka, 10000);
        int taskId2 = eksperymentator.kostkaDoZbadania(kostka, 5000);

        // Oczekiwanie na zakończenie badań
        while (!eksperymentator.badanieKostkiZakonczono(taskId1) || !eksperymentator.badanieKostkiZakonczono(taskId2)) {
            // Czekamy, aż wszystkie wątki zakończą pracę
        }

        // Pobieranie wyników
        Map<Integer, Integer> histogram1 = eksperymentator.histogram(taskId1);
        Map<Integer, Integer> histogram2 = eksperymentator.histogram(taskId2);

        // Wyświetlanie wyników
        System.out.println("Histogram dla zadania 1:");
        System.out.println(histogram1);

        System.out.println("Histogram dla zadania 2:");
        System.out.println(histogram2);
    }
}
