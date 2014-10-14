package com.hesso.greenliving.model;

import java.util.Date;

public class Transaction {
	private BudgetEntry from;
	private BudgetEntry to;
	private long id;
	private Date date;
	private double amount;

	public long getID() {
		return this.id;
	}

	public BudgetEntry getBudgetEntry() {
		return this.from;
	}

	public double getAmount() {
		return this.amount;
	}

	public BudgetEntry getBudgetEntryDestination() {
		return this.to;
	}
}
