package tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import model.Formula;
import model.operator.False;
import model.operator.True;
import top.Lexer;
import top.Parser;
import top.misc.ICnfConvertable;
import top.misc.IConstNames;

/**
 * Verify PL to CNF conversion 
 * @author Dominik
 */
public class CnfTest implements IConstNames {

	private final Lexer lexer;
	
	public CnfTest() {
		this.lexer = new Lexer();
	}
	
	/**
	 * A /\ A = A
	 */
	@Test
	public void aAndA() {
		ArrayList<String> tokens;
		Formula<?> model, cnf;
		try {
			tokens = this.lexer.lex("A /\\ A");
			model = new Parser(tokens).parse();
			cnf = ((ICnfConvertable)model).convert();
			assertEquals("A", cnf.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A \/ ~A = True
	 */
	@Test
	public void aOrNotA() {
		ArrayList<String> tokens;
		Formula<?> model, cnf;
		try {
			tokens = this.lexer.lex("A \\/ ~A");
			model = new Parser(tokens).parse();
			cnf = ((ICnfConvertable)model).convert();
			assertEquals(new True().toString(), cnf.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A /\ ~A = False
	 */
	@Test
	public void aAndNotA() {
		ArrayList<String> tokens;
		Formula<?> model, cnf;
		try {
			tokens = this.lexer.lex("A /\\ ~A");
			model = new Parser(tokens).parse();
			cnf = ((ICnfConvertable)model).convert();
			assertEquals(new False().toString(), cnf.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * A /\ False = False
	 */
	@Test
	public void aAndFalse() {
		ArrayList<String> tokens;
		Formula<?> model, cnf;
		try {
			tokens = this.lexer.lex("A /\\ False");
			model = new Parser(tokens).parse();
			cnf = ((ICnfConvertable)model).convert();
			assertEquals(new False().toString(), cnf.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * A /\ True = A
	 */
	@Test
	public void aAndTrue() {
		ArrayList<String> tokens;
		Formula<?> model, cnf;
		try {
			tokens = this.lexer.lex("A /\\ True");
			model = new Parser(tokens).parse();
			cnf = ((ICnfConvertable)model).convert();
			assertEquals("A", cnf.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * A \/ True = True
	 */
	@Test
	public void aOrTrue() {
		ArrayList<String> tokens;
		Formula<?> model, cnf;
		try {
			tokens = this.lexer.lex("A \\/ True");
			model = new Parser(tokens).parse();
			cnf = ((ICnfConvertable)model).convert();
			assertEquals(new True().toString(), cnf.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A \/ False = A
	 */
	@Test
	public void aOrFalse() {
		ArrayList<String> tokens;
		Formula<?> model, cnf;
		try {
			tokens = this.lexer.lex("A \\/ False");
			model = new Parser(tokens).parse();
			cnf = ((ICnfConvertable)model).convert();
			assertEquals("A", cnf.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	
	/**
	 * A <==> True = A
	 */
	@Test
	public void aImpliesTrue() {
		ArrayList<String> tokens;
		Formula<?> model, cnf;
		try {
			tokens = this.lexer.lex("A <==> True");
			model = new Parser(tokens).parse();
			cnf = ((ICnfConvertable)model).convert();
			assertEquals("A", cnf.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	
	/**
	 * A <==> False = ~A
	 */
	@Test
	public void aImpliesFalse() {
		ArrayList<String> tokens;
		Formula<?> model, cnf;
		try {
			tokens = this.lexer.lex("A <==> False");
			model = new Parser(tokens).parse();
			cnf = ((ICnfConvertable)model).convert();
			assertEquals("~A", cnf.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A /\ (B /\ C) = A /\ B /\ C
	 */
	@Test
	public void flattenAnd() {
		ArrayList<String> tokens;
		Formula<?> model, cnf;
		try {
			tokens = this.lexer.lex("A /\\ (B /\\ C)");
			model = new Parser(tokens).parse();
			cnf = ((ICnfConvertable)model).convert();
			assertEquals("A /\\ B /\\ C", cnf.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A \/ (B \/ C) = A \/ B \/ C
	 */
	@Test
	public void flattenOr() {
		ArrayList<String> tokens;
		Formula<?> model, cnf;
		try {
			tokens = this.lexer.lex("A \\/ (B \\/ C)");
			model = new Parser(tokens).parse();
			cnf = ((ICnfConvertable)model).convert();
			assertEquals("A \\/ B \\/ C", cnf.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	/**
//	 * ~E \/ [~I /\ ~M] = (~E \/ ~I) /\ (~E \/ ~M)
//	 */
//	@Test
//	public void distributivity2LToR() {
//		ArrayList<String> tokens;
//		Formula<?> model, cnf;
//		try {
//			tokens = this.lexer.lex("~E \\/ [~I /\\ ~M]");
//			model = new Parser(tokens).parse();
//			cnf = ((ICnfConvertable)model).convert();
//			assertEquals("(~E \\/ ~I) /\\ (~E \\/ ~M)", cnf.toString());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
//	/** NOTE: fix parser bug!
//	 * unwrap
//	 * A \/ (((B \/ C) \/ ~D) \/ E)
//	 */
//	@Test
//	public void unwrap() {
//		ArrayList<String> tokens;
//		Formula<?> model, cnf;
//		try {
//			tokens = this.lexer.lex("A \\/ (((B \\/ C) \\/ ~D) \\/ E)");
//			model = new Parser(tokens).parse();
//			cnf = ((ICnfConvertable)model).convert();
//			assertEquals("A \\/ B \\/ C \\/ ~D \\/ E", cnf.toString());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
}
