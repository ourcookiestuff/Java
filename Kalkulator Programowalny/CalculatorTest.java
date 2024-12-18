public class CalculatorTest {

    public static void main(String[] args) {
        // Tworzenie instancji klasy ProgrammableCalculator
        ProgrammableCalculator calculator = new ProgrammableCalculator();

        // Ustawienie LineReader i LinePrinter (do testów)
        calculator.setStdin(calculator.new ConsoleLineReader());
        calculator.setStdout(calculator.new ConsoleLinePrinter());

        // Przykład: Odczytywanie kodu programu z ciągu znaków
        String programCode = "10 INPUT n\n" +
                "11 PRINT \"Hello moge cie zjesc\"\n" +
                //"12 END\n" +
                "20 LET sum = 0\n" +
                //"21 IF n > sum PRINT \"Wygrales\"\n" +
                "30 LET i = 1\n" +
                "40 LET sum = sum + i\n" +
                //"41 print sum\n" +
                "50 LET i = i + 1\n" +
                "60 IF n > i GOTO 40\n" +
                "70 PRINT sum\n" +
                "80 END";

        // Utworzenie BufferedReader dla kodu programu
        java.io.BufferedReader programReader = new java.io.BufferedReader(new java.io.StringReader(programCode));
        calculator.programCodeReader(programReader);

        // Uruchomienie programu zaczynając od linii 10
        calculator.run(10);
    }
}
