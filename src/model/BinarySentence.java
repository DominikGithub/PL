package model;

/**
 * Implements a binary formula
 * @author Dominik
 * @param <L>
 * @param <R>
 */
public abstract class BinarySentence<L, R> extends Formula {

	protected R right;
	
	/**
	 * Ctor.
	 * @param left
	 * @param right
	 */
	public BinarySentence(L left, R right) {
		super(left);
		this.right = right;
	}
	
	/**
	 * get right side
	 * @return Formula
	 */
	public R getRight(){
		return this.right;
	}
	
	@Override
	public Formula<?> convert() {
		this.value = ((Formula<?>) this.value).convert();
		this.right = (R) ((Formula<?>)this.right).convert();
		return this;
	}
	
	@Override
	public boolean isCnf() {
		return ((Formula<?>)this.value).isCnf() && ((Formula<?>)this.right).isCnf();
	}
}
