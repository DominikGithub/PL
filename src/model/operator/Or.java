package model.operator;

import model.BinarySentence;
import model.Formula;
import top.valuation.Valuation;

public class Or<L ,R> extends BinarySentence<L ,R> {

	public Or(L left, R right) {
		super(left, right);
	}
	
	@Override
	public String toString() {
		return this.value.toString()+" \\/ "+this.right.toString();
	}
	
	@Override
	public boolean eval(Valuation v) {
		boolean a = ((Formula<?>) this.value).eval(v);
		boolean b = ((Formula<?>) this.right).eval(v);
		return a || b;
	}

	/**
	 * convert: 
	 * associativity
	 * A \/ A = A
	 * A \/ ~A = True
	 * A \/ True = True
	 * 2. distributivity
	 */
	@Override
	public Formula<?> convert() {
		super.convert();
		Formula<?> locLe = (Formula<?>) this.value;
		Formula<?> locRi = (Formula<?>) this.right;

		// associativity
		if(locLe instanceof Group && ((Group)locLe).getValue() instanceof Or)
			locLe = (Formula<?>) ((Group)locLe).getValue();
		if(locRi instanceof Group && ((Group)locRi).getValue() instanceof Or)
			locRi = (Formula<?>) ((Group)locRi).getValue();

		// ungroup variables within same operator chain/hierarchy
		// resolve multiple appearance
		if((locLe instanceof Atom || locLe instanceof Not) && locRi instanceof Or){
			Formula<?> ret = flattenHierarchy(locLe, (Or<?,?>)locRi);
			if(ret == null) return locRi;
			locRi = ret;
		} else if((locRi instanceof Atom || locRi instanceof Not) && locLe instanceof Or){
			Formula<?> ret = flattenHierarchy(locRi, (Or<?,?>)locLe);
			if(ret == null) return locLe;
			locLe = ret;
		}

		// A \/ A = A
		if(locLe.equals(locRi)) {
			if(locLe instanceof Group)
				return (Formula<?>) ((Group) locLe).getValue();
			return locLe;
		}
		
		if(RESOLVE_BOOLEANS){
			if(locLe instanceof False) return locRi; 
			if(locRi instanceof False) return locLe;

			// A \/ ~A = True
			if(locLe.equals(new Not(locRi))) return new True();
			if(locRi.equals(new Not(locLe))) return new True();
			
			// A \/ True = True
			if(locLe instanceof True || locRi instanceof True) return new True();
		}
		
		//handle wrapped group
		if(locLe instanceof Group){
			Formula<?> child = (Formula<?>) ((Group)locLe).getValue();
			if(child instanceof Or || child instanceof Atom || child instanceof Not)
				locLe = child;
			// commutativity: swap to last distributivity order (A OR AND)
//			if(locLe instanceof And){
//				Formula<?> tmp = (Formula<?>) locRi;
//				locRi = (Formula<?>) locLe;
//				locLe = tmp;
//			}
		}
		if(locRi instanceof Group){ 
			Formula<?> child = (Formula<?>) ((Group)locRi).getValue();
			if(child instanceof Or || child instanceof Atom || child instanceof Not)
				locRi = child;
//			if(child instanceof And)
//				locRi = child;
		}
		
		// 2. distributivity: left to right
		if((locLe instanceof And 
				|| (locLe instanceof Group && ((Group)locLe).getValue() instanceof And))
			&& !(locRi instanceof And)){
			return distributivity(locLe, locRi);
		}
		if(!(locLe instanceof And) 
			&& (locRi instanceof And 
					|| (locRi instanceof Group && ((Group)locRi).getValue() instanceof And))){
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
		Formula<?> notAnd = (Formula<?>)right;
		And<?,?> and;
		if(left instanceof Group)	and = (And<?, ?>)((Group)left).getValue();
		else 						and = (And<?,?>)left;
		
		Formula<?> andLeft = (Formula<?>) and.getValue();
		Formula<?> andRight = (Formula<?>) and.getRight();
		Or orLeft = new Or(notAnd, andLeft);
		Or orRight = new Or(andRight, notAnd);
		return new And(new Group(orLeft), new Group(orRight)).convert();
	}
	
	/**
	 * flatten and resolve:
	 * A \/ False = A
	 * A \/ True = True
	 * A \/ ~A = True		missing!
	 * 
	 * @param pattern
	 * @param tree
	 * @return Formula or null if pattern was resolved within the tree 
	 */
	private Formula<?> flattenHierarchy(Formula<?> pattern, Or tree){
		Formula<?> le = (Formula<?>) tree.value;
		Formula<?> ri = (Formula<?>) tree.right;
			 if(le instanceof Or)	le = flattenHierarchy(pattern, (Or<?,?>) le);
		else if(ri instanceof Or)	ri = flattenHierarchy(pattern, (Or<?,?>) ri);
		
		if((le instanceof Atom || le instanceof Not) && (ri instanceof Atom || ri instanceof Not)){
			Formula<?> leResolve = new Or(pattern, le).convert();
			Formula<?> riResolve = new Or(pattern, ri).convert();
			if(RESOLVE_BOOLEANS){
					 if(leResolve instanceof True || riResolve instanceof True) return new True();
				else if(leResolve instanceof False) return riResolve;
				else if(riResolve instanceof False) return leResolve;
			}
			// return null if the pattern is included in the tree already and the pattern can be omitted therfore
			if(leResolve instanceof BinarySentence && !(riResolve instanceof BinarySentence)) return null;
			if(riResolve instanceof BinarySentence && !(leResolve instanceof BinarySentence)) return null;
		}
		return tree;
	}

	/**
	 * @return true if children are unwrapped Or or Atom or Not only
	 */
	@Override
	public boolean isCnf() {
		Formula<?> left = (Formula<?>) this.getValue();
		Formula<?> right = (Formula<?>) this.getRight();
		return super.isCnf()
			&& (left instanceof Atom  || left instanceof Not  || left instanceof Or)
			&& (right instanceof Atom || right instanceof Not || right instanceof Or)
			&& left.isCnf() 
			&& right.isCnf();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(!(obj instanceof Or)) return false;
		Or<?, ?> ext = (Or<?, ?>) obj;
		return this.value.equals(ext.getValue()) && this.right.equals(ext.getRight());	//TODO missing: value==right && right==value ?????
	}

}
