package model.operator;

import model.BinarySentence;
import model.Formula;
import top.misc.ICnfConvertable;
import top.valuation.Valuation;

public class Not extends Formula<Object> implements ICnfConvertable {

	public Not(Formula<?> form) {
		super(form);
		if(this.value instanceof Not)
			this.value = new Group(form);
	}

	@Override
	public String toString() {
		return "~"+this.value.toString();
	}
	
	@Override
	public boolean eval(Valuation v) {
		return !((Formula<?>)this.value).eval(v);
	}

	/**
	 * convert: 
	 * De Morgan left
	 * double-negation left
	 */
	@Override
	public Formula<?> convert() {
		this.value = ((Formula) this.value).convert();
		if (this.value instanceof Group){
			Formula<?> inner = (Formula<?>) ((Group<?>) this.value).getValue(); 
			// double-negation
			if (inner instanceof Not){
				Formula<?> positive = ((Formula<?>) ((Group<?>) this.value).getValue()).convert();
				return (Formula<?>) positive.getValue();
			} else if(inner instanceof BinarySentence){
				Formula<?> left = ((Formula) ((BinarySentence)inner).getValue()).convert();
				Formula<?> right = ((Formula) ((BinarySentence)inner).getRight()).convert();
				// De morgan (Not re-/unwrap Atom)
				if(left instanceof Not) 		left = (Formula<?>) ((Not)left).getValue(); 
				else							left = new Not(left);
				if(right instanceof Not)		right = (Formula<?>) ((Not)right).getValue(); 
				else							right = new Not(right);
				if(inner instanceof And)		return new Group(new Or(left.convert(), right.convert()));
				else if(inner instanceof Or)	return new Group(new And(left.convert(), right.convert()));
			}
		} 
		if(RESOLVE_BOOLEANS){
			if (this.value instanceof True)	return new False();
			if (this.value instanceof False)return new True();
		}
		return this;
	}
	
	@Override
	public boolean isCnf() {
		return this.getValue() instanceof Atom;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(!(obj instanceof Not)) return false;
		return this.value.equals(((Not)obj).getValue());
	}
}
