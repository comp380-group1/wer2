package contacts;

import java.util.ArrayList;

import main.Participant;

import editevent.EditEventAdapterActivity;
import editevent.EditEventContact;
import wer.main.R;
import wer.main.R.layout;
import wer.main.R.menu;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.app.Activity;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class AddFromContactsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contacts_main);
		
		
		
		
		/*participantsList.setOnItemClickListener(new OnItemClickListener() {
		    @Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		    	EditEventContact contact = listViewContacts.get(position);
		    	contact.changeAlreadyInEventToOpposite();
		    	adapter.update(listViewContacts);
		    	participantsList.invalidate();
		    }

		});*/
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_from_contacts, menu);
		return true;
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
