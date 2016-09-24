package top.misc;

import top.valuation.Valuation;

public interface IEvaluate {

	/**
	 * Evaluates a formula under valuations v
	 * @param v
	 * @return boolean
	 */
	public boolean eval(Valuation v);

}
