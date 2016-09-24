package model.operator;

import model.BinarySentence;
import model.Formula;
import top.valuation.Valuation;

public class Implies<L ,R> extends BinarySentence<L ,R> {

	public Implies(L left, R right) {
		super(left, right);
	}
	
	@Override
	public String toString() {
		return this.value.toString()+" ==> "+this.right.toString();
	}
	
	@Override
	public boolean eval(Valuation v) {
		return (!((Formula<?>)this.value).eval(v)) || ((Formula<?>)this.right).eval(v);
	}

	/**
	 * converts: 
	 * contrapos: ~A ==> ~B to A ==> B DISABLED!
	 * implies: A ==> B to ~A \/ B
	 */
	@Override
	public Formula<?> convert() {
		super.convert();
//		if(this.value instanceof Not && this.right instanceof Not){
//			this.value = ((Not)this.value).getValue();
//			this.right = (R) ((Not)this.right).getValue();
//		}		
		Not not = new Not((Formula<?>) this.value);
		Or<Not, R> or = new Or<Not, R>(not, this.right);
		return or.convert();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(!(obj instanceof Implies)) return false;
		Implies<?, ?> ext = (Implies<?, ?>) obj;
		return this.value.equals(ext.getValue()) && this.right.equals(ext.getRight());
	}

}
