package com.hesso.greenliving.ui;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import android.content.Context;

import com.hesso.greenliving.R;
import com.hesso.greenliving.model.Account;
import com.hesso.greenliving.model.Budget;
import com.hesso.greenliving.model.Transaction;

public class AccountDisplayer extends Account {
    private static final long serialVersionUID = 144503805493560225L;

    public static List<AccountDisplayer> convert( Context context, Collection<Account> accounts ) {
	List<AccountDisplayer> res = new LinkedList<AccountDisplayer>();

	for( Account account : accounts ) {
	    res.add( new AccountDisplayer( context, account ) );
	}

	return res;
    }

    public static String toString( Context context, Account account ) {
	return account.isOffBudget() ? context.getString( R.string.off_budget ) : account.getName();
    }

    private Account account;
    private Context context;

    public AccountDisplayer( Context context, Account account ) {
	this.context = context;
	this.account = account;
    }

    @Override
    public String toString() {
	return AccountDisplayer.toString( this.context, this.account );
    }

    @Override
    public long getId() {
	return this.account.getId();
    }

    @Override
    public boolean isDeleted() {
	return this.account.isDeleted();
    }

    @Override
    public void delete() {
	this.account.delete();
    }

    @Override
    public void addObserver( Observer observer ) {
	this.account.addObserver( observer );
    }

    @Override
    public void init() {
	this.account.init();
    }

    @Override
    public Budget getBudget() {
	return this.account.getBudget();
    }

    @Override
    public String getName() {
	return this.account.getName();
    }

    @Override
    public void setName( String name ) {
	this.account.setName( name );
    }

    @Override
    public double getTargetAmount() {
	return this.account.getTargetAmount();
    }

    @Override
    public int countObservers() {
	return this.account.countObservers();
    }

    @Override
    public void setTargetAmount( double targetAmount ) {
	this.account.setTargetAmount( targetAmount );
    }

    @Override
    public Collection<Transaction> getTransactions() {
	return this.account.getTransactions();
    }

    @Override
    public void deleteObserver( Observer observer ) {
	this.account.deleteObserver( observer );
    }

    @Override
    public Collection<? extends Transaction> getIncomingTransactions() {
	return this.account.getIncomingTransactions();
    }

    @Override
    public void deleteObservers() {
	this.account.deleteObservers();
    }

    @Override
    public Collection<? extends Transaction> getOutgoingTransactions() {
	return this.account.getOutgoingTransactions();
    }

    @Override
    public boolean hasChanged() {
	return this.account.hasChanged();
    }

    @Override
    public boolean removeOutgoingTransaction( Transaction transaction ) {
	return this.account.removeOutgoingTransaction( transaction );
    }

    @Override
    public void notifyObservers() {
	this.account.notifyObservers();
    }

    @Override
    public void notifyObservers( Object data ) {
	this.account.notifyObservers( data );
    }

    @Override
    public void fill( double amount ) {
	this.account.fill( amount );
    }

    @Override
    public void expense( double amount ) {
	this.account.expense( amount );
    }

    @Override
    public void transfert( double amount, Account destination ) {
	this.account.transfert( amount, destination );
    }

    @Override
    public double getCurrentAmount() {
	return this.account.getCurrentAmount();
    }

    @Override
    public void destroy() {
	this.account.destroy();
    }

    @Override
    public boolean isOffBudget() {
	return this.account.isOffBudget();
    }

    @Override
    public void update( Observable observable, Object data ) {
	this.account.update( observable, data );
    }

    @Override
    public boolean equals( Object o ) {
	return this.account.equals( o );
    }

    @Override
    public int hashCode() {
	return this.account.hashCode();
    }
}
