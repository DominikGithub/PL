package model.operator;

import model.BinarySentence;
import model.Formula;
import top.valuation.Valuation;

/**
 * biconditional
 * @author Dominik
 * @param <L>
 * @param <R>
 */
public class Biconditional<L, R> extends BinarySentence<L, R> {

	public Biconditional(L left, R right) {
		super(left, right);
	}
	
	@Override
	public String toString() {
		return this.value.toString()+" <==> "+this.right.toString();
	}
	
	@Override
	public boolean eval(Valuation v) {
		Formula<?> s1 = new Implies((Formula<?>)this.value, this.right);
		Formula<?> s2 = new Implies(this.right, (Formula<?>)this.value);
		return s1.eval(v) && s2.eval(v);
	}

	/**
	 * convert:
	 * A <==> B to (A ==> B) /\ (B ==> A)
	 */
	@Override
	public Formula convert() {
		super.convert();
		Implies lImpl = new Implies(this.value, this.right);
		Implies rImpl = new Implies(this.right, this.value);
		And and = new And(new Group(lImpl), new Group(rImpl));
		return and.convert();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(!(obj instanceof Biconditional)) return false;
		Biconditional<?, ?> ext = (Biconditional<?, ?>) obj;
		return this.value.equals(ext.getValue()) && this.right.equals(ext.getRight());	//TODO missing: value==right && right==value as well?????
	}
}
