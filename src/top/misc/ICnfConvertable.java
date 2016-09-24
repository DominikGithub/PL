package top.misc;

import model.Formula;

/**
 * Convertable to simple expression 
 * @author Dominik
 */
public interface ICnfConvertable {

	/**
	 * reduce formula to next lower logical expression
	 * @return Formula
	 */
	public Formula<?> convert();
	
	/**
	 * check if element is CNF
	 * @return
	 */
	public boolean isCnf();
}
