package top.misc;

/**
 * Helper flag to switch boolean resolution on/off
 * @author Dominik
 */
public interface IBooleanResolveFlag {

	/**
	 * set this flag to false to prevent resolution of equivalence rules to boolean
	 * A /\ ~A = False
	 * A /\ False = False
	 * A \/ True = True
	 * A \/ ~A = True
	 */
	static boolean RESOLVE_BOOLEANS = true;
}
