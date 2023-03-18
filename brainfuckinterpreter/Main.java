package brainfuckinterpreter;

public class Main {
    public static void main(String[] args) throws Exception {

        // If the second parameter is provided
        int memorySize = BrainFuckInterpreter.DEFAULT_MEMORY_SIZE;
        if (args.length > 1) 
            memorySize = Integer.parseInt(args[1]);

        BrainFuckInterpreter interpreter = new BrainFuckInterpreter(memorySize);

        interpreter.readProgram(args[0]);

        interpreter.verifyProgram();

        interpreter.interpret();



        




    }
    
}
