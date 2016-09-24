package model.operator;

import java.util.ArrayList;

import model.BinarySentence;
import model.Formula;
import top.valuation.Valuation;
import top.valuation.ValuationSet;

public class Entails<L, R> extends BinarySentence<L, R> {

	public Entails(L left, R right) {
		super(left, right);
	}
	
	@Override
	public String toString() {
		return this.value+" |= "+this.right.toString();
	}
	
	/**
	 * KB |= a (equal to any of the following)
	 * - a is true in every model in which KB is true
	 * - KB => a is a tautology
	 * - KB /\ ~a is unsatisfiable
	 * @param notUsed here not used at all, but required by interface
	 * @return boolean
	 */
	@Override
	public boolean eval(Valuation notUsed) {
		ArrayList<Valuation> kbTrue = new ArrayList<Valuation>();
		Formula<?> kb = (Formula<?>) this.value;
		Formula<?> alpha = (Formula<?>) this.right;

		if(kb instanceof False) return true;
		if(kb instanceof True) 	return false;

		//collect all models in which KB is true
		for(Valuation valu : ValuationSet.getValuations(kb))
			if(kb.eval(valu)) kbTrue.add(valu);
		
		//check if alpha is true in all models in which KB is true
		for(Valuation valu : kbTrue)
			if(!alpha.eval(valu)) 
				return false;
		return true;
	}

	@Override
	public Formula<?> convert() {
		return eval(null)?new True(): new False();
	}
}
