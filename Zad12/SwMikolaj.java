import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

class SwMikolaj implements Inwentaryzator {
    Map<String, Integer> mapaWynikow = new HashMap<>();

    private boolean dopuszczalnePole(Field pole) {
        String nazwaPola = pole.getName();
        return pole.getType() == int.class
                && !java.lang.reflect.Modifier.isStatic(pole.getModifiers())
                && java.lang.reflect.Modifier.isPublic(pole.getModifiers())
                && (nazwaPola.equals("bombki") || nazwaPola.equals("lancuchy") || nazwaPola.equals("cukierki")
                || nazwaPola.equals("prezenty") || nazwaPola.equals("szpice") || nazwaPola.equals("lampki"));
    }

    @Override
    public Map<String, Integer> inwentaryzacja(List<String> listaKlas) {
        for (String klasa : listaKlas) {
            try {
                Class<?> c = Class.forName(klasa);
                Field[] pola = c.getDeclaredFields();
                for (Field pole : pola) {
                    if (dopuszczalnePole(pole)) {
                        pole.setAccessible(true);
                        String nazwaPola = pole.getName();
                        int wartosc = (int) pole.get(c.getDeclaredConstructor().newInstance());
                        mapaWynikow.put(nazwaPola, mapaWynikow.getOrDefault(nazwaPola, 0) + wartosc);
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return mapaWynikow;
    }
}

