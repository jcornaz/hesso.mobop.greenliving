package com.hesso.greenliving.dao;

import com.hesso.greenliving.model.ScheduledTransaction;
import com.j256.ormlite.dao.Dao;

public class SchedulesDao extends EntitiesDao<ScheduledTransaction> {

    public SchedulesDao( Dao<ScheduledTransaction, Long> dao ) {
	super( dao );
    }

    @Override
    protected void persistChildren( ScheduledTransaction entity ) {
	// Les ScheduledTransaction n'ont pas d'enfants
    }

    @Override
    protected void deleteChildren( ScheduledTransaction entity ) {
	// Les ScheduledTransaction n'ont pas d'enfants
    }

    @Override
    protected void refreshChildren( ScheduledTransaction entity ) {
	// Les ScheduledTransaction n'ont pas d'enfants
    }

    @Override
    protected void hasBeenRefreshed( ScheduledTransaction entity ) {
	// Les ScheduledTransaction n'ont pas d'enfants
    }
}
