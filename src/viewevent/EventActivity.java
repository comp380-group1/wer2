package viewevent;

import wer.main.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

/**
 * Todo:
 * -Basically everything...
 * 
 * @author Matt Hamersky
 * @info Displays information about a particular event including name, participants, and expenses.
 * The user is allowed to tap a participant/expense and another activity is launched displaying more
 * information about it.
 * Uses layouts: viewevent_main
 */

public class EventActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewevent_main);
		
		System.out.println("here");
		//this is a test to see what happens
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event, menu);
		return true;
	}

}
