package de.mxro.async.map.android.tests;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowSQLiteDatabase;

import de.mxro.async.map.AsyncMap;
import de.mxro.async.map.android.AsyncMapAndorid;
import de.mxro.async.map.android.SQLiteConfiguration;
import de.mxro.serialization.jre.SerializationJre;

@RunWith(RobolectricTestRunner.class)
public class TestThatValuesCanBeReadAndWritten {

	@Test
	public void test() throws Exception {

		SQLiteConfiguration conf = AsyncMapAndorid.createDefaultConfiguration();
		AsyncMap<String, Object> map = AsyncMapAndorid.createMap(conf,
				SerializationJre.newJavaSerializer(),
				ShadowSQLiteDatabase.create(null));

		map.putSync("one", 1);

		map.putSync("two", 2);
		
		Assert.assertEquals(1, map.getSync("one"));
		Assert.assertEquals(2, map.getSync("two"));
		
		

	}
}
