package top;
import java.io.IOException;
import java.util.ArrayList;

import top.misc.IConstNames;

/**
 * Lexer 
 * @author Dominik
 */
public class Lexer implements IConstNames {
	
	private ArrayList<String> tokenTypes;
	private ArrayList<String> tokens;
	
	/**
	 * Ctor.
	 */
	public Lexer() {
		this.tokenTypes = new ArrayList<String>();
		this.tokenTypes.add(SPACE_IDX, SPACES);
		this.tokenTypes.add(PUNKTUATION_IDX, PUNCTUATION);
		this.tokenTypes.add(SYMBOLIC_IDX, SYMBOLIC);
		this.tokenTypes.add(NUMERIC_IDX, NUMERIC);
		this.tokenTypes.add(ALPHANUMERIC_IDX, ALPHANUMERIC);
	}

	/**
	 * entry point for lexing the given string into tokens.
	 * @param token
	 * @throws IOException 
	 */
	public ArrayList<String> lex(String token) throws IOException{
		if(token == null) throw new IOException("Token null");
		this.tokens = new ArrayList<String>();
		token = token.trim();
		int prevClass = getCharClass(token.charAt(0));
		int tokenStartIdx = 0;
		for(int c=1; c<token.length(); c++){
			int charClass = getCharClass(token.charAt(c));
			
			// ()[]{} always forced as single char token
			if(charClass != prevClass || charClass == PUNKTUATION_IDX){
				if(prevClass == ALPHANUMERIC_IDX && charClass == NUMERIC_IDX)
					continue;
				if(prevClass != SPACE_IDX) 
					this.tokens.add(token.substring(tokenStartIdx, c));
				tokenStartIdx = c;
				prevClass = charClass;
			}
		}
		this.tokens.add(token.substring(tokenStartIdx));
		return this.tokens;
	}
	
	/**
	 * get the type of a single character
	 * @param c character
	 * @return index of type class or NOCLASS_IDX=-1 if none matches
	 */
	private int getCharClass(char c){
		for(int tokIdx=0; tokIdx<=this.tokenTypes.size(); tokIdx++){
			if(this.tokenTypes.get(tokIdx).contains(c+""))
				return tokIdx;
		}
		return NOCLASS_IDX;
	}
}
