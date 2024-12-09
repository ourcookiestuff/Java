import java.math.BigInteger;
import java.io.*;
import java.net.Socket;
import java.util.Objects;

class Klient implements NetConnection {
    private Socket socket;
    private BufferedReader br;
    private PrintWriter out;
    private BigInteger sum;
    private String wynik;
    private String doPorownania;
    private String haslo;

    @Override
    public void password(String password) {
        this.haslo = password;
    }

    @Override
    public void connect(String host, int port) {
        try {
            socket = new Socket(host, port);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            sum = BigInteger.ZERO;
            String line;

            for (int i = 0; i < 3; i++) {
                line = br.readLine();
                System.out.println(line);
            }

            out.println("Program");
            out.flush();
            System.out.println("Program");

            while ((line = br.readLine()) != null) {
                System.out.println(line);

                if (line.matches("-?\\d+")) {
                    sum = sum.add(new BigInteger(line));
                }

                if (line.equals("EOD")) {
                    BigInteger doDodania = new BigInteger(haslo);
                    sum = sum.add(doDodania);
                    wynik = sum.toString();
                    break;
                }
            }

            while ((line = br.readLine()) != null) {
                System.out.println(line);

                if (line.contains("Mnie wyszło")) {
                    String[] element = line.split(" ");
                    doPorownania = element[2];
                    break;
                }
            }

            if (Objects.equals(doPorownania, wynik)) {
                out.println(wynik);
                out.flush();
                System.out.println(wynik);
            } else {
                out.println(ODPOWIEDZ_DLA_OSZUSTA);
                out.flush();
                System.out.println(ODPOWIEDZ_DLA_OSZUSTA);
            }

            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }

            br.close();
            out.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Klient klient = new Klient();
        klient.password("1000");
        klient.connect("172.30.24.12", 9090);
    }
}
