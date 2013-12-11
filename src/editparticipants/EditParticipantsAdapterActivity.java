package editparticipants;

import java.util.List;

import wer.main.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Todo: COMPLETED
 * 
 * @author Matt Hamersky
 * @info Adapter class for EditEventActivity.class, handles displaying the event name and list of contacts for
 * an event.
 * Uses layouts: editevent_list_view_components
 *
 */

public class EditParticipantsAdapterActivity extends ArrayAdapter<EditParticipantsContact> {

	private int resource;
	
	List<EditParticipantsContact> contacts;
	
	public EditParticipantsAdapterActivity(Context context, int resource, List<EditParticipantsContact> objects) {
		
		super(context, resource, objects);
		this.resource = resource;
		contacts = objects;
	
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LinearLayout alertView;
		
		//EditEventContact contact = getItem(position);
		EditParticipantsContact contact = contacts.get(position);
		
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

		TextView name = (TextView)alertView.findViewById(R.id.name);
		TextView number = (TextView)alertView.findViewById(R.id.phonenumber);
		
		name.setText(contact.getName());
		number.setText(contact.getPhoneNumber());
		
		/*if(contact.isAlreadyInEvent()) {
			alertView.setBackgroundColor(Color.GREEN);
		}
		else {
			alertView.setBackgroundColor(Color.WHITE);
		}*/
		
		return alertView;
	}
	
	public void update(List<EditParticipantsContact> contacts) {
		this.contacts = contacts;
		notifyDataSetChanged();
	}

}
