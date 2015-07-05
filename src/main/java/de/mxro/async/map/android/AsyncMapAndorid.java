package de.mxro.async.map.android;

import delight.functional.Function;

import java.io.File;

import android.database.sqlite.SQLiteDatabase;
import de.mxro.async.map.AsyncMap;
import de.mxro.async.map.android.internal.AndroidAsyncMap;
import de.mxro.serialization.Serializer;
import de.mxro.serialization.jre.StreamDestination;
import de.mxro.serialization.jre.StreamSource;

public class AsyncMapAndorid {

    /**
     * The length of keys used by the engine.
     */
    public static int KEY_LENGTH = 512;

    private static Function<File, SQLiteDatabase> sqlLiteFactory = new Function<File, SQLiteDatabase>() {

        @Override
        public SQLiteDatabase apply(final File dbFile) {
            return SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        }
    };

    public static <V> AsyncMap<String, V> createMap(final SQLiteConfiguration conf,
            final Serializer<StreamSource, StreamDestination> serializer, final SQLiteDatabase injectedDb) {
        return new AndroidAsyncMap<V>(conf, serializer, injectedDb);
    }

    public static <V> AsyncMap<String, V> createMap(final SQLiteConfiguration conf,
            final Serializer<StreamSource, StreamDestination> serializer) {
        return createMap(conf, serializer, null);
    }

    public static SQLiteConfiguration createDefaultConfiguration() {
        return new SQLiteConfiguration() {

            @Override
            public String getValueColumnName() {
                return "value";
            }

            @Override
            public String getTableName() {
                return "data";
            }

            @Override
            public String getKeyColumnName() {
                return "key";
            }

        };
    }

    public static SQLiteDatabase assertDatabase(final File dbFile, final SQLiteConfiguration conf) {
        final SQLiteDatabase db = sqlLiteFactory.apply(dbFile);
        return db;
    }

    public static void assertTable(final SQLiteDatabase db, final SQLiteConfiguration conf) {

        db.execSQL("CREATE TABLE IF NOT EXISTS " + conf.getTableName() + "(" + conf.getKeyColumnName() + " VARCHAR("
                + String.valueOf(KEY_LENGTH) + ") PRIMARY KEY, " + conf.getValueColumnName() + " BLOB)");

    }

    public static final void setSQLiteDatabaseFactory(final Function<File, SQLiteDatabase> factory) {
        if (factory == null) {
            throw new IllegalArgumentException("Factory cannot be <null>.");
        }

        sqlLiteFactory = factory;
    }

}
