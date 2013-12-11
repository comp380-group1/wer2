package viewevents;

import java.util.Date;
import java.util.List;


import java.util.ArrayList;

import editparticipants.EditParticipantsActivity;
import eventstatus.EventStatusActivity;

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
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Todo: 
 * -Fix refreshing listview after editing an event
 * -Make sure participants are actually being saved to the event
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
	
	public static final int EDIT_EVENT = 382497; //random number for onActivityResult - shouldn't interfere with any other results
	public static final int NEW_EVENT = 4327532;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewevents_main);
		
		dm = new DataManager(this.getApplicationContext());
		
		newEventButton = (Button)findViewById(R.id.newevent);
		
		try {
			events = dm.getAllEvents();
			loadParticipantsForEachEvent();
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
		    	intent.putExtra("event_id", eventID);
		    	startActivity(intent);
		    }

		});
		
		registerForContextMenu(listview);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
	    menu.setHeaderTitle(events.get(info.position).getName());
	    String[] menuItems = {"Status", "Edit", "Delete", "Cancel"};
	    for (int i = 0; i<menuItems.length; i++) {
	      menu.add(Menu.NONE, i, i, menuItems[i]);
	    }
	}
	
	@Override
    public boolean onContextItemSelected(MenuItem item){
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		Event event = (Event)listview.getAdapter().getItem(info.position);
		
        switch(item.getItemId()){
        	case 0: //status
        		Intent status_intent = new Intent(MainActivity.this, EventStatusActivity.class);
        		status_intent.putExtra("event_id", event.getId());
        		startActivity(status_intent);
        		break;
	        case 1:  //edit
	        	if(event.isReconciled()) {
	        		Toast.makeText(getApplicationContext(), "Reconciled events are not eligible to be edited",
	        				   4).show();
	        		break;
	        	}
	            Intent edit_intent = new Intent(MainActivity.this, EditParticipantsActivity.class);
	            edit_intent.putExtra("isForEditing", true);
	            edit_intent.putExtra("event_id", event.getId());
	            startActivityForResult(edit_intent, EDIT_EVENT);
	            break;
	        case 2: //delete
	        	events.remove(event);
	        	dm.deleteEvent(event);
	        	try {
	        		events = dm.getAllEvents();
	        		loadParticipantsForEachEvent();
	        	} catch (Exception e) {
	        		e.printStackTrace();
	        	}
	        	adapter = new EventAdapter(this, R.layout.viewevents_list_view_components, events);
	    		listview.setAdapter(adapter);
	        	refreshListView();
	        	break;
	        default: //cancel
	        	break;
        }
        
        return true;
        
	}
	
	@Override
	public void onResume() {
		super.onResume();
		try {
			events = dm.getAllEvents();
			loadParticipantsForEachEvent();
		} catch (Exception e) {
			e.printStackTrace();
		}
		adapter = new EventAdapter(this, R.layout.viewevents_list_view_components, events);
		listview.setAdapter(adapter);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode) {
			case NEW_EVENT:
				if (resultCode == Activity.RESULT_OK) {
					Event event = dm.getEvent(data.getLongExtra("event id", -1));
			    	if(event == null) {
			    		Toast.makeText(getApplicationContext(), "Problem saving event", 4).show();
			        }
			        else {
			        	events.add(event);
			        	try {
							events = dm.getAllEvents();
						} catch (Exception e) {
							e.printStackTrace();
						}
			        	loadParticipantsForEachEvent();
			        	adapter = new EventAdapter(this, R.layout.viewevents_list_view_components, events);
						listview.setAdapter(adapter);
			        	refreshListView();
			        }
				}
			    break;
			case EDIT_EVENT:
				if(resultCode == Activity.RESULT_OK) {
					try {
						events = dm.getAllEvents(); //because we freakin can
						loadParticipantsForEachEvent();
					} catch(Exception e) {
						Log.i("ERROR", "Couldn't load events - MainActivity");
					}
					adapter = new EventAdapter(this, R.layout.viewevents_list_view_components, events);
					listview.setAdapter(adapter);
					refreshListView();
				}
				break;
			default:
		    	break;
		}
	}
	
	public void createNewEvent(View view) {
		Intent intent = new Intent(MainActivity.this, EditParticipantsActivity.class);
		intent.putExtra("isForEditing", false); //start activity with blank fields
		startActivityForResult(intent, NEW_EVENT);
	}
	
	private void refreshListView() {
		adapter.update(events);
		adapter.notifyDataSetChanged();
    	listview.invalidate();
	}
	
	private void loadParticipantsForEachEvent() {
		for(int i = 0; i < events.size(); i++) {
			try {
				events.get(i).setParticipants(dm.getParticipantsByEventId(events.get(i).getId()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		dm.close();
	}
}
