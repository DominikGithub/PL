package model;

import model.operator.Atom;
import model.operator.False;
import model.operator.True;
import top.misc.IBooleanResolveFlag;
import top.misc.ICnfConvertable;
import top.misc.IEvaluate;
import top.valuation.Valuation;

/**
 * Generic formula
 * @author Dominik
 */
public abstract class Formula<L> implements IEvaluate, ICnfConvertable, IBooleanResolveFlag {
	
	protected L value;
	
	/**
	 * Ctor.
	 * @param form
	 */
	public Formula(L form) {
		this.value = form;
	}
	
	@Override
	public String toString() {
		return value.toString();
	}
	
	/**
	 * get value
	 * @return value
	 */
	public L getValue(){
		if(this instanceof Atom) return (L) this;
		return this.value;
	}

	@Override
	public boolean eval(Valuation v) {
		return ((IEvaluate)this.value).eval(v);
	}

	@Override
	public Formula convert() {
		if(this instanceof False || this instanceof True) return this;
		if(!(this.value instanceof Atom)) return ((Formula<?>)this.value).convert();
		if(!((Formula<?>) this.value).isCnf()) 
			this.value = (L) ((Formula<?>) this.value).convert();
		return this;
	}
}
