import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TestLeniwyEksperymentator {
    public static void main(String[] args) {
        LeniwyEksperymentator leniwyEksperymentator = new LeniwyEksperymentator();
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        leniwyEksperymentator.fabrykaWatkow(executorService);

        KostkaDoGry kostkaDoGry = new MojaKostka();
        int identyfikator = leniwyEksperymentator.kostkaDoZbadania(kostkaDoGry, 100);

        while (!leniwyEksperymentator.badanieKostkiZakonczono(identyfikator)) {
            // Oczekiwanie na zakończenie badania
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Map<Integer, Integer> histogram = leniwyEksperymentator.histogram(identyfikator);
        System.out.println("Histogram wyników: " + histogram);

        executorService.shutdown();
    }

    // Przykładowa implementacja interfejsu KostkaDoGry
    static class MojaKostka implements KostkaDoGry {
        @Override
        public int rzut() {
            // Symulacja rzutu kostką
            return (int) (Math.random() * 6) + 1;
        }
    }
}
