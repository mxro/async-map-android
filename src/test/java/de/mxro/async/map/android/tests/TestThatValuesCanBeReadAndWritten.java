package de.mxro.async.map.android.tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.app.Activity;

@RunWith(RobolectricTestRunner.class)
public class TestThatValuesCanBeReadAndWritten {

	
	@Test
	  public void clickingButton_shouldChangeResultsViewText() throws Exception {
	    TestActivity activity = Robolectric.buildActivity(TestActivity.class).create().get();
	}
	
}
