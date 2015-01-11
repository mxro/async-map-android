package de.mxro.async.map.android.tests;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowSQLiteDatabase;

import android.database.sqlite.SQLiteDatabase;
import de.mxro.async.AsyncCommon;
import de.mxro.async.Operation;
import de.mxro.async.callbacks.ValueCallback;
import de.mxro.async.jre.Async;
import de.mxro.async.map.AsyncMap;
import de.mxro.async.map.android.AsyncMapAndorid;
import de.mxro.async.map.android.SQLiteConfiguration;
import de.mxro.fn.Success;
import de.mxro.serialization.jre.SerializationJre;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class TestThatValuesCanBeReadAndWritten {

	@Test
	public void test() throws Exception {

		final SQLiteConfiguration conf = AsyncMapAndorid.createDefaultConfiguration();
		final SQLiteDatabase db = ShadowSQLiteDatabase.create(null);
		
		AsyncMapAndorid.assertTable(db, conf);
		
		final AsyncMap<String, Object> map = AsyncMapAndorid.createMap(conf,
				SerializationJre.newJavaSerializer(),
				db);

		Async.waitFor(new Operation<Success>() {

			@Override
			public void apply(ValueCallback<Success> callback) {
				map.start(AsyncCommon.wrap(callback));
			}
		});

		map.putSync("one", 1);
		
		map.putSync("two", 2);
		
		map.putSync("three", 3);	
		
		map.removeSync("three");

		Assert.assertEquals(1, map.getSync("one"));
		Assert.assertEquals(2, map.getSync("two"));
		Assert.assertEquals(null, map.getSync("three"));

		Async.waitFor(new Operation<Success>() {

			@Override
			public void apply(ValueCallback<Success> callback) {
				map.stop(AsyncCommon.wrap(callback));
				db.close();
			}
		});

	}
}
