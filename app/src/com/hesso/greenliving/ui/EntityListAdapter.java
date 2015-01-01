package com.hesso.greenliving.ui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.hesso.greenliving.model.Entity;

public abstract class EntityListAdapter<EntityType extends Entity, ViewType extends IEntityView<EntityType>> extends ArrayAdapter<EntityType> {

    protected Set<EntityType> entities = new HashSet<EntityType>();

    public EntityListAdapter( Context context, int resource ) {
	super( context, resource );
    }

    public void setList( Collection<EntityType> entities ) {
	Set<EntityType> toRemove = new HashSet<EntityType>( this.entities );

	for( EntityType entity : entities ) {
	    if( !toRemove.remove( entity ) ) {
		this.add( entity );
	    }
	}

	this.remove( toRemove );
    }

    private void remove( Collection<EntityType> transactions ) {
	for( EntityType transaction : transactions ) {
	    this.remove( transaction );
	}
    }

    @Override
    public void remove( EntityType entity ) {
	super.remove( entity );
	this.entities.remove( entity );
    }

    @Override
    public void add( EntityType entity ) {
	super.add( entity );
	this.entities.add( entity );
    }

    @Override
    public long getItemId( int position ) {
	return this.getItem( position ).getId();
    }

    @Override
    public boolean hasStableIds() {
	return true;
    }

    @Override
    @SuppressWarnings ("unchecked" )
    public View getView( int position, View convertView, ViewGroup parent ) {
	ViewType res;

	if( convertView != null ) {
	    res = (ViewType) convertView;
	} else {
	    res = this.inflateItem( parent );
	}

	res.setModel( this.getItem( position ) );

	return (View) res;
    }

    protected abstract ViewType inflateItem( ViewGroup parent );
}
