package editexpense;

import java.text.DecimalFormat;
import java.util.List;

import main.DataManager;
import main.Expense;
import main.ExpenseParticipant;
import wer2.main.R;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class EditExpenseAdapter extends ArrayAdapter<ExpenseParticipant> {

private int resource;
	
	List<ExpenseParticipant> expensesList;
	
	public EditExpenseAdapter(Context context, int resource, List<ExpenseParticipant> objects) {
		
		super(context, resource, objects);
		this.resource = resource;
		
		expensesList = objects;
	
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LinearLayout alertView;
		
		final ExpenseParticipant expenseParticipant = getItem(position);
		
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
		final CheckBox inOrOutBox = (CheckBox)alertView.findViewById(R.id.inoroutbox);
		final EditText amountPaid = (EditText)alertView.findViewById(R.id.participantamount);
		
		amountPaid.setRawInputType(Configuration.KEYBOARD_12KEY);
		
		DecimalFormat f = new DecimalFormat("##.00");
		
		participantName.setText(expenseParticipant.getParticipant().getName());
		if(expenseParticipant.getPaid() == 0) {
			amountPaid.setText("0");
		}
		else {
			amountPaid.setText("" + f.format(expenseParticipant.getPaid()));
		}
		if(expenseParticipant.isParticipating()) {
			inOrOutBox.setChecked(true);
		}
		else {
			inOrOutBox.setChecked(false);
		}
		
		participantName.setSelected(true);
		
		amountPaid.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
			  if (!hasFocus) {
			   try {
				   expenseParticipant.setAmount(Double.parseDouble(amountPaid.getText().toString()));
				   expenseParticipant.setParticipating(true);
				   if(Double.parseDouble(amountPaid.getText().toString()) < 0) {
					   //Toast.makeText(getContext(), "Not valid input - resetting field", 4).show();
					   amountPaid.setText("0");
				   }
			   } catch(Exception e) {
				   //Toast.makeText(getContext(), "Not valid input - resetting field", 4).show();
				   amountPaid.setText("0");
				   expenseParticipant.setAmount(0.0);
			   }
			  }
			  else {
				  double value = Double.parseDouble(amountPaid.getText().toString());
				  if(value == 0) {
					  amountPaid.setText("");
				  }
			  }
			}
		});
		
		inOrOutBox.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	if(inOrOutBox.isChecked()) {
		    		expenseParticipant.setParticipating(true);
		    	}
		    	else {
		    		expenseParticipant.setParticipating(false);
		    		expenseParticipant.setAmount(0.0);
		    		amountPaid.setText(Double.toString(0.0));
		    		Log.i("group1", ""+expenseParticipant.getAllottedAmount());
		    	}
		    }
		});
		
		
		return alertView;
		
	}
	
}
