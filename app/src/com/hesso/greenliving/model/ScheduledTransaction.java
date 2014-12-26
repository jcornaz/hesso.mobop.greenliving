package com.hesso.greenliving.model;

import java.math.BigDecimal;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable (tableName = "scheduled_transactions" )
public class ScheduledTransaction extends Entity {

    private static final long serialVersionUID = 7070488132832129999L;

    @DatabaseField (canBeNull = false, foreign = true )
    private BudgetEntry entry;

    @DatabaseField (canBeNull = false )
    private int dayOfMonth;

    @DatabaseField (canBeNull = false )
    private BigDecimal amount;

    public BudgetEntry getEntry() {
	return entry;
    }

    public void setEntry( BudgetEntry entry ) {
	this.entry = entry;
    }

    public ScheduledTransaction() {
    }

    public ScheduledTransaction( BudgetEntry entry, int dayOfMonth, BigDecimal amount ) {
    }

    public int getDayOfMonth() {
	return dayOfMonth;
    }

    public void setDayOfMonth( int dayOfMonth ) {
	this.dayOfMonth = dayOfMonth;
    }

    public BigDecimal getAmount() {
	return amount;
    }

    public void setAmount( BigDecimal amount ) {
	this.amount = amount;
    }
}
