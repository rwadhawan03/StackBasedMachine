package runtime;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class Runtime {
	// data fields
	private ArrayList<Instruction> pgm;
	private int pc;
	private Stack<Double> stack;
	private ArrayList<Double> memory;

	Runtime() {
		stack = new Stack<>();
		memory = new ArrayList<>(10);
	}

	/**
	 * Set SBM to initial state
	 */
	private void initialize() {
		// empty stack
		while (!stack.isEmpty()) {
			stack.pop();
		}
		// reset all memory locations
		for (int i = 0; i < 10; i++) {
			memory.add(0.0);
		}
		// set pc to first instruction
		pc = 1;
	}

	// jumpToLabel(String label) takes parameter label, and returns the index of at
	// which this label was declared
	private int jumpToLabel(String label) {
		int result = 0;
		for (int i = 0; i < pgm.size(); i++) {
			if (pgm.get(i).toString().equalsIgnoreCase("label " + label)) {
				return result;
			}
			result++;
		}
		throw new IllegalStateException("runtime: label not found");

	}

	// processInstruction(Instruction i) takes a parameter i, and return true while
	// performing action to the memory, stack, pc, and pgm with the instruction i,
	// and returns false if the instruction i is an exit program
	private boolean processInstruction(Instruction i) {
		switch (i.code) {
		case 0:
			return false;
		case 1:
			stack.push(((PushLiteral) i).getLiteral());
			break;
		case 2:
			stack.push(memory.get(((PushLocation) i).getAddress()));
			break;
		case 3:
			double topVal = stack.pop();
			memory.set(((Pop) i).getAddress(), topVal);
			break;
		case 4:
			double firstA = stack.pop();
			double secondA = stack.pop();
			stack.push(firstA + secondA);
			break;
		case 5:
			double firstS = stack.pop();
			double secondS = stack.pop();
			stack.push(firstS - secondS);
			break;
		case 6:
			double firstM = stack.pop();
			double secondM = stack.pop();
			stack.push(firstM * secondM);
			break;
		case 7:
			double firstD = stack.pop();
			double secondD = stack.pop();
			stack.push(firstD / secondD);
			break;
		case 8:
			Label name = new Label(i.code, i.mnemonic, ((Label) i).getName());
			break;
		case 9:
			if (stack.peek() == 0) {
				processInstruction(pgm.get(jumpToLabel(((Jmpz) i).getTargetLabel())));
				pc = jumpToLabel(((Jmpz) i).getTargetLabel());
			}
			break;
		case 10:
			processInstruction(pgm.get(jumpToLabel(((Jmp) i).getTargetLabel())));
			pc = jumpToLabel(((Jmp) i).getTargetLabel());
			break;
		case 11:
			Double dec = stack.pop() - 1;
			stack.push(dec);
			break;
		}

		return true;
	}

	// run() executes the arrayList pgm of all the instructions within the list
	public void run() {
		initialize();
		while (processInstruction(pgm.get(pc - 1))) {
			pc++;
		}
		pc++;
	}

	private Instruction parseInstruction(String str, int line) {
		String[] p = str.split("[ ]+"); // delimiters are non-empty sequences of spaces
		Instruction i = null;

		switch (p[0]) {
		case "exit":
			i = new Exit(0, "exit");
			break;
		case "push":
			if (p.length == 1) {
				throw new IllegalStateException("parseInstruction: syntax error at line " + line);
			}
			try {
				if (p[1].charAt(0) == 'm') {
					int loc = Integer.parseInt(p[1].substring(1));
					if (loc < 0 || loc > 9) {
						throw new IllegalStateException("parseInstruction: syntax error at line " + line);
					}
					i = new PushLocation(2, "push", loc);
				} else {
					i = new PushLiteral(1, "push", Double.parseDouble(p[1]));
				}
			} catch (NumberFormatException e) {
				throw new IllegalStateException("parseInstruction: syntax error at line " + line);
			}
			break;
		case "pop":
			if (p.length == 1) {
				throw new IllegalStateException("parseInstruction: syntax error at line " + line);
			}
			int loc = Integer.parseInt(p[1].substring(1));
			if (loc < 0 || loc > 9) {
				throw new IllegalStateException("parseInstruction: syntax error at line " + line);
			}
			i = new Pop(3, "pop", loc);
			break;
		case "add":
			i = new Add(4, "add");
			break;
		case "sub":
			i = new Sub(5, "sub");
			break;
		case "mul":
			i = new Mul(6, "mul");
			break;
		case "div":
			i = new Div(7, "div");
			break;
		case "label":
			i = new Label(8, "label", p[1]);
			break;
		case "jmpz":
			i = new Jmpz(9, "jmpz", p[1]);
			break;
		case "jmp":
			i = new Jmp(10, "jmp", p[1]);
			break;
		case "dec":
			i = new Dec(11, "dec");
			break;
		case "":
			break;
		default:
			throw new IllegalStateException("parseInstruction: syntax error at line " + line);
		}
		return i;
	}

	public void readFromFile(String name) {
		pgm = new ArrayList<>();
		File f = new File(name);
		try {
			Scanner s = new Scanner(f);
			int line = 1;

			while (s.hasNext()) {
				Instruction i = parseInstruction(s.nextLine(), line);
				if (i != null) {
					pgm.add(i);
				}
				line++;
			}
			s.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String toString() {
		StringBuilder r = new StringBuilder();
		r.append("Pgm   : ");
		r.append(pgm == null ? "null" : pgm.toString());
		r.append("\nPc    : " + pc);
		r.append("\nStack : ");
		r.append(stack.toString());
		r.append("\nMemory: ");
		r.append(memory.toString());
		r.append("\n------------------------------------------------\n");

		return r.toString();
	}

	public static void main(String[] args) {
		Runtime r = new Runtime();
		r.readFromFile("C://Users//risha//eg2.pgm"); // Load and parse mini-bytecode program
		r.run(); // execute program
		//System.out.println(r); // print resulting state of the SBM
	}
}
