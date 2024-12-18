import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

class WatkowyEksperymentator implements BadaczKostekDoGry{
    private ThreadFactory fabrykaWatkow;
    private int limitWatkow;
    private Map<Integer, Thread> mapaWatkow;
    private Map<Integer, Map<Integer, Integer>> mapaWynikow;
    private final AtomicInteger ID = new AtomicInteger(0);

    public WatkowyEksperymentator() {
        this.mapaWynikow = new ConcurrentHashMap<>();
        this.mapaWatkow = new ConcurrentHashMap<>();
    }

    @Override
    public void dozwolonaLiczbaDzialajacychWatkow(int limitWatkow) {
        this.limitWatkow = limitWatkow;
    }

    @Override
    public void fabrykaWatkow(ThreadFactory fabryka) {
        this.fabrykaWatkow = fabryka;
    }

    @Override
    public int kostkaDoZbadania(KostkaDoGry kostka, int liczbaRzutow) {
        int id_watku = ID.incrementAndGet();
        Runnable zadanie = new Zadanie(kostka, liczbaRzutow, mapaWynikow, id_watku);
        Thread watek = fabrykaWatkow.getThread(zadanie);

        synchronized (mapaWatkow) {
            while (mapaWatkow.size() >= limitWatkow) {
                try {
                    mapaWatkow.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            mapaWatkow.put(id_watku, watek);
            watek.start();
        }
        return id_watku;
    }

    @Override
    public boolean badanieKostkiZakonczono(int identyfikator) {
        synchronized (mapaWatkow) {
            if (!mapaWatkow.containsKey(identyfikator) || !mapaWatkow.get(identyfikator).isAlive()) {
                mapaWatkow.remove(identyfikator);
                mapaWatkow.notifyAll();
                return true;
            }
            return false;
        }
    }

    @Override
    public Map<Integer, Integer> histogram(int identyfikator) {
        synchronized (mapaWynikow) {
            while (!mapaWynikow.containsKey(identyfikator)) {
                try {
                    mapaWynikow.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            return mapaWynikow.get(identyfikator);
        }
    }

    public class Zadanie implements Runnable {
        private KostkaDoGry kostka;
        private int liczbaRzutow;
        private Map<Integer, Map<Integer, Integer>> mapaWynikow;
        private int id_watku;

        public Zadanie(KostkaDoGry kostka, int liczbaRzutow, Map<Integer, Map<Integer, Integer>> mapaWynikow, int id_watku) {
            this.kostka = kostka;
            this.liczbaRzutow = liczbaRzutow;
            this.mapaWynikow = mapaWynikow;
            this.id_watku = id_watku;
        }

        @Override
        public void run() {
            Map<Integer, Integer> wyniki = new HashMap<>();
            for (int i = 0; i < liczbaRzutow; i++) {
                int roll = kostka.rzut();
                wyniki.put(roll, wyniki.getOrDefault(roll, 0) + 1);
            }

            synchronized (mapaWynikow) {
                mapaWynikow.put(id_watku, wyniki);
                mapaWynikow.notifyAll();
            }

            synchronized (mapaWatkow) {
                mapaWatkow.remove(id_watku);
                mapaWatkow.notifyAll();
            }
        }
    }
}
