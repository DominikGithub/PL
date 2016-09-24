package top.misc;

/**
 * Generates a set of all combinations of 
 * a binary sequence of specified length 
 * @author Dominik
 */
public class PermutationGenerator {

	private int[] set;
	private boolean firstCall;
	
	/**
	 * Ctor.
	 * @param len
	 */
	public PermutationGenerator(int len){
		this.set = new int[len];
		this.firstCall = true;
	}
	
	/**
	 * check whether there are further combinations available
	 * @return true if available
	 */
	public boolean hasNext(){
		for(int i=0; i<set.length; i++)
			if(this.set[i] == 0)
				return true;
		return false;
	}
	
	/**
	 * get next combination
	 * @return set
	 */
	public int[] next(){
		int len = this.set.length;
		if(!this.firstCall) {
			int i=len-1;
			for(; i>=0; i--){
				if(this.set[i] == 0){
					this.set[i] = 1;
					break;
				}
			}
			for(i++; i<this.set.length; i++)
				this.set[i] = 0;
		}
		this.firstCall = false;
//		printSet();
		int[] dest = new int[len];
		System.arraycopy(this.set, 0, dest, 0, len);
		return dest;
	}
	
	/**
	 * print out last set
	 */
	private void printSet(){
		String s = "";
		for(int in : this.set)
			s += in+"";
		System.out.println(s);
	}
}
