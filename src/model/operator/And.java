package model.operator;

import model.BinarySentence;
import model.Formula;
import top.valuation.Valuation;

public class And<L ,R> extends BinarySentence<L ,R> {

	public And(L left, R right) {
		super(left, right);
	}
	
	@Override
	public String toString() {
		return this.value.toString()+" /\\ "+this.right.toString();
	}
	
	@Override
	public boolean eval(Valuation v) {
		boolean a = ((Formula<?>) this.value).eval(v);
		boolean b = ((Formula<?>) this.right).eval(v);
		return a && b;
	}

	/**
	 * convert:
	 * associativity
	 * A /\ A = A
	 * A /\ True = A
	 * A /\ False = False
	 * A /\ ~A = False
	 * 1. distributivity
	 */
	@Override
	public Formula<?> convert() {
		super.convert();
		Formula<?> locLe = (Formula<?>) this.value;
		Formula<?> locRi = (Formula<?>) this.right;

		// associativity
		if(locLe instanceof Group){
			Formula<?> child = (Formula<?>) ((Group<?>)locLe).getValue();
			if(child instanceof And || child instanceof Atom || child instanceof Not) locLe = child;
		}
		if(locRi instanceof Group){
			Formula<?> child = (Formula<?>) ((Group<?>)locRi).getValue();
			if(child instanceof And || child instanceof Atom || child instanceof Not) locRi = child;
		}
		
		// ungroup variables within same operator chain/hierarchy
		// resolve multiple appearance
		if((locLe instanceof Atom || locLe instanceof Not) && locRi instanceof And) {
			Formula<?> ret = flattenHierarchy(locLe, (And<?,?>)locRi);
			if(ret == null) return locRi;
			locRi = ret;
		} else if((locRi instanceof Atom || locRi instanceof Not) && locLe instanceof And){
			Formula<?> ret = flattenHierarchy(locRi, (And<?,?>)locLe);
			if(ret == null) return locLe; 
			locLe = ret;
		}

		// A /\ A = A
		if(locLe.equals(locRi)) {
			if(locLe instanceof Group)
				return (Formula<?>) ((Group<?>) locLe).getValue();
			return locLe;
		}

		// resolve equivalence to boolean
		if(RESOLVE_BOOLEANS){
			// A /\ True = A
			if(locLe instanceof True) return locRi;
			if(locRi instanceof True) return locLe;
			
			// A /\ False = False
			if(locLe instanceof False || locRi instanceof False) return new False();
			
			// A /\ ~A = False
			if(locLe.equals(new Not(locRi))) return new False();
			if(locRi.equals(new Not(locLe))) return new False();
		}
		
		// 1. distributivity: left to right
		if((locLe instanceof Or 
				|| (locLe instanceof Group && ((Group<?>)locLe).getValue() instanceof Or)) 
			&& !(locRi instanceof Or)){
			return distributivity(locLe, locRi);
		}
		if(!(locLe instanceof Or)
			&& (locRi instanceof Or)
				|| (locRi instanceof Group && ((Group<?>)locRi).getValue() instanceof Or)){
			return distributivity(locRi, locLe);
		}
		this.value = locLe;
		this.right = (R) locRi;
		return this;
	}

	/**
	 * distributivity
	 * @param left
	 * @param right
	 * @return Formula
	 */
	private Formula<?> distributivity(Formula<?> left, Formula<?> right) {
		Formula<?> notOr = (Formula<?>)right;
		Or<?,?> or;
		if(left instanceof Group)	or = (Or<?, ?>) ((Group)left).getValue();
		else 						or = (Or<?,?>)left;
		
		Formula<?> orLeft = (Formula<?>) or.getValue();
		Formula<?> orRight = (Formula<?>) or.getRight();
		And<Formula<?>, Formula<?>> andLeft = new And(notOr, orLeft);
		And<Formula<?>, Formula<?>> andRight = new And(orRight, notOr);
		return new Or(new Group(andLeft), new Group(andRight)).convert();
	}
	
	/**
	 * flatten: grouped ANDs
	 * 
	 * resolve:
	 * A /\ True = A
	 * A /\ False = False
	 * 
	 * @param pattern
	 * @param tree
	 * @return Formula or null if pattern was resolved within the tree 
	 */
	private Formula<?> flattenHierarchy(Formula<?> pattern, And<?, ?> tree){
		Formula<?> le = (Formula<?>) tree.value;
		Formula<?> ri = (Formula<?>) tree.right;
			 if(le instanceof And)	le = flattenHierarchy(pattern, (And<?,?>) le);
		else if(ri instanceof And)	ri = flattenHierarchy(pattern, (And<?,?>) ri);

		if((le instanceof Atom || le instanceof Not) && (ri instanceof Atom || ri instanceof Not)){
			Formula<?> leResolve = new And(pattern, le).convert();
			Formula<?> riResolve = new And(pattern, ri).convert();
			if(RESOLVE_BOOLEANS){
					 if(leResolve instanceof False || riResolve instanceof False) return new False();
				else if(leResolve instanceof True) return riResolve;
				else if(riResolve instanceof True) return leResolve;
			}
			// return null if the pattern is included in the tree already and the pattern can be omitted therfore
			if(leResolve instanceof BinarySentence && !(riResolve instanceof BinarySentence)) return null;
			if(riResolve instanceof BinarySentence && !(leResolve instanceof BinarySentence)) return null;
		}
		return tree;
	}

	/**
	 * @return true if children are groups of Or only
	 */
	@Override
	public boolean isCnf() {
		Formula<?> left = (Formula<?>) this.getValue();
		boolean leftIsWrapped = false, rightIsWrapped = false;
		if(left instanceof Group) {
			left = (Formula<?>) ((Group<?>)left).getValue();
			leftIsWrapped = true;
		}
		Formula<?> right = (Formula<?>) this.getRight();
		if(right instanceof Group){
			right = (Formula<?>) ((Group<?>)right).getValue();
			rightIsWrapped = true;
		}
		return super.isCnf()
			&& (left instanceof Atom  || left instanceof Not  || left instanceof And  || (leftIsWrapped && left instanceof Or)) 
			&& (right instanceof Atom || right instanceof Not || right instanceof And || (rightIsWrapped && right instanceof Or)) 
			&& left.isCnf() && right.isCnf();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(!(obj instanceof And)) return false;
		And<?, ?> ext = (And<?, ?>) obj;
		return this.value.equals(ext.getValue()) && this.right.equals(ext.getRight());	//TODO missing: value==right && right==value ?????
	}
}
