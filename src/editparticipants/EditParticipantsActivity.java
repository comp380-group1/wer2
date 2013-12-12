package editparticipants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import contacts.AddFromContactsActivity;

import viewevent.EventActivity;
import viewevents.MainActivity;
import wer.main.R;

import main.DataManager;
import main.Event;
import main.Expense;
import main.ExpenseParticipant;
import main.Participant;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
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

public class EditParticipantsActivity extends Activity {

	EditText eventName;
	ListView participantsList;
	Button finishEvent;
	
	//if the user adds contacts, this saves the event in the db, if the user then hits the
	//back button and NOT the "finish" or "create event" button then the event should not 
	//stay in the db.  This boolean keeps track of what the user wants to do in regards
	//to saving the event.
	boolean hasBeenFinished = false;
	
	List<Participant> people;
	List<EditParticipantsContact> listViewContacts = new ArrayList<EditParticipantsContact>();
	EditParticipantsAdapterActivity adapter;
	
	public static final int ADD_CONTACT = 234897; //don't think I'll need this...
	public static final int ADD_CONTACTS = 75263847;
	
	Event event = null; //event object being edited/created
	public static long id = -1; //id of event being edited/created
	
	DataManager dm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editevent_main);
		id = -1;
		dm = new DataManager(this.getApplicationContext());
		
		eventName = (EditText)findViewById(R.id.eventname);
		participantsList = (ListView)findViewById(R.id.participantslist);
		finishEvent = (Button)findViewById(R.id.finish);
		
		registerForContextMenu(participantsList);
		
		Intent intent = getIntent();
		if(intent.getBooleanExtra("isForEditing", false)) { //start fields with data
			id = intent.getLongExtra("event_id", -1);
			event = dm.getEvent(id);
			hasBeenFinished = true;
			
			///////////////////////////////////////////////////////////////////////////
			//Expense expense = new Expense(id, "drinks", new Date(), 0.0);
			//Expense expense2 = new Expense(id, "food", new Date(), 12.76);
			//dm.saveExpense(expense);
			//dm.saveExpense(expense2);
			///////////////////////////////////////////////////////////////////////////
			fetchParticipantsExistingEvent();
			populateFieldsExistingEvent();
		}
		else {
			populateFieldsNewEvent();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void fetchParticipantsExistingEvent() {
		List<Participant> event_participants = null;
		try {
			event_participants = dm.getParticipantsByEventId(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		changeParticipantsToContacts(event_participants);
		adapter = new EditParticipantsAdapterActivity(this, R.layout.editevent_list_view_components, listViewContacts);
		participantsList.setAdapter(adapter);
	}
	
	public void addParticipant(View view) {
		boolean isEdit = false;
		addParticipant(isEdit, null);
	}
	
	private void addParticipant(final boolean isEdit, final EditParticipantsContact contact) {
		LayoutInflater li = LayoutInflater.from(getBaseContext());
		View promptsView = li.inflate(R.layout.editevent_dialog, null);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		alertDialogBuilder.setView(promptsView);
		
		final EditText customName = (EditText) promptsView
				.findViewById(R.id.customname);
		final EditText customNumber = (EditText) promptsView
				.findViewById(R.id.customnumber);
		customNumber.setRawInputType(Configuration.KEYBOARD_12KEY);
		
		if(isEdit) {
			customName.setText(contact.getName());
			customNumber.setText(contact.getPhoneNumber());
		}

		// set dialog message
		alertDialogBuilder
			.setCancelable(false)
			.setPositiveButton("OK",
			  new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int id) {
			    	if(EditParticipantsActivity.id == -1) { //if the event has never been saved, save it so we have an event id
						event = new Event(eventName.getText().toString(), new Date(), false, false);
						EditParticipantsActivity.id = dm.saveEvent(event);
						Log.i("group1", Long.toString(EditParticipantsActivity.id));
					}
			    	Participant person = null;
			    	List<Expense> expenses = null;
		    		try {
		    			expenses = dm.getExpensesByEventId(EditParticipantsActivity.id);
		    		} catch (Exception e) {
		    			e.printStackTrace();
		    		}
		    		
			    	if(isEdit) {
			    		final Participant participant = dm.getParticipant(contact.getId());
			    		//person = new Participant(contact.getId(), EditParticipantsActivity.id, customName.getText().toString(), customNumber.getText().toString(), 0.0);
			    		participant.addPhoneNumber(customNumber.getText().toString());
			    		participant.setName(customName.getText().toString());
			    		dm.saveParticipant(participant);
			    		changeParticipantToContact(participant);
			    	}
			    	else {
			    		person = new Participant(customName.getText().toString(), EditParticipantsActivity.id, customNumber.getText().toString());
			    		long participantId = dm.saveParticipant(person);
			    		if(expenses != null) {
							for(int i = 0; i < expenses.size(); i++) {
								dm.saveExpenseParticipant(new ExpenseParticipant(id, expenses.get(i).getId(), participantId, 0.0, 0.0, false));
							}
						}
			    		changeParticipantToContact(person);
			    	}
			    	adapter = new EditParticipantsAdapterActivity(EditParticipantsActivity.this, R.layout.editevent_list_view_components, listViewContacts);
					participantsList.setAdapter(adapter);
					refreshListView();
			    }
			  })
			.setNegativeButton("Cancel",
			  new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog,int id) {
			    	dialog.cancel();
			    }
			  });

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}
	
	public void addParticipants(View view) {
		if(id == -1) { //if the event has never been saved, save it so we have an event id
			event = new Event(eventName.getText().toString(), new Date(), false, false);
			id = dm.saveEvent(event);
		}
		
		Intent intent = new Intent(EditParticipantsActivity.this, AddFromContactsActivity.class);
		intent.putExtra("event_id", id);
		startActivityForResult(intent, ADD_CONTACTS);
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
			event = new Event(eventName.getText().toString(), new Date(), false, false);
		}
		else {
			event.setName(eventName.getText().toString());
		}
		
		changeContactsToParticipants(); //might not need these two lines....
		event.setParticipants(people); //requires furthing testing when the DataManager implements nesting to know for sure
		
		id = dm.saveEvent(event);
		
		Intent resultIntent = new Intent();
		resultIntent.putExtra("event id", id);
		setResult(Activity.RESULT_OK, resultIntent);
		hasBeenFinished = true;
		finish();
	}
	
	private void changeParticipantsToContacts(List<Participant> event_participants) {
		listViewContacts = new ArrayList<EditParticipantsContact>();
		for(int i = 0; i < event_participants.size(); i++) {
			Participant person = event_participants.get(i);
			listViewContacts.add(new EditParticipantsContact(person.getName(), person.getPhoneNumber(), true, person.getId()));
		}		
	}
	
	private void changeParticipantToContact(Participant person) {
		if(listViewContacts == null) {
			listViewContacts = new ArrayList<EditParticipantsContact>();
		}
		//check if we're updating a contact or adding a new one
		for(int i = 0; i < listViewContacts.size(); i++) {
			if(person.getId() == listViewContacts.get(i).getId()) {
				listViewContacts.get(i).setName(person.getName());
				listViewContacts.get(i).setPhoneNumber(person.getPhoneNumber());
				return;
			}
		}
		listViewContacts.add(new EditParticipantsContact(person.getName(), person.getPhoneNumber(), true, person.getId()));
	}
	
	private void changeContactsToParticipants() {
		people = new ArrayList<Participant>();
		for(int i = 0; i < listViewContacts.size(); i++) {
			EditParticipantsContact temp = listViewContacts.get(i);
			if(temp.getId() == -1) {
				Participant participant = new Participant(temp.getName(), id, temp.getPhoneNumber(), 0.0);
				dm.saveParticipant(participant);
				people.add(participant);
			}
		}
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
	    menu.setHeaderTitle(listViewContacts.get(info.position).getName());
	    String[] menuItems = {"Edit", "Delete", "Cancel"};
	    for (int i = 0; i<menuItems.length; i++) {
	      menu.add(Menu.NONE, i, i, menuItems[i]);
	    }
	}
	
	@Override
    public boolean onContextItemSelected(MenuItem item){
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		EditParticipantsContact tempContact = (EditParticipantsContact)participantsList.getAdapter().getItem(info.position);
		
        switch(item.getItemId()){
	        case 0:  //edit
	            addParticipant(true, tempContact);
	            break;
	        case 1: //delete
	        	event = dm.getEvent(id); //refresh the locally stored event
	        	//check to see if the participant is participating in any expenses
	        	//if they are, they can't be deleted until those dependencies are removed
	        	try {
	        		List<Expense> expenses = dm.getExpensesByEventId(id);
	        		for(int i = 0; i < expenses.size(); i++) { //initial search to see if the participant has participating dependencies
	        			if(expenses.get(i).isParticipantParticipating(tempContact.getId())) {
	        				Toast.makeText(getApplicationContext(), "Remove expense dependencies before deleting", 4).show();
	        				return true;
	        			}
	        		}
	        		/*ExpenseParticipant ep;
	        		for(int i = 0; i < expenses.size(); i++) { //second search to actually remove the participant because there are no dependencies
	        			ep = expenses.get(i).returnAndRemoveParticipantById(tempContact.getId());
	        			if(ep != null) {
	        				dm.deleteExpenseParticipant(ep);
	        			}
	        		}*/
	        	} catch (Exception e) {
	        		e.printStackTrace();
	        	}
	        	event.removeParticipantById(tempContact.getId());
	        	listViewContacts.remove(tempContact);
	        	dm.deleteExpenseParticipantByParticipantId(tempContact.getId());
	        	dm.deleteParticipant(tempContact.getId());
	        	refreshListView();
	        	break;
	        default: //cancel
	        	break;
        }
        
        return true;
        
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode) {
			case ADD_CONTACTS:
				if (resultCode == Activity.RESULT_OK) {
					try {
						people = dm.getParticipantsByEventId(id);
						changeParticipantsToContacts(people);
						adapter = new EditParticipantsAdapterActivity(this, R.layout.editevent_list_view_components, listViewContacts);
						participantsList.setAdapter(adapter);
						refreshListView();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			    break;
			default:
				break;
			    	
		}
	}
	
	private void refreshListView() {
		runOnUiThread(new Runnable() {
	        @Override
	        public void run() {
	                adapter.notifyDataSetChanged();
	        }
	    });
		participantsList.invalidate();
		participantsList.invalidateViews();
	}
	
	@Override
	public void onBackPressed(){
		Log.i("Group 1", "Back button pressed");
		if(!hasBeenFinished) {
			dm.deleteEvent(id);
		}
		dm.close();
	    finish();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(!hasBeenFinished) {
			dm.deleteEvent(id);
		}
		dm.close();
	}
}
