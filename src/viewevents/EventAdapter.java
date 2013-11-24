package viewevents;

import java.util.List;

import wer.main.R;

import main.Event;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EventAdapter extends ArrayAdapter<Event> {

	private int resource;
	
	public EventAdapter(Context context, int resource, List<Event> objects) {
		
		super(context, resource, objects);
		this.resource = resource;
	
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LinearLayout alertView;
		
		Event event = getItem(position);
		
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
		ImageView image = (ImageView)alertView.findViewById(R.id.image);
		
		if(event.isReconciled()) {
			//alertView.setBackgroundColor(Color.GRAY);
			image.setImageResource(R.drawable.gray_button_updated);
		}
		else {
			//alertView.setBackgroundColor(Color.GREEN);
			image.setImageResource(R.drawable.green_button_updated);
		}
		
		eventName.setSelected(true);
		participants.setSelected(true);
		
		eventName.setText(event.getEventName());
		participants.setText(event.getParticipantsAsString());
		
		return alertView;
		
	}

}
