package com.hesso.greenliving.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class GreenLivingObservable extends Observable {

    private List<Observer> observers = new LinkedList<Observer>();
    private boolean hasChanged = false;

    @Override
    public void addObserver( Observer observer ) {
	this.observers.add( observer );
    }

    @Override
    public int countObservers() {
	return this.observers.size();
    }

    @Override
    public synchronized void deleteObserver( Observer observer ) {
	this.observers.remove( observer );
    }

    @Override
    public synchronized void deleteObservers() {
	this.observers.clear();
    }

    @Override
    public boolean hasChanged() {
	return this.hasChanged;
    }

    @Override
    public void notifyObservers() {
	if( this.hasChanged() ) {
	    for( Observer observer : this.observers ) {
		observer.update( this, null );
	    }
	}
    }

    @Override
    protected void setChanged() {
	this.hasChanged = true;
    }
}
