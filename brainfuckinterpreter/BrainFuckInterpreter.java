package brainfuckinterpreter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.ArrayList;

public class BrainFuckInterpreter {
    public static final int DEFAULT_MEMORY_SIZE = 30000;

    public int memorySize; 
    public static String program; 
    public static int chars_per_line;
    public char[] memory;
    public List<Integer> lineLengths;


    BrainFuckInterpreter(int memory_size){
        this.memorySize = memory_size;
        this.memory = new char[memorySize];
        this.lineLengths = new ArrayList<Integer>();

    }

    public void readProgram(String file_name) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file_name));
            String line = reader.readLine();
            StringBuilder programBuilder = new StringBuilder();
            while (line != null) {
                programBuilder.append(line);
                lineLengths.add(line.length());
                line = reader.readLine();
            }
            program = programBuilder.toString();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void verifyProgram() {
        Deque<Integer> stack = new ArrayDeque<Integer>();
        int line = 1;
        int char_pos = 1;
        for (int i = 0; i < program.length(); i++) {
            char command = program.charAt(i);
            if (command == '[') {
                stack.push(i);
            } else if (command == ']') {
                if (stack.isEmpty()) {
                    System.out.println("Unopened cycle - line " + line + " character " + char_pos);
                    System.exit(0);
                } else {
                    stack.pop();
                }
            }
            if (char_pos == lineLengths.get(line - 1)) {
                line++;
                char_pos = 1;
            } else {
                char_pos++;
            }
        }
        if (!stack.isEmpty()) {
            int pos = stack.pop();
            line = 1;
            char_pos = 1;
            for (int i = 0; i < pos; i++) {
                if (char_pos == lineLengths.get(line - 1)) {
                    line++;
                    char_pos = 1;
                } else {
                    char_pos++;
                }
            }
            System.out.println("Unclosed cycle - line " + line + " character " + char_pos);
            System.exit(0);
        }

    }

    public void interpret() throws IOException{
        int data_pointer = 0;
        int program_pointer = 0;
        Deque<Integer> stack = new ArrayDeque<Integer>();
        while (program_pointer < program.length()) {
            char command = program.charAt(program_pointer);
            switch (command) {
                case '>':
                    data_pointer++;
                    if (data_pointer >= memorySize) {
                        System.out.println("Memory overrun");
                        System.exit(0);
                    }
                    break;
                case '<':
                    data_pointer--;
                    if (data_pointer < 0) {
                        System.out.println("Memory underrun");
                        System.exit(0);
                    }
                    break;
                case '+':
                    memory[data_pointer]++;
                    break;
                case '-':
                    memory[data_pointer]--;
                    break;
                case '.':
                    if(memory[data_pointer] != 65535)
                        System.out.print((char) memory[data_pointer]);
                    break;
                case ',':
                    memory[data_pointer] = (char)System.in.read();
                    break;
                case '[':
                    if (memory[data_pointer] == 0) {
                        int count = 1;
                        while (count != 0) {
                            program_pointer++;
                            if (program.charAt(program_pointer) == '[')
                                count++;
                            else if (program.charAt(program_pointer) == ']')
                                count--;
                        }
                    } else {
                        stack.push(program_pointer);
                    }
                    break;
                case ']':
                    program_pointer = stack.pop() - 1;  
                    break;
            }
            program_pointer++;
        }
    }
}
