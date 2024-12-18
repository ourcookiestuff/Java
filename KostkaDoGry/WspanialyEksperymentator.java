import java.util.*;

class WspanialyEksperymentator implements Eksperymentator{
    private KostkaDoGry kostka;
    private long czasEksperymentu;

    @Override
    public void użyjKostki(KostkaDoGry kostka){
        this.kostka = kostka;
    }

    @Override
    public void czasJednegoEksperymentu(long czasEksperymentu){
        this.czasEksperymentu = czasEksperymentu;
    }

    @Override
    public Map<Integer, Double> szansaNaWyrzucenieOczek(int liczbaKostek) {
        Map<Integer, Double> probabilities = new HashMap<>();
        int totalExperiments = 0;
        long startTime = System.currentTimeMillis();

        while (System.currentTimeMillis() - startTime < czasEksperymentu) {
            int sum = 0;
            for (int i = 0; i < liczbaKostek; i++) {
                sum += kostka.rzut();
            }
            probabilities.put(sum, probabilities.getOrDefault(sum, 0.0) + 1);
            totalExperiments++;
        }

        for (Map.Entry<Integer, Double> entry : probabilities.entrySet()) {
            int sum = entry.getKey();
            double count = entry.getValue();
            double probability = count / totalExperiments;
            probabilities.put(sum, probability);
        }

        return probabilities;
    }

    @Override
    public double szansaNaWyrzucenieKolejno(List<Integer> sekwencja) {
        int totalCount = 0;
        int successCount = 0;
        long startTime = System.currentTimeMillis();
        List<Integer> results = new ArrayList<>();

        while (System.currentTimeMillis() - startTime < czasEksperymentu) {
            results.clear();
            for (int i = 0; i < sekwencja.size(); i++) {
                int wynik = kostka.rzut();
                results.add(wynik);
            }

            boolean success = true;
            for (int i = 0; i < sekwencja.size(); i++) {
                if (!results.get(i).equals(sekwencja.get(i))) {
                    success = false;
                    break;
                }
            }

            if (success) {
                successCount++;
            }

            totalCount++;
        }

        return (double) successCount / totalCount;

    }

    @Override
    public double szansaNaWyrzucenieWDowolnejKolejności(Set<Integer> oczka){
        int totalCount = 0;
        int successCount = 0;
        long startTime = System.currentTimeMillis();

        while (System.currentTimeMillis() - startTime < czasEksperymentu){
            List<Integer> results = new ArrayList<>();
            for (int i = 0; i < oczka.size(); i++){
                results.add(kostka.rzut());
            }

            boolean success = true;
            for (Integer oczko : oczka){
                if(!results.contains(oczko)){
                    success = false;
                    break;
                }
            }

            if (success){
                successCount++;
            }

            totalCount++;
        }

        return (double) successCount/totalCount;
    }

}

