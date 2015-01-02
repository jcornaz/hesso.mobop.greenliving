package com.hesso.greenliving.dao;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.concurrent.Callable;

import android.util.Log;

import com.hesso.greenliving.exception.UnexpectedException;
import com.hesso.greenliving.model.Entity;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.CloseableWrappedIterable;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.ObjectCache;
import com.j256.ormlite.dao.RawRowMapper;
import com.j256.ormlite.dao.RawRowObjectMapper;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.GenericRowMapper;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.PreparedUpdate;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.support.DatabaseResults;
import com.j256.ormlite.table.ObjectFactory;

public abstract class EntitiesDao<EntityType extends Entity> implements Observer, Dao<EntityType, Long> {

    private Dao<EntityType, Long> dao;

    public EntitiesDao( Dao<EntityType, Long> dao ) {
	this.dao = dao;
    }

    public void persist( EntityType entity ) {
	try {
	    this.createOrUpdate( entity );
	    entity.addObserver( this );
	    this.updateChildren( entity );
	} catch( SQLException e ) {
	    throw new UnexpectedException( e );
	}

	Log.i( this.getClass().getSimpleName(), "entity " + entity.getId() + " persisted" );
    }

    @Override
    public int delete( EntityType entity ) {
	int res = 0;
	entity.deleteObserver( this );

	try {
	    this.deleteChildren( entity );
	    res = this.dao.delete( entity );
	} catch( SQLException e ) {
	    throw new UnexpectedException( e );
	}

	Log.i( this.getClass().getSimpleName(), "entity " + entity.getId() + " deleted" );

	return res;
    }

    @Override
    public int delete( Collection<EntityType> entities ) {
	int res = 0;

	for( EntityType entity : entities ) {
	    res += this.delete( entity );
	}

	return res;
    }

    @Override
    @SuppressWarnings ("unchecked" )
    public void update( Observable observable, Object data ) {
	try {
	    EntityType entity = (EntityType) observable;
	    if( entity.isDeleted() ) {
		this.delete( entity );
	    } else {
		this.update( entity );
		this.updateChildren( entity );
	    }
	} catch( SQLException e ) {
	    throw new UnexpectedException( e );
	}
    }

    @Override
    public int refresh( EntityType entity ) {
	int res = 0;

	try {
	    res = this.dao.refresh( entity );
	} catch( SQLException e ) {
	    throw new UnexpectedException( e );
	}

	this.hasBeenRefreshed( entity );

	entity.init();

	this.refreshChildren( entity );

	return res;
    }

    protected <T extends Entity> void refreshChildren( EntityType parentEntity, EntitiesDao<T> childrenDao, Map<EntityType, Set<T>> persistedMap ) {
	Set<T> children = persistedMap.get( parentEntity );
	if( children != null ) {
	    for( T child : children ) {
		childrenDao.refresh( child );
	    }
	}
    }

    protected <T extends Entity> void updateChildren( EntityType parentEntity, EntitiesDao<T> childrenDao, Map<EntityType, Set<T>> persistedMap, Collection<T> existingList ) {
	Set<T> children = persistedMap.get( parentEntity );

	if( children == null ) {
	    children = new HashSet<T>();
	    persistedMap.put( parentEntity, children );
	}

	for( T child : existingList ) {
	    if( children.add( child ) ) {
		childrenDao.persist( child );
	    }
	}
    }

    protected <T extends Entity> void deleteChildren( EntityType parentEntity, EntitiesDao<T> childrenDao, Map<EntityType, Set<T>> persistedMap ) {
	Set<T> children = persistedMap.remove( parentEntity );
	if( children != null ) {
	    childrenDao.delete( children );
	}
    }

    protected abstract void hasBeenRefreshed( EntityType entity );

    protected abstract void refreshChildren( EntityType entity );

    protected abstract void updateChildren( EntityType entity );

    protected abstract void deleteChildren( EntityType entity );

    @Override
    public CloseableIterator<EntityType> closeableIterator() {
	return this.dao.closeableIterator();
    }

    @Override
    public EntityType queryForId( Long id ) throws SQLException {
	return this.dao.queryForId( id );
    }

    @Override
    public EntityType queryForFirst( PreparedQuery<EntityType> preparedQuery ) throws SQLException {
	return this.dao.queryForFirst( preparedQuery );
    }

    @Override
    public List<EntityType> queryForAll() throws SQLException {
	return this.dao.queryForAll();
    }

    @Override
    public List<EntityType> queryForEq( String fieldName, Object value ) throws SQLException {
	return this.dao.queryForEq( fieldName, value );
    }

    @Override
    public List<EntityType> queryForMatching( EntityType matchObj ) throws SQLException {
	return this.dao.queryForMatching( matchObj );
    }

    @Override
    public List<EntityType> queryForMatchingArgs( EntityType matchObj ) throws SQLException {
	return this.dao.queryForMatchingArgs( matchObj );
    }

    @Override
    public List<EntityType> queryForFieldValues( Map<String, Object> fieldValues ) throws SQLException {
	return this.dao.queryForFieldValues( fieldValues );
    }

    @Override
    public List<EntityType> queryForFieldValuesArgs( Map<String, Object> fieldValues ) throws SQLException {
	return this.dao.queryForFieldValuesArgs( fieldValues );
    }

    @Override
    public EntityType queryForSameId( EntityType data ) throws SQLException {
	return this.dao.queryForSameId( data );
    }

    @Override
    public QueryBuilder<EntityType, Long> queryBuilder() {
	return this.dao.queryBuilder();
    }

    @Override
    public UpdateBuilder<EntityType, Long> updateBuilder() {
	return this.dao.updateBuilder();
    }

    @Override
    public DeleteBuilder<EntityType, Long> deleteBuilder() {
	return this.dao.deleteBuilder();
    }

    @Override
    public List<EntityType> query( PreparedQuery<EntityType> preparedQuery ) throws SQLException {
	return this.dao.query( preparedQuery );
    }

    @Override
    public int create( EntityType data ) throws SQLException {
	return this.dao.create( data );
    }

    @Override
    public EntityType createIfNotExists( EntityType data ) throws SQLException {
	return this.dao.createIfNotExists( data );
    }

    @Override
    public com.j256.ormlite.dao.Dao.CreateOrUpdateStatus createOrUpdate( EntityType data ) throws SQLException {
	return this.dao.createOrUpdate( data );
    }

    @Override
    public int update( EntityType data ) throws SQLException {
	return this.dao.update( data );
    }

    @Override
    public int updateId( EntityType data, Long newId ) throws SQLException {
	return this.dao.updateId( data, newId );
    }

    @Override
    public int update( PreparedUpdate<EntityType> preparedUpdate ) throws SQLException {
	return this.dao.update( preparedUpdate );
    }

    @Override
    public int deleteById( Long id ) throws SQLException {
	return this.dao.deleteById( id );
    }

    @Override
    public int deleteIds( Collection<Long> ids ) throws SQLException {
	return this.dao.deleteIds( ids );
    }

    @Override
    public int delete( PreparedDelete<EntityType> preparedDelete ) throws SQLException {
	return this.dao.delete( preparedDelete );
    }

    @Override
    public CloseableIterator<EntityType> iterator() {
	return this.dao.iterator();
    }

    @Override
    public CloseableIterator<EntityType> iterator( int resultFlags ) {
	return this.dao.iterator( resultFlags );
    }

    @Override
    public CloseableIterator<EntityType> iterator( PreparedQuery<EntityType> preparedQuery ) throws SQLException {
	return this.dao.iterator( preparedQuery );
    }

    @Override
    public CloseableIterator<EntityType> iterator( PreparedQuery<EntityType> preparedQuery, int resultFlags ) throws SQLException {
	return this.dao.iterator( preparedQuery, resultFlags );
    }

    @Override
    public CloseableWrappedIterable<EntityType> getWrappedIterable() {
	return this.dao.getWrappedIterable();
    }

    @Override
    public CloseableWrappedIterable<EntityType> getWrappedIterable( PreparedQuery<EntityType> preparedQuery ) {
	return this.dao.getWrappedIterable( preparedQuery );
    }

    @Override
    public void closeLastIterator() throws SQLException {
	this.dao.closeLastIterator();
    }

    @Override
    public GenericRawResults<String[]> queryRaw( String query, String... arguments ) throws SQLException {
	return this.dao.queryRaw( query, arguments );
    }

    @Override
    public <UO> GenericRawResults<UO> queryRaw( String query, RawRowMapper<UO> mapper, String... arguments ) throws SQLException {
	return this.dao.queryRaw( query, mapper, arguments );
    }

    @Override
    public <UO> GenericRawResults<UO> queryRaw( String query, DataType[] columnTypes, RawRowObjectMapper<UO> mapper, String... arguments ) throws SQLException {
	return this.dao.queryRaw( query, columnTypes, mapper, arguments );
    }

    @Override
    public GenericRawResults<Object[]> queryRaw( String query, DataType[] columnTypes, String... arguments ) throws SQLException {
	return this.dao.queryRaw( query, columnTypes, arguments );
    }

    @Override
    public long queryRawValue( String query, String... arguments ) throws SQLException {
	return this.dao.queryRawValue( query, arguments );
    }

    @Override
    public int executeRaw( String statement, String... arguments ) throws SQLException {
	return this.dao.executeRaw( statement, arguments );
    }

    @Override
    public int executeRawNoArgs( String statement ) throws SQLException {
	return this.dao.executeRawNoArgs( statement );
    }

    @Override
    public int updateRaw( String statement, String... arguments ) throws SQLException {
	return this.dao.updateRaw( statement, arguments );
    }

    @Override
    public <CT> CT callBatchTasks( Callable<CT> callable ) throws Exception {
	return this.dao.callBatchTasks( callable );
    }

    @Override
    public String objectToString( EntityType data ) {
	return this.dao.objectToString( data );
    }

    @Override
    public boolean objectsEqual( EntityType data1, EntityType data2 ) throws SQLException {
	return this.dao.objectsEqual( data1, data2 );
    }

    @Override
    public Long extractId( EntityType data ) throws SQLException {
	return this.dao.extractId( data );
    }

    @Override
    public Class<EntityType> getDataClass() {
	return this.dao.getDataClass();
    }

    @Override
    public FieldType findForeignFieldType( Class<?> clazz ) {
	return this.dao.findForeignFieldType( clazz );
    }

    @Override
    public boolean isUpdatable() {
	return this.dao.isUpdatable();
    }

    @Override
    public boolean isTableExists() throws SQLException {
	return this.dao.isTableExists();
    }

    @Override
    public long countOf() throws SQLException {
	return this.dao.countOf();
    }

    @Override
    public long countOf( PreparedQuery<EntityType> preparedQuery ) throws SQLException {
	return this.dao.countOf( preparedQuery );
    }

    @Override
    public void assignEmptyForeignCollection( EntityType parent, String fieldName ) throws SQLException {
	this.dao.assignEmptyForeignCollection( parent, fieldName );
    }

    @Override
    public <FT> ForeignCollection<FT> getEmptyForeignCollection( String fieldName ) throws SQLException {
	return this.dao.getEmptyForeignCollection( fieldName );
    }

    @Override
    public void setObjectCache( boolean enabled ) throws SQLException {
	this.dao.setObjectCache( enabled );
    }

    @Override
    public void setObjectCache( ObjectCache objectCache ) throws SQLException {
	this.dao.setObjectCache( objectCache );
    }

    @Override
    public ObjectCache getObjectCache() {
	return this.dao.getObjectCache();
    }

    @Override
    public void clearObjectCache() {
	this.dao.clearObjectCache();
    }

    @Override
    public EntityType mapSelectStarRow( DatabaseResults results ) throws SQLException {
	return this.dao.mapSelectStarRow( results );
    }

    @Override
    public GenericRowMapper<EntityType> getSelectStarRowMapper() throws SQLException {
	return this.dao.getSelectStarRowMapper();
    }

    @Override
    public RawRowMapper<EntityType> getRawRowMapper() {
	return this.dao.getRawRowMapper();
    }

    @Override
    public boolean idExists( Long id ) throws SQLException {
	return this.dao.idExists( id );
    }

    @Override
    public DatabaseConnection startThreadConnection() throws SQLException {
	return this.dao.startThreadConnection();
    }

    @Override
    public void endThreadConnection( DatabaseConnection connection ) throws SQLException {
	this.dao.endThreadConnection( connection );
    }

    @Override
    @Deprecated
    public void setAutoCommit( boolean autoCommit ) throws SQLException {
	this.dao.setAutoCommit( autoCommit );
    }

    @Override
    public void setAutoCommit( DatabaseConnection connection, boolean autoCommit ) throws SQLException {
	this.dao.setAutoCommit( connection, autoCommit );
    }

    @Override
    @Deprecated
    public boolean isAutoCommit() throws SQLException {
	return this.dao.isAutoCommit();
    }

    @Override
    public boolean isAutoCommit( DatabaseConnection connection ) throws SQLException {
	return this.dao.isAutoCommit( connection );
    }

    @Override
    public void commit( DatabaseConnection connection ) throws SQLException {
	this.dao.commit( connection );
    }

    @Override
    public void rollBack( DatabaseConnection connection ) throws SQLException {
	this.dao.rollBack( connection );
    }

    @Override
    public ConnectionSource getConnectionSource() {
	return this.dao.getConnectionSource();
    }

    @Override
    public void setObjectFactory( ObjectFactory<EntityType> objectFactory ) {
	this.dao.setObjectFactory( objectFactory );
    }
}