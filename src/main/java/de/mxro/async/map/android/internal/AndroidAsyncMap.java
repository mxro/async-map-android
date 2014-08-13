package de.mxro.async.map.android.internal;

import android.database.sqlite.SQLiteDatabase;
import de.mxro.async.callbacks.SimpleCallback;
import de.mxro.async.callbacks.ValueCallback;
import de.mxro.async.map.AsyncMap;
import de.mxro.async.map.operations.MapOperation;

public class AndroidAsyncMap<V> implements AsyncMap<String, V> {

	private final SQLiteDatabase db;
	
	@Override
	public void put(String key, V value, SimpleCallback callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void get(String key, ValueCallback<V> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(String key, SimpleCallback callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public V getSync(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void putSync(String key, V value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeSync(String key) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start(SimpleCallback callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop(SimpleCallback callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void commit(SimpleCallback callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void performOperation(MapOperation operation) {
		// TODO Auto-generated method stub
		
	}

}
