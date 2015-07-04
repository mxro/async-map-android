package de.mxro.async.map.android.internal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import de.mxro.async.map.AsyncMap;
import de.mxro.async.map.android.AsyncMapAndorid;
import de.mxro.async.map.android.SQLiteConfiguration;
import de.mxro.async.map.operations.ClearCacheOperation;
import de.mxro.async.map.operations.MapOperation;
import de.mxro.serialization.Serializer;
import de.mxro.serialization.jre.SerializationJre;
import de.mxro.serialization.jre.StreamDestination;
import de.mxro.serialization.jre.StreamSource;
import delight.async.callbacks.SimpleCallback;
import delight.async.callbacks.ValueCallback;

public class AndroidAsyncMap<V> implements AsyncMap<String, V> {

    private final boolean ENABLE_LOG = false;

    private final SQLiteConfiguration conf;

    private final Serializer<StreamSource, StreamDestination> serializer;

    private final SQLiteDatabase db;

    @Override
    public void put(final String key, final V value, final SimpleCallback callback) {
        putSync(key, value);

        this.commit(callback);

    }

    @Override
    public void get(final String key, final ValueCallback<V> callback) {
        callback.onSuccess(this.getSync(key));
    }

    @Override
    public void remove(final String key, final SimpleCallback callback) {
        removeSync(key);

        this.commit(callback);

    }

    @SuppressWarnings("unchecked")
    @Override
    public V getSync(final String key) {

        final byte[] data = executeQueryImmidiately(createSelectStatement(), key);

        if (ENABLE_LOG) {
            if (data == null) {
                System.out.println(this + ": getSync " + key + " retrieved null");
            }
        }

        if (data == null) {
            return null;
        }

        final Object object = serializer.deserialize(SerializationJre
                .createStreamSource(new ByteArrayInputStream(data)));

        if (ENABLE_LOG) {
            System.out.println(this + ": getSync " + key + " retrieved " + object);
        }

        return (V) object;
    }

    private String createSelectStatement() {
        final String sql = "SELECT " + conf.getKeyColumnName() + ", " + conf.getValueColumnName() + " FROM "
                + conf.getTableName() + " WHERE " + conf.getKeyColumnName() + " = ?";

        return sql;
    }

    @Override
    public void putSync(final String key, final V value) {
        assert key.length() <= AsyncMapAndorid.KEY_LENGTH;

        if (ENABLE_LOG) {
            System.out.println(this + ": putSync " + key + " Value " + value);
        }

        final SQLiteStatement statement = createInsertStatement(key, value);

        executeInsertStatementImmidiately(statement);

    }

    private byte[] executeQueryImmidiately(final String sql, final String key) {

        final Cursor query = db.query(conf.getTableName(),
                new String[] { conf.getKeyColumnName(), conf.getValueColumnName() }, conf.getKeyColumnName() + "=?",
                new String[] { key }, null, null, null);
        if (query.getCount() == 0) {
            return null;
        }

        query.moveToFirst();
        final byte[] data = query.getBlob(1);
        query.close();
        return data;
    }

    private void executeUpdateOrDeleteStatementImmidiately(final SQLiteStatement statement) {
        db.beginTransaction();

        final int rowsAffected = statement.executeUpdateDelete();

        if (rowsAffected != 1) {
            throw new RuntimeException("No rows could be found for query " + statement.toString());
        }

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    private void executeInsertStatementImmidiately(final SQLiteStatement statement) {
        db.beginTransaction();

        statement.executeInsert();

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    private SQLiteStatement createInsertStatement(final String key, final V value) {
        final String sql = "INSERT OR REPLACE INTO " + conf.getTableName() + " (" + conf.getKeyColumnName() + ","
                + conf.getValueColumnName() + ") VALUES (?, ?)";
        final SQLiteStatement statement = db.compileStatement(sql);

        statement.bindString(1, key);

        final ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
        serializer.serialize(value, SerializationJre.createStreamDestination(os));

        statement.bindBlob(2, os.toByteArray());
        return statement;
    }

    @Override
    public void removeSync(final String key) {

        executeUpdateOrDeleteStatementImmidiately(createRemoveStatement(key));

    }

    private SQLiteStatement createRemoveStatement(final String key) {
        final String sql = "DELETE FROM " + conf.getTableName() + " WHERE " + conf.getKeyColumnName() + " = ?";
        final SQLiteStatement statement = db.compileStatement(sql);

        statement.bindString(1, key);

        return statement;
    }

    @Override
    public void start(final SimpleCallback callback) {

        callback.onSuccess();
    }

    @Override
    public void stop(final SimpleCallback callback) {

        db.close();

        callback.onSuccess();
    }

    @Override
    public void commit(final SimpleCallback callback) {
        callback.onSuccess();
    }

    @Override
    public void performOperation(final MapOperation operation) {
        if (operation instanceof ClearCacheOperation) {
            SQLiteDatabase.releaseMemory();
        }
    }

    public AndroidAsyncMap(final SQLiteConfiguration conf,
            final Serializer<StreamSource, StreamDestination> serializer, final SQLiteDatabase db) {
        super();
        this.conf = conf;
        this.serializer = serializer;
        this.db = db;
    }

}
