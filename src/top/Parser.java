package top;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;

import model.Formula;
import model.operator.And;
import model.operator.Atom;
import model.operator.Biconditional;
import model.operator.Entails;
import model.operator.False;
import model.operator.Group;
import model.operator.Implies;
import model.operator.Not;
import model.operator.Or;
import model.operator.True;
import top.misc.GroupEnd;
import top.misc.IConstNames;

/**
 * Parser 
 * @author Dominik
 * NOTE: fails to parse group in a group followed by something
 */
public class Parser implements IConstNames {
	
	private ArrayList<String> symbols;
	private ListIterator<String> iter;
	private String tok;
	private int grCounter = 0;
	
	/**
	 * Ctor.
	 * @throws IOException 
	 */
	public Parser(ArrayList<String> tokens) throws IOException{
		if(tokens == null) throw new IOException("Token list null");
		iter = tokens.listIterator();
		symbols = new ArrayList<String>();
		symbols.add(ENTAIL);
		symbols.add(BICONDITIONAL);
		symbols.add(IMPLIES);
		symbols.add(OR);
		symbols.add(AND);
		symbols.add(NOT);
		symbols.add(TRUE);
		symbols.add(FALSE);
		symbols.add(BRACKETO);
	}
	
	/**
	 * parse 
	 * @return Formula
	 * @throws IOException
	 */
	public Formula<?> parse() throws IOException {
		Formula<?> previous = null; 
		try {
			while(iter.hasNext())
				previous = complexSentence(previous);
		} catch (IOException ex){/**/}
		return previous;
	}
		
	/**
	 * parse a complex sentence 
	 * @return Formula or null
	 * @throws GroupEnd 
	 */
	private Formula<?> complexSentence(Formula<?> previous) throws IOException {
		if(!iter.hasNext()) throw new IOException("Token list empty");
		tok = iter.next();
			 if(BRACKETO.equals(tok)) 	return group();
			 if(BRACKETC.equals(tok)) 	throw new GroupEnd();
			 if(NOT.equals(tok)) 		return new Not(complexSentence(previous));
		else if(ENTAIL.equals(tok)) 	return new Entails(previous, sentence(previous));
		else if(AND.equals(tok)) 		return new And(previous, sentence(previous));
		else if(OR.equals(tok)) 		return new Or(previous, sentence(previous));
		else if(IMPLIES.equals(tok)) 	return new Implies(previous, sentence(previous));
		else if(BICONDITIONAL.equals(tok)) return new Biconditional(previous, sentence(previous));
	    return sentence(previous);
	}

	/**
	 * parse a sentence
	 * @return Formula
	 */
	private Formula<?> sentence(Formula<?> previous) throws IOException {
			 if(!this.symbols.contains(tok) && !BRACKETC.equals(tok)) return new Atom(tok);
		else if(TRUE.equalsIgnoreCase(tok)) return new True();
		else if(FALSE.equalsIgnoreCase(tok)) return new False();
		return complexSentence(previous);
	}
	
	/**
	 * parse group
	 */
	private Formula<?> group() throws IOException {
		Formula<?> previous = null;
		this.grCounter++;
		Group g = new Group();
		while(!BRACKETC.equals(tok) || this.grCounter > 0){
			if(!g.isEmpty()) previous = (Formula<?>) g.popLast();
			try {
				previous = complexSentence(previous);
			} catch(GroupEnd e){
				this.grCounter--;
				break;
			} finally {
				g.add(previous);
			}
		}
		return g;
	}
}
