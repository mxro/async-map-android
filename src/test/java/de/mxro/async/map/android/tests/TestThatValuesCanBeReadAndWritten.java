package de.mxro.async.map.android.tests;

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
	  public void clickingButton_shouldChangeResultsViewText() throws Exception {
	    TestActivity activity = Robolectric.buildActivity(TestActivity.class).create().get();
	    
	    
	    SQLiteConfiguration conf = AsyncMapAndorid.createDefaultConfiguration(); 
		AsyncMap<String, Object> map = AsyncMapAndorid.createMap(conf, SerializationJre.newJavaSerializer(),  ShadowSQLiteDatabase.create(null));
	    		
		

	    
	}
	
}
