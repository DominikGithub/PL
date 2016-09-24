package model.operator;

import model.Formula;
import top.misc.ICnfConvertable;
import top.valuation.Valuation;

/**
 * Atomic value representation
 * @author Dominik
 */
public class Atom  extends Formula<String> implements Comparable<Atom>, ICnfConvertable {

	private String value;
	
	/**
	 * Ctos.
	 * @param form
	 */
	public Atom(String form) {
		super(form);
		this.value = form;
	}
	
	@Override
	public String toString() {
		return value.toString();
	}

	@Override
	public int compareTo(Atom a) {
		return this.value.equals(a.value)?0:1;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Atom)) return false;
		return this.value.equals(((Atom)obj).value);
	}
	
	@Override
	public boolean eval(Valuation v){
		return v.get(this.value).eval(v);
	}

	@Override
	public Atom convert() {
		return this;
	}

	@Override
	public boolean isCnf() {
		return true;
	}
}
