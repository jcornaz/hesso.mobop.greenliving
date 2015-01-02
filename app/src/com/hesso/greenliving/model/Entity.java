package com.hesso.greenliving.model;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Collection;

import android.support.v4.util.LongSparseArray;

import com.j256.ormlite.field.DatabaseField;

public abstract class Entity extends GreenLivingObservable implements Serializable {
    private static final long serialVersionUID = -110511284436795169L;

    @DatabaseField (generatedId = true )
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

    public abstract void init();

    protected abstract void destroy();
}
