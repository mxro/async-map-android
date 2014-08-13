package de.mxro.async.map.android.internal;

import java.io.ByteArrayOutputStream;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import de.mxro.async.callbacks.SimpleCallback;
import de.mxro.async.callbacks.ValueCallback;
import de.mxro.async.map.AsyncMap;
import de.mxro.async.map.android.SQLiteConfiguration;
import de.mxro.async.map.operations.MapOperation;
import de.mxro.serialization.Serializer;
import de.mxro.serialization.jre.SerializationJre;
import de.mxro.serialization.jre.StreamDestination;
import de.mxro.serialization.jre.StreamSource;

public class AndroidAsyncMap<V> implements AsyncMap<String, V> {

	private final SQLiteConfiguration conf;

	private final Serializer<StreamSource, StreamDestination> serializer;

	private SQLiteDatabase db;

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

		
		executeQueryImmidiately(createSelectStatement(), key);
		
		statement.

		return null;
	}

	

	private String createSelectStatement() {
		final String sql = "SELECT " + conf.getKeyColumnName() + ", "
				+ conf.getValueColumnName() + " FROM " + conf.getTableName()
				+ " WHERE " + conf.getKeyColumnName() + " = ?";
		
		return sql;
	}

	@Override
	public void putSync(String key, V value) {

		SQLiteStatement statement = createInsertStatement(key, value);

		executeStatementImmidiately(statement);

	}

	private byte[] executeQueryImmidiately(String sql, String key) {
		Cursor query = db.rawQuery(sql, new String[] {key});
		byte[] data = query.getBlob(1);
		query.close();
		return data;
	}
	
	private void executeStatementImmidiately(SQLiteStatement statement) {
		db.beginTransaction();

		statement.execute();

		db.setTransactionSuccessful();
		db.endTransaction();
	}

	private SQLiteStatement createInsertStatement(String key, V value) {
		String sql = "INSERT INTO " + conf.getTableName() + " VALUES (?,?);";
		SQLiteStatement statement = db.compileStatement(sql);

		statement.bindString(0, key);

		ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
		serializer.serialize(value,
				SerializationJre.createStreamDestination(os));

		statement.bindBlob(1, os.toByteArray());
		return statement;
	}

	@Override
	public void removeSync(String key) {

		executeStatementImmidiately(createRemoveStatement(key));

	}

	private SQLiteStatement createRemoveStatement(String key) {
		String sql = "DELETE FROM " + conf.getTableName() + " WHERE "
				+ conf.getKeyColumnName() + " = ?";
		SQLiteStatement statement = db.compileStatement(sql);

		statement.bindString(0, key);

		return statement;
	}

	@Override
	public void start(SimpleCallback callback) {
		db = SQLiteDatabase.openOrCreateDatabase(conf.getDatabasePath(), null);

		callback.onSuccess();
	}

	@Override
	public void stop(SimpleCallback callback) {
		db.close();

		callback.onSuccess();
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
