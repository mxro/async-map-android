package de.mxro.async.map.android;

import java.sql.CallableStatement;
import java.sql.SQLException;

import android.database.sqlite.SQLiteDatabase;
import de.mxro.async.map.AsyncMap;
import de.mxro.async.map.android.internal.AndroidAsyncMap;
import de.mxro.serialization.Serializer;
import de.mxro.serialization.jre.StreamDestination;
import de.mxro.serialization.jre.StreamSource;

public class AsyncMapAndorid {

	public static <V> AsyncMap<String, V> createMap(SQLiteConfiguration conf,
			Serializer<StreamSource, StreamDestination> serializer,
			SQLiteDatabase injectedDb) {
		return new AndroidAsyncMap<V>(conf, serializer, injectedDb);
	}

	public static <V> AsyncMap<String, V> createMap(SQLiteConfiguration conf,
			Serializer<StreamSource, StreamDestination> serializer) {
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

			@Override
			public String getDatabasePath() {
				return "db";
			}
		};
	}

	public static void assertTable(SQLiteDatabase db, SQLiteConfiguration conf) {

		db.execSQL("CREATE TABLE IF NOT EXISTS " + conf.getTableName()
				+ "("+conf.getKeyColumnName()+" VARCHAR(512) PRIMARY KEY, "+conf.getValueColumnName()+" BLOB)");

	}

}
