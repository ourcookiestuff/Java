import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

class LeniwyEksperymentator implements LeniwyBadaczKostekDoGry {
    private ExecutorService executorService;
    private Map<Integer, Future<?>> mapaZadan;
    private Map<Integer, Map<Integer, Integer>> mapaWynikow;
    private final AtomicInteger ID = new AtomicInteger(0);

    @Override
    public void fabrykaWatkow(ExecutorService executorService) {
        this.executorService = executorService;
        this.mapaZadan = new ConcurrentHashMap<>();
        this.mapaWynikow = new ConcurrentHashMap<>();
    }

    @Override
    public int kostkaDoZbadania(KostkaDoGry kostka, int liczbaRzutow) {
        int id_watku = ID.incrementAndGet();
        Future<Integer> future = executorService.submit(new Zadanie(kostka, liczbaRzutow, mapaWynikow, id_watku));
        mapaZadan.put(id_watku, future);
        return id_watku;
    }

    @Override
    public boolean badanieKostkiZakonczono(int identyfikator) {
        Future<?> future = mapaZadan.get(identyfikator);
        if (future != null && future.isDone()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Map<Integer, Integer> histogram(int identyfikator) {
        synchronized (mapaWynikow) {
            return mapaWynikow.get(identyfikator);
        }
    }

    private static class Zadanie implements Callable<Integer> {
        private final KostkaDoGry kostka;
        private final int liczbaRzutow;
        private final Map<Integer, Map<Integer, Integer>> mapaWynikow;
        private final int id_watku;

        public Zadanie(KostkaDoGry kostka, int liczbaRzutow, Map<Integer, Map<Integer, Integer>> mapaWynikow, int id_watku) {
            this.kostka = kostka;
            this.liczbaRzutow = liczbaRzutow;
            this.mapaWynikow = mapaWynikow;
            this.id_watku = id_watku;
        }

        @Override
        public Integer call() {
            Map<Integer, Integer> wyniki = new HashMap<>();
            for (int i = 0; i < liczbaRzutow; i++) {
                int roll = kostka.rzut();
                wyniki.put(roll, wyniki.getOrDefault(roll, 0) + 1);
            }

            synchronized (mapaWynikow) {
                mapaWynikow.put(id_watku, wyniki);
                mapaWynikow.notifyAll();
            }

            return id_watku;
        }
    }
}