package model.operator;

import model.Formula;

public class Boolean extends Formula<String> {

	public Boolean(String form) {
		super(form);
	}

	@Override
	public boolean isCnf() {
		return true;
	}
	
}
