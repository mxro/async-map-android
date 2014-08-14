package de.mxro.async.map.android.tests;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowSQLiteDatabase;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import de.mxro.async.Async;
import de.mxro.async.Deferred;
import de.mxro.async.callbacks.ValueCallback;
import de.mxro.async.jre.AsyncJre;
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

		SQLiteConfiguration conf = AsyncMapAndorid.createDefaultConfiguration();
		SQLiteDatabase db = ShadowSQLiteDatabase.create(null);
		
		AsyncMapAndorid.assertTable(db, conf);
		
		final AsyncMap<String, Object> map = AsyncMapAndorid.createMap(conf,
				SerializationJre.newJavaSerializer(),
				db);

		AsyncJre.waitFor(new Deferred<Success>() {

			@Override
			public void get(ValueCallback<Success> callback) {
				map.start(Async.wrap(callback));
			}
		});

		map.putSync("one", 1);
		
		map.putSync("two", 2);
		
		map.putSync("three", 3);
		
		Cursor cursor = db.rawQuery("SELECT * FROM data", new String[] {});
		
		while (!cursor.isAfterLast()) {
			System.out.println(cursor.getString(0));
			cursor.moveToNext();
		}
		
		map.removeSync("three");

		Assert.assertEquals(1, map.getSync("one"));
		Assert.assertEquals(2, map.getSync("two"));

		AsyncJre.waitFor(new Deferred<Success>() {

			@Override
			public void get(ValueCallback<Success> callback) {
				System.out.println(map.getSync("two"));
				map.stop(Async.wrap(callback));
			}
		});

	}
}
