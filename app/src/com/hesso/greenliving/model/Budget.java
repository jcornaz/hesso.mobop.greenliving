package com.hesso.greenliving.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class Budget {
	private int dayOfMonth;
	private BigDecimal amount = new BigDecimal(0);
	private Set<BudgetEntry> entries = new HashSet<BudgetEntry>();
}
