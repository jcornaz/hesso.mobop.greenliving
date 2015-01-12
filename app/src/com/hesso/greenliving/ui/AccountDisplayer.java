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
	return account.getId();
    }

    @Override
    public boolean isDeleted() {
	return account.isDeleted();
    }

    @Override
    public void delete() {
	account.delete();
    }

    @Override
    public void addObserver( Observer observer ) {
	account.addObserver( observer );
    }

    @Override
    public void init() {
	account.init();
    }

    @Override
    public Budget getBudget() {
	return account.getBudget();
    }
    
    @Override
    public String getName() {
	return account.getName();
    }

    @Override
    public void setName( String name ) {
	account.setName( name );
    }

    @Override
    public double getTargetAmount() {
	return account.getTargetAmount();
    }

    @Override
    public int countObservers() {
	return account.countObservers();
    }

    @Override
    public void setTargetAmount( double targetAmount ) {
	account.setTargetAmount( targetAmount );
    }

    @Override
    public void deleteObserver( Observer observer ) {
	account.deleteObserver( observer );
    }

    @Override
    public Collection<Transaction> getTransactions() {
	return account.getTransactions();
    }

    @Override
    public Collection<? extends Transaction> getIncomingTransactions() {
	return account.getIncomingTransactions();
    }

    @Override
    public void deleteObservers() {
	account.deleteObservers();
    }

    @Override
    public Collection<? extends Transaction> getOutgoingTransactions() {
	return account.getOutgoingTransactions();
    }

    @Override
    public boolean hasChanged() {
	return account.hasChanged();
    }

    @Override
    public void notifyObservers() {
	account.notifyObservers();
    }

    @Override
    public void notifyObservers( Object data ) {
	account.notifyObservers( data );
    }

    @Override
    public void fill( double amount ) {
	account.fill( amount );
    }

    @Override
    public void expense( double amount ) {
	account.expense( amount );
    }

    @Override
    public void transfert( double amount, Account destination ) {
	account.transfert( amount, destination );
    }

    @Override
    public double getCurrentAmount() {
	return account.getCurrentAmount();
    }

    @Override
    public void destroy() {
	account.destroy();
    }

    @Override
    public boolean isOffBudget() {
	return account.isOffBudget();
    }

    @Override
    public void update( Observable observable, Object data ) {
	account.update( observable, data );
    }

    @Override
    public boolean isInBudget() {
	return account.isInBudget();
    }

    @Override
    public boolean equals( Object o ) {
	return account.equals( o );
    }

    @Override
    public double getTheoricAmount() {
	return account.getTheoricAmount();
    }

    @Override
    public int hashCode() {
	return account.hashCode();
    }

    @Override
    public boolean removeOutgoingTransaction( Transaction transaction ) {
	return account.removeOutgoingTransaction( transaction );
    }

    @Override
    public boolean addOutgoingTransaction( Transaction transaction ) {
	return account.addOutgoingTransaction( transaction );
    }

    @Override
    public boolean removeIncomingTransaction( Transaction transaction ) {
	return account.removeIncomingTransaction( transaction );
    }

    @Override
    public boolean addIncomingTransaction( Transaction transaction ) {
	return account.addIncomingTransaction( transaction );
    }

    @Override
    public void setOffBudget( boolean value ) {
	account.setOffBudget( value );
    }
}
