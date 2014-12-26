package com.hesso.greenliving.model;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable (tableName = "budgets" )
public class Budget extends Entity {
    private static final long serialVersionUID = 8240495679280473832L;

    private static final BigDecimal DEFAULT_TARGET = new BigDecimal( 0 );
    private static final int DEFAULT_DAY_OF_MONTH = 25;

    private static Budget instance;

    public static Budget getInstance() {
	if( instance == null ) {
	    instance = new Budget();
	}

	return instance;
    }

    @DatabaseField (canBeNull = false )
    private int dayOfMonth;

    @DatabaseField (canBeNull = false )
    private BigDecimal target = new BigDecimal( 0 );

    @ForeignCollectionField
    private Collection<BudgetEntry> entries;

    private Budget() {
	this.setDayOfMonth( DEFAULT_DAY_OF_MONTH );
	this.setTarget( DEFAULT_TARGET );
    }

    public Set<BudgetEntry> getEntries() {
	return new HashSet<BudgetEntry>( this.entries );
    }

    public boolean addEntry( BudgetEntry entry ) {
	boolean res = this.entries.add( entry );

	if( res )
	    this.setChanged();

	return res;
    }

    public boolean removeEntry( BudgetEntry entry ) {
	boolean res = this.entries.remove( entry );

	if( res )
	    this.setChanged();

	return res;
    }

    public int getDayOfMonth() {
	return dayOfMonth;
    }

    public void setDayOfMonth( int dayOfMonth ) {
	if( this.dayOfMonth != dayOfMonth ) {
	    this.dayOfMonth = dayOfMonth;
	    this.setChanged();
	}
    }

    public BigDecimal getTarget() {
	return target;
    }

    public void setTarget( BigDecimal amount ) {
	if( !this.target.equals( amount ) ) {
	    this.target = amount;
	    this.setChanged();
	}
    }

    public Collection<Transaction> getTransactions() {
	Collection<Transaction> res = new LinkedList<Transaction>();

	for( BudgetEntry entry : this.entries ) {
	    res.addAll( entry.getOutgoingTransactions() );
	}

	return res;
    }

    public void setId( long id ) {
	super.setId( id );
    }
}
