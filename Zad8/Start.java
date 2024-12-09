import javax.swing.*;
import java.awt.*;
import java.io.*;

class Start extends JFrame {
    private double[][] punkty;
    private double a = 0;
    private double b = 0;
    private final int margines = 50;
    private boolean dataLoaded = false;
    int rozmiar = 0;
    private JLabel labelA;
    private JLabel labelB;

    private void obliczRegresje() {
        double sumaX = 0, sumaY = 0, sumaXY = 0, sumaXX = 0;

        for (int i = 0; i < rozmiar; i++) {
            sumaX += punkty[i][0];
            sumaY += punkty[i][1];
            sumaXY += punkty[i][0] * punkty[i][1];
            sumaXX += punkty[i][0] * punkty[i][0];
        }
        double W = rozmiar * sumaXX - sumaX * sumaX;
        b = (sumaXX * sumaY - sumaX * sumaXY) / W;
        a = (rozmiar * sumaXY - sumaX * sumaY) / W;
    }

    public Start() {
        setTitle("Aplikacja do wyliczania regresji liniowej");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JButton przycisk = new JButton("Załaduj dane");
        przycisk.addActionListener(e -> zaladowanieDanych());

        JPanel panelPrzycisku = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelPrzycisku.add(przycisk);
        add(panelPrzycisku, BorderLayout.SOUTH);
        labelA = new JLabel("a = ");
        labelB = new JLabel("b = ");
        panelPrzycisku.add(labelA);
        panelPrzycisku.add(labelB);
        add(panelPrzycisku, BorderLayout.SOUTH);

        add(new GraphPanel(), BorderLayout.CENTER);
    }

    private void zaladowanieDanych() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int powrot = fileChooser.showOpenDialog(this);
        if (powrot == JFileChooser.APPROVE_OPTION) {
            File wybranyPlik = fileChooser.getSelectedFile();
            dataLoaded = true;
            try (BufferedReader reader = new BufferedReader(new FileReader(wybranyPlik))) {
                String line;
                line = reader.readLine();
                rozmiar = Integer.parseInt(line);
                punkty = new double[rozmiar][2];
                for (int i = 0; i < rozmiar; i++) {
                    line = reader.readLine();
                    String[] element = line.split("\\s+");
                    punkty[i][0] = Double.parseDouble(element[0]);
                    punkty[i][1] = Double.parseDouble(element[1]);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (dataLoaded) {
                obliczRegresje();
                repaint();
            }
        }
    }

    private class GraphPanel extends JPanel {
        private double xMin, xMax, yMin, yMax;

        // Metoda do rysowania strzałki
        private void strzalka(Graphics2D g2d, int x1, int y1, int x2, int y2) {

            int wielkosc = 10;
            int dx = x2 - x1, dy = y2 - y1;
            double D = Math.sqrt(dx*dx + dy*dy);
            double xm = D - wielkosc, xn = xm, ym = wielkosc, yn = -wielkosc, x;
            double sin = dy / D, cos = dx / D;

            x = xm*cos - ym*sin + x1;
            ym = xm*sin + ym*cos + y1;
            xm = x;

            x = xn*cos - yn*sin + x1;
            yn = xn*sin + yn*cos + y1;
            xn = x;

            int[] xpoints = {x2, (int) xm, (int) xn};
            int[] ypoints = {y2, (int) ym, (int) yn};

            g2d.fillPolygon(xpoints, ypoints, 3);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            xMin = Double.POSITIVE_INFINITY;
            xMax = Double.NEGATIVE_INFINITY;
            yMin = Double.POSITIVE_INFINITY;
            yMax = Double.NEGATIVE_INFINITY;

            for (int i = 0; i < rozmiar; i++) {
                xMin = Math.min(xMin, punkty[i][0]);
                xMax = Math.max(xMax, punkty[i][0]);
                yMin = Math.min(yMin, punkty[i][1]);
                yMax = Math.max(yMax, punkty[i][1]);
            }

            double xMargin = (xMax - xMin) * 0.2;
            double yMargin = (yMax - yMin) * 0.2;
            xMin -= xMargin;
            xMax += xMargin;
            yMin -= yMargin;
            yMax += yMargin;

            int szerokosc = getWidth();
            int wysokosc = getHeight();

            double skalaX = (szerokosc - 2 * margines) / (xMax - xMin);
            double skalaY = (wysokosc - 2 * margines) / (yMax - yMin);

            int OX = margines + (int)((-xMin) * skalaX);
            int OY = margines + (int)((yMax) * skalaY);

            if (dataLoaded) {
                // Rysowanie osi
                g2d.drawLine(margines, OY, szerokosc - margines, OY); // X
                g2d.drawLine(OX, margines, OX, wysokosc - margines); // Y

                // Strzałka dla osi X
                strzalka(g2d, OX, OY, szerokosc - margines, OY);

                // Strzałka dla osi Y
                strzalka(g2d, OX, OY, OX, margines);

                // Punkty
                g2d.setColor(Color.GREEN);
                FontMetrics fm = g2d.getFontMetrics();
                for (int i = 0; i < rozmiar; i++) {
                    int x = margines + (int)((punkty[i][0] - xMin) * skalaX);
                    int y = wysokosc - margines - (int)((punkty[i][1] - yMin) * skalaY);
                    g2d.fillOval(x - 3, y - 3, 6, 6);

                    String label = String.format("(%.2f, %.2f)", punkty[i][0], punkty[i][1]);
                    int labelWidth = fm.stringWidth(label);
                    int labelX = x - labelWidth / 2;
                    int labelY = y - fm.getHeight() / 2;
                    g2d.drawString(label, labelX, labelY);
                }

                // Wykres regresji liniowej
                g2d.setColor(Color.BLUE);
                int x1 = margines;
                int y1 = wysokosc - margines - (int)((a * xMin + b - yMin) * skalaY);
                int x2 = szerokosc - margines;
                int y2 = wysokosc - margines - (int)((a * xMax + b - yMin) * skalaY);
                g2d.drawLine(x1, y1, x2, y2);

                g2d.setColor(Color.BLACK);
                labelA.setText("a = " + String.format("%.2f", a));
                labelB.setText("b = " + String.format("%.2f", b));
                repaint();
            } else {
                // Rysowanie osi
                g2d.drawLine(margines, wysokosc / 2, szerokosc - margines, wysokosc / 2);
                g2d.drawLine(szerokosc / 2, margines, szerokosc / 2, wysokosc - margines);

                // Strzałka dla osi X
                strzalka(g2d, margines, wysokosc / 2, szerokosc - margines, wysokosc / 2);

                // Strzałka dla osi Y
                strzalka(g2d, szerokosc / 2, wysokosc - margines, szerokosc / 2, margines);
            }
        }

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Start app = new Start();
                app.setVisible(true);
            }
        });
    }
}
