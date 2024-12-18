/*public class CalculatorTest {
    public static void main(String[] argc){
        Calculator calculator = new Calculator();

        calculator.setAccumulator(10);
        calculator.addToAccumulator(5);
        System.out.println("Accumulator: " + calculator.getAccumulator());
    }
}*/
public class CalculatorTest {
    public static void main(String[] args) {
        Calculator calculator = new Calculator();

        System.out.println("Initial accumulator value: " + calculator.getAccumulator());

        // Test setAccumulator
        calculator.setAccumulator(10);
        System.out.println("Accumulator after setAccumulator: " + calculator.getAccumulator());

        calculator.accumulatorToMemory(0);
        System.out.println("Memory[0] after accumulatorToMemory: " + calculator.getMemory(0));

        // Test addToAccumulator
        calculator.addToAccumulator(5);
        System.out.println("Accumulator after addToAccumulator: " + calculator.getAccumulator());

        calculator.accumulatorToMemory(1);
        System.out.println("Memory[1] after accumulatorToMemory: " + calculator.getMemory(1));

        // Test subtractFromAccumulator
        calculator.subtractFromAccumulator(3);
        System.out.println("Accumulator after subtractFromAccumulator: " + calculator.getAccumulator());

        calculator.accumulatorToMemory(2);
        System.out.println("Memory[2] after accumulatorToMemory: " + calculator.getMemory(2));

        // Test addMemoryToAccumulator
        calculator.addMemoryToAccumulator(1);
        System.out.println("Accumulator after addMemoryToAccumulator: " + calculator.getAccumulator());

        // Test subtractMemoryFromAccumulator
        calculator.subtractMemoryFromAccumulator(0);
        System.out.println("Accumulator after subtractMemoryFromAccumulator: " + calculator.getAccumulator());

        for (int i = 0; i < Calculator.MEMORY_SIZE; i++) {
            System.out.println("Memory[" + i + "]: " + calculator.getMemory(i));
        }

        // Test exchangeMemoryWithAccumulator
        calculator.exchangeMemoryWithAccumulator(1);
        System.out.println("Memory[1] after exchangeMemoryWithAccumulator: " + calculator.getMemory(1));
        System.out.println("Accumulator after exchangeMemoryWithAccumulator: " + calculator.getAccumulator());

        // Test pushAccumulatorOnStack
        calculator.pushAccumulatorOnStack();
        System.out.println("Accumulator after pushAccumulatorOnStack: " + calculator.getAccumulator());

        calculator.exchangeMemoryWithAccumulator(2);
        System.out.println("Memory[2] after exchangeMemoryWithAccumulator: " + calculator.getMemory(2));
        System.out.println("Accumulator after exchangeMemoryWithAccumulator: " + calculator.getAccumulator());

        // Test pullAccumulatorFromStack
        calculator.pullAccumulatorFromStack();
        System.out.println("Accumulator after pullAccumulatorFromStack: " + calculator.getAccumulator());

        // Test reset
        calculator.reset();
        System.out.println("Accumulator after reset: " + calculator.getAccumulator());
        System.out.println("Memory[0] after reset: " + calculator.getMemory(0));
    }
}

/*public class CalculatorTest {
    public static void main(String[] args) {
        Calculator calculator = new Calculator();

        System.out.println("Initial accumulator value: " + calculator.getAccumulator());

        // Zapisz kilka wartości w pamięci
        calculator.setAccumulator(10);
        calculator.accumulatorToMemory(0);

        calculator.setAccumulator(20);
        calculator.accumulatorToMemory(1);

        calculator.setAccumulator(30);
        calculator.accumulatorToMemory(2);

        // Zdejmowanie ze stosu
        calculator.pushAccumulatorOnStack();
        calculator.setAccumulator(40);
        calculator.accumulatorToMemory(3);

        calculator.pushAccumulatorOnStack();
        calculator.setAccumulator(50);
        calculator.accumulatorToMemory(4);

        // Zdejmowanie ze stosu i wyświetlanie zawartości pamięci
        calculator.pullAccumulatorFromStack();
        System.out.println("Accumulator after pulling from stack: " + calculator.getAccumulator());

        System.out.println("Memory contents:");
        for (int i = 0; i < Calculator.MEMORY_SIZE; i++) {
            System.out.println("Memory[" + i + "]: " + calculator.getMemory(i));
        }

        // Zamień zawartość pamięci z akumulatorem
        calculator.exchangeMemoryWithAccumulator(0);
        System.out.println("Accumulator after exchange with Memory[0]: " + calculator.getAccumulator());

        calculator.exchangeMemoryWithAccumulator(1);
        System.out.println("Accumulator after exchange with Memory[1]: " + calculator.getAccumulator());


        // Reset
        calculator.reset();
        System.out.println("Accumulator after reset: " + calculator.getAccumulator());
    }
}*/

