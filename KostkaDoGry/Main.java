import java.util.*;


public class Main {
    public static void main(String[] args) {
        KostkaDoGry kostka = new KostkaDoGry() {
            @Override
            public int rzut() {
                // Symulacja rzutu kostką, np. generowanie losowej liczby od 1 do 6
                return (int) (Math.random() * 6) + 1;
            }
        };

        Eksperymentator eksperymentator = new WspanialyEksperymentator();

        // Przekaż kostkę do eksperymentatora
        eksperymentator.użyjKostki(kostka);

        // Ustal czas eksperymentu (np. 1000 milisekund)
        eksperymentator.czasJednegoEksperymentu(1000);

        // Przykład obliczenia prawdopodobieństwa sumy oczek dla 3 kostek
        int liczbaKostek = 3;
        Map<Integer, Double> probabilities = eksperymentator.szansaNaWyrzucenieOczek(liczbaKostek);
        System.out.println("Prawdopodobieństwo sumy oczek dla " + liczbaKostek + " kostek:");
        for (Map.Entry<Integer, Double> entry : probabilities.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        // Przykład obliczenia prawdopodobieństwa sekwencji oczek
        List<Integer> sekwencja = Arrays.asList(4, 2, 3);
        double sequenceProbability = eksperymentator.szansaNaWyrzucenieKolejno(sekwencja);
        System.out.println("Prawdopodobieństwo sekwencji oczek " + sekwencja + ": " + sequenceProbability);

        // Przykład obliczenia prawdopodobieństwa sekwencji oczek
        List<Integer> sekwencja2 = Arrays.asList(6, 2, 3);
        double sequenceProbability2 = eksperymentator.szansaNaWyrzucenieKolejno(sekwencja2);
        System.out.println("Prawdopodobieństwo sekwencji oczek " + sekwencja2 + ": " + sequenceProbability2);

        // Przykład obliczenia prawdopodobieństwa sekwencji oczek
        List<Integer> sekwencja3 = Arrays.asList(4, 2, 3);
        double sequenceProbability3 = eksperymentator.szansaNaWyrzucenieKolejno(sekwencja3);
        System.out.println("Prawdopodobieństwo sekwencji oczek " + sekwencja3 + ": " + sequenceProbability3);

        // Przykład obliczenia prawdopodobieństwa wyrzucenia oczek w dowolnej kolejności
        Set<Integer> oczkaSet = new HashSet<>(Arrays.asList(4, 2, 3));
        double anyOrderProbability = eksperymentator.szansaNaWyrzucenieWDowolnejKolejności(oczkaSet);
        System.out.println("Prawdopodobieństwo wyrzucenia oczek w dowolnej kolejności " + oczkaSet + ": " + anyOrderProbability);
    }
}
