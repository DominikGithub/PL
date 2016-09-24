package model.operator;

import java.util.ArrayList;
import java.util.Iterator;

import model.Formula;
import top.misc.ICnfConvertable;
import top.valuation.Valuation;

public class Group<L> extends Formula<L> implements ICnfConvertable {

	private ArrayList<L> inner;
	
	/**
	 * Ctor.
	 */
	public Group() {
		super(null);
		this.inner = new ArrayList<L>();
	}
	
	/**
	 * Ctor.
	 * @param element
	 */
	public Group(L element) {
		this();
		this.inner.add(element);
	}

	/**
	 * append Formula to group
	 * @param tok
	 */
	public void add(L tok) {
		this.inner.add(tok);
	}
	
	/**
	 * pop last group element
	 * @return Formula
	 */
	public L popLast(){
		L last = getValue();
		int len = this.inner.size();
		this.inner.remove(len-1);
		return last;
	}
	
	/**
	 * get last group element
	 * @return Formula
	 */
	@Override
	public L getValue() {
		int len = this.inner.size();
		return this.inner.get(len-1);
	}
	
	/**
	 * get group content as list
	 * @return list
	 */
	public ArrayList<L> asList(){
		return this.inner;
	}
	
	/**
	 * check if group is empty
	 * @return true if more than 0 elements
	 */
	public boolean isEmpty(){
		return this.inner.size() < 1;
	}
	
	@Override
	public String toString() {
		return this.inner.toString();
	}
	
	@Override
	public boolean eval(Valuation v) {
		for(L s : inner)
			if(!((Formula<?>)s).eval(v))			//TODO is this correct? functions like AND operator...
				return false;
		return true;
	}

	@Override
	public Formula<?> convert() {
		if(!isEmpty()){
			L ele = (L) ((Formula<?>)this.popLast()).convert();
			if(ele instanceof True || ele instanceof False || ele instanceof Atom)
				return (Formula<?>) ele;
			this.inner.add(ele);
		}
		return this;
	}

	/**
	 * collection of Or
	 */
	@Override
	public boolean isCnf() {
		for(L f : inner)
			if(!(f instanceof Not || f instanceof Or)) return false;
		return true;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(!(obj instanceof Group)) return false;
		Group<?> ext = (Group<?>) obj;
		Iterator<?> extLiIter = ext.asList().iterator(), thisIter = this.inner.iterator();
		while(thisIter.hasNext() && extLiIter.hasNext())
			if(!thisIter.next().equals(extLiIter.next())) return false;		//TODO missing: value==right && right==value as well?????
		return true;
	}
}
