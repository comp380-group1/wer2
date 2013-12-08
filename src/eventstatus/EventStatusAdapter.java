package eventstatus;

import java.text.DecimalFormat;
import java.util.List;

import wer.main.R;

import main.ExpenseParticipant;
import main.Participant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EventStatusAdapter extends ArrayAdapter<Participant> {

	private int resource;
	
	public EventStatusAdapter(Context context, int resource, List<Participant> objects) {
		super(context, resource, objects);
		this.resource = resource;
		
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LinearLayout alertView;
		
		Participant participant = getItem(position);
		
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
		
		TextView participantName = (TextView)alertView.findViewById(R.id.name);
		TextView participantAmount = (TextView)alertView.findViewById(R.id.amount);
		TextView participantNumber = (TextView)alertView.findViewById(R.id.number);
		
		DecimalFormat f = new DecimalFormat("##.00");
		
		participantName.setText(participant.getName());
		participantAmount.setText("$ " + f.format(participant.getCurrentBalance()));
		participantNumber.setText(participant.getPhoneNumber());
		
		return alertView;
		
	}
	
}
