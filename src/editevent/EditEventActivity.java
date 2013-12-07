package editevent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import viewevent.EventActivity;
import viewevents.MainActivity;
import wer.main.R;

import main.DataManager;
import main.Event;
import main.Participant;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Todo:
 * -Make sure fetching contacts does what it is supposed to do
 * -Add functionality to allow the user to create a new participant
 * -Add functionality that when a user taps a contact, it changes to green to signify it is selected
 * -Add onClose method to retrieve selected contacts and add them to that event
 * 
 * @author Matt Hamersky
 * @info Utility activity that gets launched when a user wants to either create or edit and event.  Creating an event
 * starts the fields as blank while editing an event starts the fields with data.
 * Uses layouts: editevent_activity
 *
 */

public class EditEventActivity extends Activity {

	EditText eventName;
	ListView participantsList;
	Button finishEvent;
	
	List<Participant> people;
	List<EditEventContact> listViewContacts;
	EditEventAdapterActivity adapter;
	
	Event event = null; //event object being edited/created
	long id; //id of event being edited/created
	
	DataManager dm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editevent_activity);
		
		dm = new DataManager(this.getApplicationContext());
		
		Log.i("Group1", "here");
		
		eventName = (EditText)findViewById(R.id.eventname);
		participantsList = (ListView)findViewById(R.id.participantslist);
		finishEvent = (Button)findViewById(R.id.finish);
		
		participantsList.setOnItemClickListener(new OnItemClickListener() {
		    @Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		    	EditEventContact contact = listViewContacts.get(position);
		    	contact.changeAlreadyInEventToOpposite();
		    	adapter.update(listViewContacts);
		    	participantsList.invalidate();
		    }

		});
		
		registerForContextMenu(participantsList);
		
		Intent intent = getIntent();
		if(intent.getBooleanExtra("isForEditing", false)) { //start fields with data
			id = intent.getLongExtra("event_id", -1);
			event = dm.getEvent(id);
			fetchContactsExistingEvent();
			populateFieldsExistingEvent();
		}
		else {
			fetchContactsNewEvent();
			populateFieldsNewEvent();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void addParticipants(View view) {
		
	}
	
	private void fetchContactsExistingEvent() {
		
		listViewContacts = new ArrayList<EditEventContact>();
		
		Cursor cursor = getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
				null, null,null, null);
		
		List<Participant> participants = event.getAllParticipants();
		
		while (cursor.moveToNext()) {
			
			boolean isAlreadyInList = false;
			
			String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
			String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			
			
			for(int i = 0; i < participants.size(); i++) {
				if((name.equals(participants.get(i).getName())) && (phoneNumber.equals(participants.get(i).getPhoneNumber()))) {
					isAlreadyInList = true;
					break;
				}
			}
			
			EditEventContact temp = new EditEventContact(name, phoneNumber, isAlreadyInList);
			listViewContacts.add(temp);
			
		}
		
		adapter = new EditEventAdapterActivity(this, R.layout.editevent_list_view_components, listViewContacts);
		participantsList.setAdapter(adapter);
	}
	
	private void fetchContactsNewEvent() {
		listViewContacts = new ArrayList<EditEventContact>();
		
		Cursor cursor = getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
				null, null,null, null);
		
		while (cursor.moveToNext()) {			
			String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
			String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			
			EditEventContact temp = new EditEventContact(name, phoneNumber, false);
			listViewContacts.add(temp);
			
		}
		
		adapter = new EditEventAdapterActivity(this, R.layout.editevent_list_view_components, listViewContacts);
		participantsList.setAdapter(adapter);
	}
	
	private void populateFieldsExistingEvent() {
		eventName.setText(event.getEventName());
		finishEvent.setText("Finish");
	}
	
	private void populateFieldsNewEvent() {
		eventName.setText("");
		finishEvent.setText("Create Event");
	}
	
	public void finished(View view) {
		if(event == null) {
			if(eventName.getText().toString().equals("")) { //if the user didn't enter an event name then we won't create a new event
				Intent resultIntent = new Intent();
				resultIntent.putExtra("event id", -1);
				setResult(Activity.RESULT_OK, resultIntent);
			}
			event = new Event(eventName.getText().toString(), new Date(), false);
		}
		else {
			event.setName(eventName.getText().toString());
		}
		
		id = dm.saveEvent(event);
		
		changeContactsToParticipants();
		event.setParticipants(people);
		
		Intent resultIntent = new Intent();
		resultIntent.putExtra("event id", id);
		setResult(Activity.RESULT_OK, resultIntent);
		dm.close();
		finish();
	}
	
	private void changeContactsToParticipants() {
		
		people = new ArrayList<Participant>();
		for(int i = 0; i < listViewContacts.size(); i++) {
			EditEventContact temp = listViewContacts.get(i);
			if(temp.isAlreadyInEvent()) {
				Participant participant = new Participant(temp.getName(), id, temp.getPhoneNumber(), 0.0);
				dm.saveParticipant(participant);
				people.add(participant);
			}
		}
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	  //if (v.getId()==R.id.list) {
	    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
	    menu.setHeaderTitle(listViewContacts.get(info.position).getName());
	    String[] menuItems = {"Edit", "Delete", "Cancel"};
	    for (int i = 0; i<menuItems.length; i++) {
	      menu.add(Menu.NONE, i, i, menuItems[i]);
	    }
	  //}
	}
	
	@Override
    public boolean onContextItemSelected(MenuItem item){
        switch(item.getItemId()){
	        case 0:  //edit
	            
	            break;
	        case 1: //delete
	        	
	        	break;
	        default: //cance
	        	break;
        }
        
        return true;
        
	}

}
