package com.hesso.greenliving.dao;

import com.hesso.greenliving.exception.NotImplementedException;
import com.hesso.greenliving.model.Budget;
import com.hesso.greenliving.model.BudgetEntry;
import com.hesso.greenliving.model.ScheduledTransaction;
import com.hesso.greenliving.model.Transaction;
import com.hesso.greenliving.model.Transfert;

public final class DAOManager {

	private DAOManager() {
	}

	public static void save(Budget budget) {
		throw new NotImplementedException();
	}

	public static void save(BudgetEntry entry) {
		throw new NotImplementedException();
	}

	public static void save(Transaction transaction) {
		throw new NotImplementedException();
	}

	public static void save(ScheduledTransaction transaction) {
		throw new NotImplementedException();
	}

	public static void save(Transfert transfert) {
		save((Transaction) transfert);
	}

	public static void delete(Budget budget) {
		throw new NotImplementedException();
	}

	public static void delete(BudgetEntry entry) {
		throw new NotImplementedException();
	}

	public static void delete(Transaction transaction) {
		throw new NotImplementedException();
	}

	public static void delete(ScheduledTransaction transaction) {
		throw new NotImplementedException();
	}

	public static void delete(Transfert transfert) {
		delete((Transaction) transfert);
	}

	public static Budget loadBudget() {
		throw new NotImplementedException();
	}
}
