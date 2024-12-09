import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;

class ProgrammableCalculator implements ProgrammableCalculatorInterface {

    private BufferedReader codeReader;
    private LineReader stdin;
    private LinePrinter stdout;
    private Map<Integer, String> programCode;
    private Map<String, Integer> variables = new HashMap<>();
    private Stack<Integer> stos = new Stack<>();

    @Override
    public void programCodeReader(BufferedReader reader) {
        codeReader = reader;
        programCode = new LinkedHashMap<>();
        readProgramCode();
    }

    @Override
    public void setStdin(LineReader input) {
        this.stdin = input;
    }

    @Override
    public void setStdout(LinePrinter output) {
        this.stdout = output;
    }

    @Override
    public void run(int line) {
        int currentLine = line;
        while (programCode.containsKey(currentLine)) {
            String instruction = programCode.get(currentLine);
            currentLine = executeInstruction(instruction, currentLine);
            if (currentLine == -1) {
                break;
            }
        }
    }

    private void readProgramCode() {
        String line;
        try {
            while ((line = codeReader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(" ", 2);
                    int lineNumber = Integer.parseInt(parts[0]);
                    programCode.put(lineNumber, parts[1]);
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private int executeInstruction(String instruction, int currentLine) {
        boolean instrukcjaIF = false;
        String[] element = instruction.split("\\s+", 3);
        int lineNumber = currentLine;

        String command = element[0].toUpperCase();

        int nextLine = lineNumber;

            switch (command) {
                case "LET":
                    executeLet(element[1],element[2]);
                    break;
                case "PRINT":
                    executePrint(instruction);
                    break;
                case "GOTO":
                    return executeGoto(element[1]);
                case "GOSUB":
                    return executeGosub(element[1], lineNumber);
                case "RETURN":
                    return executeReturn();
                case "IF":
                    lineNumber = executeIf(element[1], element[2], lineNumber);
                    break;
                case "INPUT":
                    executeInput(element[1]);
                    break;
                case "END":
                    return -1;
                default:
                    break;
            }

        nextLine = findNextLine(lineNumber);

        return nextLine;
    }

    private int findNextLine(int currentLine) {
        boolean foundCurrentLine = false;

        for (Map.Entry<Integer, String> entry : programCode.entrySet()) {
            if (foundCurrentLine) {
                return entry.getKey();
            }
            if (entry.getKey() == currentLine) {
                foundCurrentLine = true;
            }
        }
        return -1;
    }

    private int findPreviousLine(int currentLine) {
        int previousLine = -1;

        for (Map.Entry<Integer, String> entry : programCode.entrySet()) {
            if (entry.getKey() == currentLine) {
                return previousLine;
            }
            previousLine = entry.getKey();
        }

        return -1;
    }

    private void executeLet(String variable, String expression) {
        if (expression.contains("+") || expression.contains("-") || expression.contains("*") || expression.contains("/")) {
            int result = evaluateExpression(expression);
            variables.put(variable.toUpperCase(), result);
        } else {
            expression = expression.trim();
            if (expression.startsWith("=")) {
                expression = expression.substring(1).trim();
            }
            int value = Integer.parseInt(expression);
            variables.put(variable.toUpperCase(), value);
        }
    }

    private void executePrint(String value) {
        String[] element = value.split("\\s+", 2);
        value = element[1];
        if (value.startsWith("\"") && value.endsWith("\"")) {
            stdout.printLine(value.substring(1, value.length() - 1));
        } else {
            String variable = value.toUpperCase();
            if (variables.containsKey(variable)) {
                stdout.printLine(String.valueOf(variables.get(variable)));
            }
        }
    }

    private int executeGoto(String targetLineNumber) {
        int lineNumber = Integer.parseInt(targetLineNumber);
        if (programCode.containsKey(lineNumber)) {
            return lineNumber;
        } else {
            return -1;
        }
    }

    private int executeGosub(String targetLineNumber, int currentLine) {
        stos.push(currentLine);

        int lineNumber = Integer.parseInt(targetLineNumber);

        if (programCode.containsKey(lineNumber)) {
            return lineNumber;
        } else {
            return -1;
        }
    }

    private int executeReturn() {
        if (!stos.isEmpty()) {
            int liniaPowrotu = stos.pop();

            int nextLine = findNextLine(liniaPowrotu);

            return nextLine;
        } else {
            return -1;
        }
    }

    private int executeIf(String variable1, String expression, int lineNumber) {
        variable1 = variable1.toUpperCase();

        String[] element = expression.split("\\s+", 4);

        String variable2 = element[1].toUpperCase();
        String comparisonOperator = element[0];
        int value1 = variables.getOrDefault(variable1, 0);
        int value2 = isVariable(variable2) ? variables.getOrDefault(variable2, 0) : Integer.parseInt(variable2);

        boolean conditionMet = false;
        switch (comparisonOperator) {
            case "=":
                conditionMet = (value1 == value2);
                break;
            case "<":
                conditionMet = (value1 < value2);
                break;
            case ">":
                conditionMet = (value1 > value2);
                break;
        }

        if (conditionMet) {
            lineNumber = findPreviousLine(executeGoto(element[3]));
            return lineNumber;

        } else {
            return lineNumber;
        }
    }

    private void executeInput(String variable) {
        variable = variable.toUpperCase();
        String input = stdin.readLine();
        int value = Integer.parseInt(input.trim());
        variables.put(variable, value);
    }

    private int evaluateExpression(String expression) {
        String[] element = expression.split("\\s+");
        int result = 0;
        int operand1 = 0;
        int operand2 = 0;
        String operator = "";

        if (element.length == 4) {
            operand1 = isNumber(element[1]) ? Integer.parseInt(element[1]) : variables.getOrDefault(element[1].toUpperCase(), 0);
            operator = element[2];
            operand2 = isNumber(element[3]) ? Integer.parseInt(element[3]) : variables.getOrDefault(element[3].toUpperCase(), 0);
        } else if (element.length == 3) {
            operand1 = variables.getOrDefault(element[1].toUpperCase(), 0);
        }

        switch (operator) {
            case "+":
                result = operand1 + operand2;
                break;
            case "-":
                result = operand1 - operand2;
                break;
            case "*":
                result = operand1 * operand2;
                break;
            case "/":
                result = operand1 / operand2;
                break;
            default:
                result = operand1;
                break;
        }

        return result;
    }

    private boolean isNumber(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isVariable(String value) {
        return Character.isLetter(value.charAt(0));
    }

    private boolean isOperator(String value) {
        return value.equals("+") || value.equals("-") || value.equals("*") || value.equals("/");
    }

    class ConsoleLineReader implements LineReader {
        private BufferedReader reader;

        public ConsoleLineReader() {
            this.reader = new BufferedReader(new java.io.InputStreamReader(System.in));
        }

        @Override
        public String readLine() {
            StringBuilder input = new StringBuilder();
            try {
                int charCode;
                while ((charCode = reader.read()) != -1) {
                    char character = (char) charCode;
                    if (character == '\n') {
                        break;
                    }
                    input.append(character);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return input.toString();
        }
    }

    class ConsoleLinePrinter implements LinePrinter {
        @Override
        public void printLine(String line) {
            System.out.println(line);
        }
    }
}

