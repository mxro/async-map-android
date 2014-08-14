package de.mxro.async.map.android.internal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

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

	private final SQLiteDatabase injectedDb;
	
	private SQLiteDatabase db;

	

	@Override
	public void put(String key, V value, SimpleCallback callback) {
		putSync(key, value);

		this.commit(callback);

	}

	@Override
	public void get(String key, ValueCallback<V> callback) {
		callback.onSuccess(this.getSync(key));
	}

	@Override
	public void remove(String key, SimpleCallback callback) {
		removeSync(key);

		this.commit(callback);

	}

	@SuppressWarnings("unchecked")
	@Override
	public V getSync(String key) {

		byte[] data = executeQueryImmidiately(createSelectStatement(), key);

		Object object = serializer.deserialize(SerializationJre
				.createStreamSource(new ByteArrayInputStream(data)));

		return (V) object;
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
		Cursor query = db.rawQuery(sql, new String[] { key });
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
		String sql = "INSERT INTO " + conf.getTableName() + " VALUES (?, ?)";
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
		if (injectedDb == null) {
			db = SQLiteDatabase.openOrCreateDatabase(conf.getDatabasePath(),
					null);
		} else {
			db = injectedDb;
		}
		callback.onSuccess();
	}

	@Override
	public void stop(SimpleCallback callback) {
		if (injectedDb == null) {
			db.close();
		}

		callback.onSuccess();
	}

	@Override
	public void commit(SimpleCallback callback) {
		callback.onSuccess();
	}

	@Override
	public void performOperation(MapOperation operation) {
		// none supported
	}

	public AndroidAsyncMap(SQLiteConfiguration conf,
			Serializer<StreamSource, StreamDestination> serializer,
			SQLiteDatabase injectedDb) {
		super();
		this.conf = conf;
		this.serializer = serializer;
		this.injectedDb = injectedDb;
	}

	

}
