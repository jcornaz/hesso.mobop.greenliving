package com.hesso.greenliving.model;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class BudgetEntry {
	private String name;
	private double targetAmount;
	private Set<ScheduledTransaction> scheduledTransactions = new HashSet<ScheduledTransaction>();
	private List<Transaction> transactions = new LinkedList<Transaction>();
}
