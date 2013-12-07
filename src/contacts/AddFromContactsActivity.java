package contacts;

import java.util.ArrayList;
import java.util.List;

import main.DataManager;
import main.Event;
import main.Participant;

import editevent.EditEventAdapterActivity;
import editevent.EditEventContact;
import wer.main.R;
import wer.main.R.layout;
import wer.main.R.menu;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class AddFromContactsActivity extends Activity {

	ListView contactsListView;
	Button addButton;
	
	List<Participant> alreadyInEventParticipants;
	List<EditEventContact> contacts = new ArrayList<EditEventContact>();
	AddContactsAdapterActivity adapter;
	
	long id;
	Event event = null;
	
	DataManager dm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contacts_main);
		
		dm = new DataManager(this.getApplicationContext());
		
		contactsListView = (ListView)findViewById(R.id.contactinfo);
		addButton = (Button)findViewById(R.id.addcontacts);
		
		Intent intent = getIntent();
		id = intent.getLongExtra("event_id", -1);
		event = dm.getEvent(id);
		try {
			alreadyInEventParticipants = dm.getParticipantsByEventId(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		fetchContacts();
		
		contactsListView.setOnItemClickListener(new OnItemClickListener() {
		    @Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		    	EditEventContact contact = contacts.get(position);
		    	contact.changeAlreadyInEventToOpposite();
		    	adapter.update(contacts);
		    	contactsListView.invalidate();
		    }
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_from_contacts, menu);
		return true;
	}
	
	private void fetchContacts() {
		
		Cursor cursor = getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
				null, null,null, null);
		boolean isIn;
		while (cursor.moveToNext()) {			
			String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
			String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			EditEventContact temp = null;
			isIn = false;
			for(int i = 0; i < alreadyInEventParticipants.size(); i++) {
				//check if the contact is already in the event, if they are, exclude them from the list
				if(alreadyInEventParticipants.get(i).getName().equals(name) &&
						alreadyInEventParticipants.get(i).getPhoneNumber().equals(phoneNumber)) {
					isIn = true;
					break;
				}
			}
			if(!isIn) {
				temp = new EditEventContact(name, phoneNumber, false, -1);
				contacts.add(temp);
			}
		}
		
		adapter = new AddContactsAdapterActivity(this, R.layout.editevent_list_view_components, contacts);
		contactsListView.setAdapter(adapter);
	}
	
	public void addContacts(View view) {
		for(int i = 0; i < contacts.size(); i++) {
			EditEventContact contact = contacts.get(i);
			if(contact.isAlreadyInEvent()) {
				Participant person = new Participant(contact.getName(), id, contact.getPhoneNumber());
				dm.saveParticipant(person);
			}
		}
		Intent resultIntent = new Intent();
		resultIntent.putExtra("event id", id);
		setResult(Activity.RESULT_OK, resultIntent);
		dm.close();
		finish();
	}
	
	/*private void fetchContactsNewEvent() {
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
	}*/

}
