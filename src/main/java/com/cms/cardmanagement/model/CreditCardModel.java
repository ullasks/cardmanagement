package com.cms.cardmanagement.model;

import java.math.BigDecimal;

public class CreditCardModel {

private String name;
	
	private String number;
	
	//New cards start with a £0 balance
	private BigDecimal balance =  BigDecimal.ZERO;

	private BigDecimal limit;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public BigDecimal getLimit() {
		return limit;
	}

	public void setLimit(BigDecimal limit) {
		this.limit = limit;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
}
