package viewevents;

import java.util.Date;
import java.util.List;


import java.util.ArrayList;

import editevent.EditEventActivity;

import viewevent.EventActivity;
import wer.main.R;

import main.DataManager;
import main.Event;
import main.Payment;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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
	
	DataManager dm;
	
	EventAdapter adapter;
	
	public static final int EDIT_EVENT = 382497;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewevents_main);
		
		dm = new DataManager(this.getApplicationContext());
		
		newEventButton = (Button)findViewById(R.id.newevent);
		newEventButton.setBackgroundColor(Color.GREEN);
		
		try {
			events = dm.getAllEvents();
		} catch(Exception e) {
			Log.i("ERROR", "Couldn't load events - MainActivity");
		}
		
		listview = (ListView)findViewById(R.id.lstText);
		listview.setItemsCanFocus(true);
		
		adapter = new EventAdapter(this, R.layout.viewevents_list_view_components, events);
		listview.setAdapter(adapter);
		
		
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
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/*public void createNewEvent(View view) {
		Event e = new Event("Big Bear", new Date(), false);
		dm.saveEvent(e);
		
		Payment p = new Payment(2, "Matt", "Denis", 5.00);
		dm.savePayment(p);
		
		p = dm.getPayment(p.getId());
		
		try {
			List<Payment> payments = dm.getPaymentsByEventId(1);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}*/
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
	  switch(requestCode) {
	    case (EDIT_EVENT) : {
	      if (resultCode == Activity.RESULT_OK) {
	        Event event = dm.getEvent(data.getLongExtra("event id", -1));
	        if(event == null) {
	        	Toast.makeText(getApplicationContext(), "Problem saving event", 4).show();
	        }
	        else {
	        	events.add(event);
	        	adapter.update(events);
	        	listview.invalidate();
	        }
	      }
	      break;
	    } 
	  }
	}
	
	public void createNewEvent(View view) {
		Intent intent = new Intent(MainActivity.this, EditEventActivity.class);
		intent.putExtra("isForEditing", false); //start activity with blank fields
		startActivityForResult(intent, EDIT_EVENT);
	}
}
