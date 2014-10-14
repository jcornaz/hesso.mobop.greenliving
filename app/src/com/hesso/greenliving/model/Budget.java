package com.hesso.greenliving.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class Budget {
	private int dayOfMonth;
	private BigDecimal amount = new BigDecimal(0);
	private Set<BudgetEntry> entries = new HashSet<BudgetEntry>();

	public Set<BudgetEntry> getEntries() {
		return new HashSet<BudgetEntry>(this.entries);
	}

	public void setEntries(Set<BudgetEntry> entries) {
		this.entries = new HashSet<BudgetEntry>(entries);
	}

	public boolean addEntry(BudgetEntry entry) {
		return this.entries.add(entry);
	}

	public boolean removeEntry(BudgetEntry entry) {
		return this.entries.remove(entry);
	}

	public int getDayOfMonth() {
		return dayOfMonth;
	}

	public void setDayOfMonth(int dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}
