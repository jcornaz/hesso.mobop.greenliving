package com.hesso.greenliving.model;

public class Transfert extends Transaction {
	private BudgetEntry destinationEntry;

	public BudgetEntry getDestinationEntry() {
		return this.destinationEntry;
	}

	public void setDestinationEntry(BudgetEntry destinationEntry) {
		this.destinationEntry = destinationEntry;
	}
}
