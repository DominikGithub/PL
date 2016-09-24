package top.misc;

/**
 * Lexer/Parser String pattern 
 * @author Dominik
 */
public interface IConstNames {
	
	//Lexer
	static final String SPACES = " \t\r\n";
	static final String PUNCTUATION = "{}()[]";
	static final String SYMBOLIC = "~`!@#$%^&*-+=|\\:;<>.?/";
	static final String NUMERIC = "0123456789";
	static final String ALPHANUMERIC = "abcdefghijklmnopqrstuvwxyz_'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	
	static final int NOCLASS_IDX = -1;
	static final int SPACE_IDX = 0;
	static final int PUNKTUATION_IDX = 1;
	static final int SYMBOLIC_IDX = 2;
	static final int NUMERIC_IDX = 3;
	static final int ALPHANUMERIC_IDX = 4;
	
	// Parser
	static final String FALSE = "False";
	static final String TRUE = "True";
	static final String BRACKETO = "(";
	static final String BRACKETC = ")";
	static final String NOT = "~";
	static final String ENTAIL = "|=";
	static final String AND = "/\\";
	static final String OR = "\\/";
	static final String IMPLIES = "==>";
	static final String BICONDITIONAL = "<==>";
	
}
