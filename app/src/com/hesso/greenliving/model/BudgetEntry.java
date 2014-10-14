package com.hesso.greenliving.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class BudgetEntry {
	private String name;
	private double targetAmount;
	private Collection<ScheduledTransaction> scheduledTransactions = new HashSet<ScheduledTransaction>();
	private List<Transaction> transactions = new LinkedList<Transaction>();

	public String getName() {
		return this.name;
	}

	public double getTargetAmount() {
		return targetAmount;
	}

	public void setTargetAmount(double targetAmount) {
		this.targetAmount = targetAmount;
	}

	public Collection<ScheduledTransaction> getScheduledTransactions() {
		return new HashSet<ScheduledTransaction>(this.scheduledTransactions);
	}

	public void setScheduledTransactions(Collection<ScheduledTransaction> scheduledTransactions) {
		this.scheduledTransactions.clear();
		this.scheduledTransactions.addAll(scheduledTransactions);
	}

	public List<Transaction> getTransactions() {
		return new LinkedList<Transaction>(this.transactions);
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions.clear();
		this.transactions.addAll(transactions);
	}

	public void setName(String name) {
		this.name = name;
	}
}
