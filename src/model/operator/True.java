package model.operator;

import top.valuation.Valuation;

/**
 * Boolean True 
 * @author Dominik
 */
public class True extends Boolean {

	public True() {
		super("True");
	}
	
	@Override
	public String toString() {
		return "TRUE";
	}
	
	@Override
	public boolean eval(Valuation v) {
		return true;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		 return obj instanceof True;
	}
}
