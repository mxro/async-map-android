package de.mxro.async.map.android.tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowSQLiteDatabase;

import android.database.sqlite.SQLiteDatabase;
import de.mxro.async.map.AsyncMap;
import de.mxro.async.map.android.AsyncMapAndorid;
import de.mxro.async.map.android.SQLiteConfiguration;
import de.mxro.serialization.jre.SerializationJre;

@RunWith(RobolectricTestRunner.class)
public class TestThatValuesCanBeReadAndWritten {

	
	@Test
	  public void clickingButton_shouldChangeResultsViewText() throws Exception {
	    TestActivity activity = Robolectric.buildActivity(TestActivity.class).create().get();
	    
	    
	    SQLiteConfiguration conf = AsyncMapAndorid.createDefaultConfiguration(); 
		AsyncMap<String, Object> test = AsyncMapAndorid.createMap(conf, SerializationJre.newJavaSerializer(),  ShadowSQLiteDatabase.create(null));
	    		
	    
	}
	
}
