package de.mxro.async.map.android;

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

}
