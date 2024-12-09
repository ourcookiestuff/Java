public class CalculatorTest {

    public static void main(String[] args) {
        // Tworzenie instancji klasy ProgrammableCalculator
        ProgrammableCalculator calculator = new ProgrammableCalculator();

        // Ustawienie LineReader i LinePrinter (do testów)
        calculator.setStdin(calculator.new ConsoleLineReader());
        calculator.setStdout(calculator.new ConsoleLinePrinter());

        // Przykład: Odczytywanie kodu programu z ciągu znaków
        String programCode = "10 GOTO 80\n" +
                "20 LET A = A + 1\n" +
                "25 LET B = B + 1\n" +
                "30 RETURN\n" +
                "40 PRINT \"ZMIENNE\"\n" +
                "45 PRINT A\n" +
                "50 PRINT B\n" +
                "55 RETURN\n" +
                "80 LET A = 100\n" +
                "85 LET B = 120\n" +
                "87 GOSUB 40\n" +
                "90 GOSUB 20\n" +
                "95 GOSUB 40";

        // Utworzenie BufferedReader dla kodu programu
        java.io.BufferedReader programReader = new java.io.BufferedReader(new java.io.StringReader(programCode));
        calculator.programCodeReader(programReader);

        // Uruchomienie programu zaczynając od linii 10
        calculator.run(10);
    }
}
