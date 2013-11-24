package editevent;

import java.util.ArrayList;
import java.util.List;

import wer.main.R;

import main.Event;
import main.Participant;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class EditEventActivity extends Activity {

	TextView eventName;
	ListView participantsList;
	Button newParticipantButton;
	
	long id;
	Event event;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editevent_activity);
		
		Intent intent = getIntent();
		id = intent.getLongExtra("event_id", -1);
		//query database with id to retrieve event
		//if query returns -1 then we are creating a new event and the fields 
		//should start unpopulated
		
		eventName = (TextView)findViewById(R.id.eventname);
		participantsList = (ListView)findViewById(R.id.participantslist);
		newParticipantButton = (Button)findViewById(R.id.newparticipant);
		
		//if(id == -1) {
		//	event = new Event();
		//}
		//else {
		//	event = DataManager.getEvent(id);
		//}
		
		fetchContacts();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.layout.editevent_activity, menu);
		return true;
	}
	
	private void fetchContacts() {
		
		List<EditEventContact> listViewContacts = new ArrayList<EditEventContact>();
		
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
		
		EditEventAdapterActivity adapter = new EditEventAdapterActivity(this, R.layout.editevent_list_view_components, listViewContacts);
		participantsList.setAdapter(adapter);
	}

}
