package top.valuation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import model.BinarySentence;
import model.Formula;
import model.operator.Atom;
import model.operator.False;
import model.operator.Group;
import model.operator.Not;
import model.operator.True;
import top.misc.PermutationGenerator;

/**
 * static tests for:
 * - tautology
 * - satisfiable
 * - unsatisfiable
 * @author Dominik
 */
public class ValuationSet {

	private static Set<Atom> variables;
	
	/**
	 * Formula is valid/tautology if it is true in ALL models
	 * A is valid if ~A is unsatisfiable
	 * @param formula
	 * @return true if all valuations give true
	 */
	public static boolean tautology(Formula<?> formula){
		Iterator<Valuation> vIter = getValuations(formula).iterator();
		while(vIter.hasNext()){
			if(!formula.eval(vIter.next()))
				return false;
		}
		return true;
	}
	
	/**
	 * Formula is satisfiable if it is true in SOME model
	 * @param formula
	 * @return true if at least one valuation gives true
	 */
	public static boolean satisfiable(Formula<?> formula){
		for(Valuation valu : getValuations(formula))
			if(formula.eval(valu)) return true;
		return false;
	}

	/**
	 * Formula is satisfiable if it is true in NO model
	 * @param formula
	 * @return true if no valuation gives true
	 */
	public static boolean unsatisfiable(Formula<?> formula){
		for(Valuation valu : getValuations(formula))
			if(formula.eval(valu)) return false;
		return true;
	}
	
	/**
	 * create all permuations of valuations
	 * @param formula
	 * @return valuation list
	 */
	public static ArrayList<Valuation> getValuations(Formula<?> formula) {
		ArrayList<Valuation> list;
		ArrayList<int[]> perm;
		Atom[] varNameArr;
		variables = new TreeSet<Atom>();
		collectVariables(formula);
		list = new ArrayList<Valuation>();
		perm = permutations(variables.size());
		varNameArr = new Atom[variables.size()];
		variables.toArray(varNameArr);
		for(int[] pattern : perm){
			Valuation v = new Valuation();
			v.put(varNameArr, pattern);
			list.add(v);
		}
		return list;
	}
	
	/**
	 * collect variables
	 * (as per exercise description this method is called atoms)
	 * @param f
	 */
	private static void collectVariables(Formula<?> f){
		ArrayList<Formula<?>> list;
		Object val = f.getValue();
			 if(f instanceof True || f instanceof False) return;
		else if (f instanceof BinarySentence) {
				collectVariables((Formula<?>) ((BinarySentence<?, ?>) f).getValue());
				collectVariables((Formula<?>) ((BinarySentence<?, ?>) f).getRight());
	  } else if(val instanceof Atom) variables.add((Atom) val);
		else if(f instanceof Not) 
			collectVariables((Formula<?>) ((Not)f).getValue());
		else if (f instanceof Group) {
			list = ((Group)f).asList();
			for(Formula<?> c : list)
				collectVariables(c);
		}
	}
	
	/**
	 * generate all sequences of binary variables
	 * of the given length
	 * @param len
	 * @return binary arrays
	 */
	private static ArrayList<int[]> permutations(int len){
		ArrayList<int[]> sets = new ArrayList<int[]>();
		PermutationGenerator gen = new PermutationGenerator(len);
		while(gen.hasNext())
			sets.add(gen.next());
		return sets;
	}
}
