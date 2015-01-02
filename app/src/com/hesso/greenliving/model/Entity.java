package com.hesso.greenliving.model;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.Observable;

import android.support.v4.util.LongSparseArray;

import com.j256.ormlite.field.DatabaseField;

public abstract class Entity extends Observable implements Serializable {
    private static final long serialVersionUID = -110511284436795169L;

    @DatabaseField (id = true )
    // TODO Améliorer le système d'identification
    private long id = new SecureRandom().nextLong();

    private boolean isDeleted = false;

    public Entity() {
    }

    public long getId() {
	return id;
    }

    protected void setId( long id ) {
	this.id = id;
    }

    public boolean isDeleted() {
	return this.isDeleted;
    }

    public void delete() {
	this.destroy();
	this.isDeleted = true;
	this.setChanged();
	this.notifyObservers();
    }

    protected <T extends Entity> void map( Collection<T> entities, LongSparseArray<T> entitiesMap ) {
	entitiesMap.clear();
	for( T entity : entities ) {
	    entitiesMap.put( entity.getId(), entity );
	}
    }

    protected abstract void destroy();

    public abstract void init();

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + (int) (id ^ (id >>> 32));
	return result;
    }

    @Override
    public boolean equals( Object obj ) {
	if( this == obj )
	    return true;
	if( obj == null )
	    return false;
	if( getClass() != obj.getClass() )
	    return false;
	Entity other = (Entity) obj;
	if( id != other.id )
	    return false;
	return true;
    }
}
