package top.valuation;

import java.util.HashMap;

import model.operator.Atom;
import model.operator.Boolean;
import model.operator.False;
import model.operator.True;

/**
 * Variable value assignment 
 * @author Dominik
 */
public class Valuation extends HashMap<String, Boolean> {

	private final String SEPERATOR = " ";
	
	/**
	 * put a list of variables
	 * @param vars
	 * @param bools
	 */
	public void put(Atom[] vars, int[] bools){
		for(int i=0; i<vars.length; i++)
			put(vars[i].toString(), (bools[i]==0?new False():new True()));
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for(Entry<String, Boolean> entry : entrySet())
			builder.append(entry.getKey()+" -> "+entry.getValue().toString()+SEPERATOR);
		String s = builder.toString();
		return s.substring(0, s.length()-SEPERATOR.length());
	}
	
	@Override
	public boolean equals(Object arg0) {
		if(!(arg0 instanceof Valuation)) return false;
		Valuation ext = (Valuation) arg0;
		boolean r = entrySet().equals(ext.entrySet());
		return r;
//		for(Entry<String, Boolean> e : entrySet()){
//			Boolean bool = e.getValue(); 
//			if(!bool.equals(ext.entrySet()))
//				return false;	//TODO something like that....
//		}
//		return true;
	}
}
