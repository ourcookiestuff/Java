import javax.swing.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.function.Function2D;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.xy.XYFunctionCollection;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Start {

    public static double[][] wczytywanieDanych(File plik) {
        double[][] dane = null;
        try {
            Scanner czytanieLini = new Scanner(plik);
            String pierwszaLinia = czytanieLini.nextLine();
            dane = new double[2][Integer.parseInt(pierwszaLinia)];
            for(int i = 0; i < Integer.parseInt(pierwszaLinia) ; i++) {
                String linia = czytanieLini.nextLine();
                String[] parts = linia.split(" ");
                dane[0][i] = Double.parseDouble(parts[0]);
                dane[1][i] = Double.parseDouble(parts[1]);
            }
            czytanieLini.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for(int m = 0; m< dane[1].length; m++) {
            System.out.println("X = " + dane[0][m] + " Y = " + dane[1][m]);
        }
        return dane;
    }

    public static double[] wyliczanieFunkcj(double[][] dane) {
        double suma_xi_kwadrat = 0.0;
        double suma_yi = 0.0;
        double suma_xi = 0.0;
        double suma_xi_yi = 0.0;
        for (int i = 0; i < dane.length; i++) {
            suma_xi_kwadrat += Math.pow(dane[0][i], 2);
            suma_yi += dane[1][i];
            suma_xi += dane[0][i];
            suma_xi_yi += dane[0][i] * dane[1][i];
        }
        double w = dane.length * suma_xi_kwadrat - Math.pow(suma_xi, 2);
        double a = ((suma_xi_kwadrat * suma_yi) - (suma_xi * suma_xi_yi))/w;
        double b = ((dane.length * suma_xi_yi) - (suma_xi * suma_yi))/w;
        double[] wynik = new double[2];
        wynik[0] = a;
        wynik[1] = b;
        System.out.println("a = " + wynik[0] + " b = " + wynik[1]);
        return wynik;
    }

    private static LineChart tworzenieWykresu(double[][] punkty, final double[] funkcja) {
        XYSeriesCollection daneWykresu = new XYSeriesCollection();

        XYFunctionCollection daneFunkcja = new XYFunctionCollection();
        Function2D wzorFunkcja = new Function2D() {
            public double getValue(double x) {
                return funkcja[0] + funkcja[1] * x;
            }
        };
        daneFunkcja.addFunction("", funkcja, 0, 4, 100);
        daneWykresu.addSeries(daneFunkcja);

        XYSeries danePunkty = new XYSeries("Punkty");
        for (int i = 0; i<punkty.length; i++) {
            danePunkty.add(punkty[0][i], punkty[1][i]);
        }
        daneWykresu.addSeries(danePunkty);

        ;
    }

    public static void main(String[] args) {

        final JFrame okno = new JFrame();
        okno.setSize(500, 400);
        okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        okno.setLayout(new BorderLayout());
        okno.setVisible(true);

        JPanel dolnyPanel = new JPanel();
        dolnyPanel.setLayout(new BorderLayout());
        okno.add(dolnyPanel, BorderLayout.SOUTH);

        JButton przycisk = new JButton("ReadFile");
        przycisk.setBounds(25, 300, 100, 40);
        dolnyPanel.add(przycisk, BorderLayout.WEST);

        final JPanel panelAB = new JPanel();
        panelAB.setLayout(new BorderLayout());
        dolnyPanel.add(panelAB, BorderLayout.CENTER);

        final JPanel aPanel = new JPanel();
        aPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panelAB.add(aPanel, BorderLayout.CENTER);

        final JPanel bPanel = new JPanel();
        bPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panelAB.add(bPanel, BorderLayout.EAST);

        final ChartPanel wykresPanel = new ChartPanel(null);
        okno.add(wykresPanel, BorderLayout.CENTER);

        przycisk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser wybieraniePliku = new JFileChooser();
                int statusWybrania = wybieraniePliku.showOpenDialog(null);
                if (statusWybrania == JFileChooser.APPROVE_OPTION) {
                    File plik = wybieraniePliku.getSelectedFile();
                    System.out.println("Wybrany plik: " + plik.getAbsolutePath());
                    double[][] dane = wczytywanieDanych(plik);
                    double[] funkcja = wyliczanieFunkcj(dane);
                    JFreeChart wykres = tworzenieWykresu(dane, funkcja);
                    wykresPanel.setChart(wykres);
                    String tekstA = "a = " + funkcja[0];
                    String tekstB = "b = " + funkcja[1];
                    JTextField aPoleTekstowe = new JTextField(tekstA, 20);
                    JTextField bPoleTekstowe = new JTextField(tekstB, 20);
                    aPanel.removeAll();
                    aPanel.add(aPoleTekstowe);
                    bPanel.removeAll();
                    bPanel.add(bPoleTekstowe);
                    okno.revalidate();
                    okno.repaint();
                }
            }
        });
    }


}