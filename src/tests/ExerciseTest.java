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

public class ExerciseTest implements IConstNames {

	private Lexer lexer;
	
	public ExerciseTest() {
		this.lexer = new Lexer();
	}
	
	/**
	 * Smoke ==> Smoke = True
	 */
	@Test
	public void AImpliesAEqTrue() {
		ArrayList<String> tokens;
		Formula<?> model, cnf;
		try {
			tokens = this.lexer.lex("Smoke ==> Smoke");
			model = new Parser(tokens).parse();
			cnf = ((ICnfConvertable)model).convert();
			assertEquals(new True().toString(), cnf.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * (Smoke ==> Fire) ==> (~Smoke ==> ~Fire) = Smoke \/ ~Fire
	 */
	@Test
	public void ImplImplNotImpl() {
		ArrayList<String> tokens;
		Formula<?> model, cnf;
		try {
			tokens = this.lexer.lex("(Smoke ==> Fire) ==> (~Smoke ==> ~Fire)");
			model = new Parser(tokens).parse();
			cnf = ((ICnfConvertable)model).convert();
			assertEquals("Smoke \\/ ~Fire", cnf.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Smoke \/ Fire \/ ~Fire = True
	 */
	@Test
	public void SOrFOrNotF() {
		ArrayList<String> tokens;
		Formula<?> model, cnf;
		try {
			tokens = this.lexer.lex("Smoke \\/ Fire \\/ ~Fire");
			model = new Parser(tokens).parse();
			cnf = ((ICnfConvertable)model).convert();
			assertEquals(new True().toString(), cnf.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * (Fire ==> Smoke) /\ Fire /\ ~Smoke = False
	 */
	@Test
	public void implAndFAndNotS() {
		ArrayList<String> tokens;
		Formula<?> model, cnf;
		try {
			tokens = this.lexer.lex("(Fire ==> Smoke) /\\ Fire /\\ ~Smoke");
			model = new Parser(tokens).parse();
			cnf = ((ICnfConvertable)model).convert();
			assertEquals(new False().toString(), cnf.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * b <==> (a <==> ~a) = ~b
	 */
	@Test
	public void bBiAImplNotA() {
		ArrayList<String> tokens;
		Formula<?> model, cnf;
		try {
			tokens = this.lexer.lex("b <==> (a <==> ~a)");
			model = new Parser(tokens).parse();
			cnf = ((ICnfConvertable)model).convert();
			assertEquals("~b", cnf.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * resolve:
	 * a /\ (~a /\ b) = False
	 */
	@Test
	public void resAAndNotAAndB() {
		ArrayList<String> tokens;
		Formula<?> model, cnf;
		try {
			tokens = this.lexer.lex("A /\\ (~A /\\ B)");
			model = new Parser(tokens).parse();
			cnf = ((ICnfConvertable)model).convert();
			assertEquals(new False().toString(), cnf.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
