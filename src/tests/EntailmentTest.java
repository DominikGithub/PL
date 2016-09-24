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
import top.valuation.ValuationSet;

public class EntailmentTest {
	
	private final Lexer lexer;
	
	public EntailmentTest() {
		this.lexer = new Lexer();
	}
	
	/**
	 * True |= False = False
	 */
	@Test
	public void trueEntailsFalse() {
		ArrayList<String> tokens;
		Formula<?> model, cnf;
		try {
			tokens = this.lexer.lex("True |= False");
			model = new Parser(tokens).parse();
			cnf = ((ICnfConvertable)model).convert();
			assertEquals(new False(), cnf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * False |= True = True
	 */
	@Test
	public void falseEntailsTrue() {
		ArrayList<String> tokens;
		Formula<?> model, cnf;
		try {
			tokens = this.lexer.lex("False |= True");
			model = new Parser(tokens).parse();
			cnf = ((ICnfConvertable)model).convert();
			assertEquals(new True(), cnf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A /\ B |= (A <==> B) = True
	 */
	@Test
	public void resAAndNotAAndB() {
		ArrayList<String> tokens;
		Formula<?> model, cnf;
		try {
			tokens = this.lexer.lex("A /\\ B |= (A <==> B)");
			model = new Parser(tokens).parse();
			cnf = ((ICnfConvertable)model).convert();
			assertEquals(new True(), cnf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A ==> B |= A \/ B = False
	 */
	@Test
	public void aImplBEntailsAOrB() {
		ArrayList<String> tokens;
		Formula<?> model, cnf;
		try {
			tokens = this.lexer.lex("A ==> B |= (A \\/ B)");	//TODO remove brackets like asked!
			model = new Parser(tokens).parse();
			cnf = ((ICnfConvertable)model).convert();
			assertEquals(new False(), cnf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A <==> B |= ~A \/ B = True
	 */
	@Test
	public void aBiBEntailsNotAOrB() {
		ArrayList<String> tokens;
		Formula<?> model, cnf;
		try {
			tokens = this.lexer.lex("A <==> B |= (~A \\/ B)");	//TODO remove brackets like asked!
			model = new Parser(tokens).parse();
			cnf = ((ICnfConvertable)model).convert();
			assertEquals(new True(), cnf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * A /\ B satisfiable = True
	 */
	@Test
	public void satisfiableAAndB() {
		ArrayList<String> tokens;
		Formula<?> model;
		try {
			tokens = this.lexer.lex("A /\\ B");
			model = new Parser(tokens).parse().convert();
			assertTrue(ValuationSet.satisfiable(model));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A /\ ~A unsatisfiable = True
	 */
	@Test
	public void unsatisfiableAAndNotA() {
		ArrayList<String> tokens;
		Formula<?> model;
		try {
			tokens = this.lexer.lex("A /\\ ~A");
			model = new Parser(tokens).parse().convert();
			assertTrue(ValuationSet.unsatisfiable(model));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A /\ ~A satisfiable = False
	 */
	@Test
	public void satisfiableAAndNotA() {
		ArrayList<String> tokens;
		Formula<?> model;
		try {
			tokens = this.lexer.lex("A /\\ ~A");
			model = new Parser(tokens).parse().convert();
			assertFalse(ValuationSet.satisfiable(model));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * True validity = True
	 */
	@Test
	public void validityTrue() {
		ArrayList<String> tokens;
		Formula<?> model;
		try {
			tokens = this.lexer.lex("True");
			model = new Parser(tokens).parse().convert();
			assertTrue(ValuationSet.tautology(model));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A \/ ~A validity = True
	 */
	@Test
	public void validityAOrNotA() {
		ArrayList<String> tokens;
		Formula<?> model;
		try {
			tokens = this.lexer.lex("A \\/ ~A");
			model = new Parser(tokens).parse().convert();
			assertTrue(ValuationSet.tautology(model));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * False unsatisfiable = True
	 */
	@Test
	public void unsatisfiableFalse() {
		ArrayList<String> tokens;
		Formula<?> model;
		try {
			tokens = this.lexer.lex("False");
			model = new Parser(tokens).parse().convert();
			assertTrue(ValuationSet.unsatisfiable(model));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	/**
//	 * superman
//	 */
//	@Test
//	public void noSuperman() {
//		ArrayList<String> tokens;
//		Formula<?> model, cnf;
//		try {
//			tokens = this.lexer.lex("(A /\\ W ==> P) /\\ (~A ==> I) /\\ (~W ==> M) /\\ (~P) /\\ (E ==> (~I /\\ ~M)) /\\ (E)");
//			model = new Parser(tokens).parse();
//			cnf = ((ICnfConvertable)model).convert();
//			assertEquals(new False(), cnf.toString());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

}
