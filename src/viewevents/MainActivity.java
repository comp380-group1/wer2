package viewevents;

import java.util.List;


import java.util.ArrayList;

import viewevent.EventActivity;
import wer.main.R;

import main.Event;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

/**
 * Todo: 
 * -Add functionality for editing an event (whether that is HOLD-CLICK the row or ON-CLICK for
 * 		an edit button).
 * -Add functionality for connecting with the database.
 * -Add functionality for creating a new event (requires launching the add/edit activity).
 * 
 * @author Matt Hamersky
 * @info Activity launched when the application starts.  Displays all the events that the user has
 * created.  Ability to add/edit/view an event while on this activity.
 * Uses layouts: viewevents_main
 */

public class MainActivity extends Activity {

	
	ListView listview;
	Button newEventButton;
	List<Event> events = null;
	
	EventAdapter adapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewevents_main);
		
		newEventButton = (Button)findViewById(R.id.newevent);
		newEventButton.setBackgroundColor(Color.GREEN);
		
		events = new ArrayList<Event>();
		createEvents();
		
		listview = (ListView)findViewById(R.id.lstText);
		listview.setItemsCanFocus(true);
		
		//adapter = new EventAdapter(this, R.layout.viewevents_list_view_components, events);
		//listview.setAdapter(adapter);
		
		
		/* This is for viewing the event, NOT editing the event
		 */
		listview.setOnItemClickListener(new OnItemClickListener() {
		    @Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		    	Event event = events.get(position);
		    	
		    	Intent intent = new Intent(MainActivity.this, EventActivity.class);
		    	long eventID = event.getId();
		    	intent.putExtra("id", eventID);
		    	startActivity(intent);
		    }

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void createNewEvent(View view) {
		
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////
	
	
	/**
	 * Realistically, this method would be used to grab the data from the database once the
	 * app starts up.
	 */
	private void createEvents() {
		
		/*events.add(new Event("Big Bear", false));
		events.add(new Event("Getting an Android for Matt", true));
		events.add(new Event("Camping Trip", false));
		events.add(new Event("Big Bear", false));
		events.add(new Event("Getting an Android for Matt", true));
		events.add(new Event("Camping Trip", false));
		events.add(new Event("Big Bear", false));
		events.add(new Event("Getting an Android for Matt", true));
		events.add(new Event("Camping Trip", false));
		events.add(new Event("Big Bear", false));
		events.add(new Event("Getting an Android for Matt", true));
		events.add(new Event("Camping Trip", false));
		events.add(new Event("Big Bear", false));
		events.add(new Event("Getting an Android for Matt", true));
		events.add(new Event("Camping Trip", false));
		events.add(new Event("Big Bear", false));
		events.add(new Event("Getting an Android for Matt", true));
		events.add(new Event("Camping Trip", false));*/
		
	}

}
