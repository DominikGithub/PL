package model.operator;

import top.valuation.Valuation;

/**
 * Boolean False 
 * @author Dominik
 */
public class False extends Boolean {

	/**
	 * Ctor.
	 * @param form
	 */
	public False() {
		super("False");
	}

	@Override
	public String toString() {
		return "FALSE";
	}
	
	@Override
	public boolean eval(Valuation v) {
		return false;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		 return obj instanceof False;
	}
}
