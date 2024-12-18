class Calculator extends CalculatorOperations{
    private int accumulator;
    private final int[] memory;
    private final int[] stack;
    private int wskaznik;

    public Calculator(){
        accumulator = 0;
        memory = new int[MEMORY_SIZE];
        stack = new int[STACK_SIZE];
        wskaznik = -1;
    }

    @Override
    public void setAccumulator(int value){
        accumulator = value;
    }

    @Override
    public int getAccumulator(){
        return accumulator;
    }

    @Override
    public int getMemory(int index){
        return memory[index];
    }

    @Override
    public void accumulatorToMemory(int index){
        memory[index] = accumulator;
    }

    @Override
    public void addToAccumulator(int value){
        accumulator += value;
    }

    @Override
    public void subtractFromAccumulator(int value){
        accumulator -= value;
    }

    @Override
    public void addMemoryToAccumulator(int index){
        accumulator += memory[index];
    }

    @Override
    public void subtractMemoryFromAccumulator(int index){
        accumulator -= memory[index];
    }

    @Override
    public void reset(){
        accumulator = 0;
        for(int i=0; i<MEMORY_SIZE; i++){
            memory[i] = 0;
        }
        for(int i=0; i<STACK_SIZE; i++){
            stack[i] = 0;
        }
        wskaznik=-1;
    }

    @Override
    public void exchangeMemoryWithAccumulator(int index){
        int temp = memory[index];
        memory[index] = accumulator;
        accumulator = temp;
    }

    @Override
    public void pushAccumulatorOnStack(){
        if(wskaznik < STACK_SIZE - 1){
            wskaznik++;
            stack[wskaznik] = accumulator;
        }
    }

    @Override
    public void pullAccumulatorFromStack(){
        if(wskaznik >= 0){
            accumulator = stack[wskaznik];
            wskaznik--;
        }
    }

}
