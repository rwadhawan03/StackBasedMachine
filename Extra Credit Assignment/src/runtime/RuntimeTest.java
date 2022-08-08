package runtime;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RuntimeTest {

	@Test
	void runtimeTest1() {
		Runtime r = new Runtime();
		r.readFromFile("C://Users//risha//eg1.pgm"); 
		r.run(); 
		assertEquals(r.toString(), "Pgm   : [push 5.0, push 3.4567, add, pop m0, exit]\n" + 
				"Pc    : 6\n" + 
				"Stack : []\n" + 
				"Memory: [8.4567, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]\n" + 
				"------------------------------------------------\n");
	}
	
	@Test
	void runtimeTest2() {
		Runtime r = new Runtime();
		r.readFromFile("C://Users//risha//eg2.pgm"); 
		r.run(); 
		assertEquals(r.toString(), "Pgm   : [push 5.0, pop m0, push m0, push m0, label l2, dec, jmpz done, pop m0, push m0, mul, push m0, jmp l2, label done, pop m0, exit]\n" + 
				"Pc    : 16\n" + 
				"Stack : [120.0]\n" + 
				"Memory: [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]\n" + 
				"------------------------------------------------\n");
	}
	
	@Test
	void runtimeTest3() {
		Runtime r = new Runtime();
		r.readFromFile("C://Users//risha//eg4.pgm"); 
		r.run(); 
		assertEquals(r.toString(), "Pgm   : [push 5.0, pop m0, exit]\n" + 
				"Pc    : 4\n" + 
				"Stack : []\n" + 
				"Memory: [5.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]\n" + 
				"------------------------------------------------\n");
	}

}
