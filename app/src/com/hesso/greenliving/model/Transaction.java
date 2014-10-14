package com.hesso.greenliving.model;

import java.util.Date;

public class Transaction {
	private BudgetEntry budgetEntry;
	private long id;
	private Date date;
	private double amount;

	public long getId() {
		return this.id;
	}

	public BudgetEntry getBudgetEntry() {
		return this.budgetEntry;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setBudgetEntry(BudgetEntry budgetEntry) {
		this.budgetEntry = budgetEntry;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getAmount() {
		return this.amount;
	}
}
