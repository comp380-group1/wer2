package eventstatus;

import java.util.List;

import main.DataManager;
import main.Event;
import main.Participant;
import wer.main.R;
import wer.main.R.layout;
import wer.main.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.ListView;

public class EventStatusActivity extends Activity {

	ListView eventStatus;
	
	Event event = null;
	List<Participant> participants;
	long id;
	
	EventStatusAdapter adapter;
	
	DataManager dm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eventstatus_main);
		
		dm = new DataManager(this.getApplicationContext());
		
		eventStatus = (ListView)findViewById(R.id.eventstatuslistview);
		
		Intent intent = getIntent();
		id = intent.getLongExtra("event_id", -1);
		event = dm.getEvent(id);
		try {
			participants = dm.getParticipantsByEventId(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		adapter = new EventStatusAdapter(EventStatusActivity.this, R.layout.eventstatus_list_view_components, participants);
		eventStatus.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.event_status, menu);
		return true;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		dm.close();
	}

}
