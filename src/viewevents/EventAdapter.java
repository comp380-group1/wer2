package viewevents;

import java.util.List;

import wer2.main.R;

import main.Event;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Todo:  
 * -Remove gray/green circle and go back to gray/green background for each row.  
 * -Add edit button in place of circle OR have the user HOLD-CLICK the row to go into editing mode.  
 * -Possibly extend text fields for participants/event name AND/OR change the data that is displayed in those fields.
 * 
 * @author Matt Hamersky
 * @info Adapter for MainActivity.class.  Handles displaying the relavent data of each event in the listview.
 * Uses layouts: viewevents_list_view_components
 */

public class EventAdapter extends ArrayAdapter<Event> {

	private int resource;
	
	List<Event> events;
	
	public EventAdapter(Context context, int resource, List<Event> objects) {
		
		super(context, resource, objects);
		this.resource = resource;
		
		events = objects;
	
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LinearLayout alertView;
		
		Event event = getItem(position);
		//Event event = events.get(position);
		
		if(convertView == null) {
			alertView = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi;
			vi = (LayoutInflater)getContext().getSystemService(inflater);
			vi.inflate(resource, alertView, true);
		}
		else {
			alertView = (LinearLayout)convertView;
		}
		
		TextView eventName = (TextView)alertView.findViewById(R.id.eventname);
		TextView participants = (TextView)alertView.findViewById(R.id.participants);
		
		if(event.isReconciled()) {
			alertView.setBackgroundColor(Color.GRAY);
		}
		else {
			alertView.setBackgroundColor(Color.parseColor("#3BA81D"));
		}
		
		eventName.setSelected(true);
		participants.setSelected(true);
		
		eventName.setText(event.getEventName());
		participants.setText(event.getParticipantsAsString());
		
		return alertView;
		
	}
	
	public void update(List<Event> events) {
		this.events = events;
		notifyDataSetChanged();
	}

}
