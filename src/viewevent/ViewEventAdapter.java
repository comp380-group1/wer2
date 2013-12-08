package viewevent;

import java.util.List;

import wer.main.R;
import main.DataManager;
import main.Event;
import main.Expense;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewEventAdapter extends ArrayAdapter<Expense> {

	
	private int resource;
	
	List<Expense> expensesList;
	
	public ViewEventAdapter(Context context, int resource, List<Expense> objects) {
		
		super(context, resource, objects);
		this.resource = resource;
		
		expensesList = objects;
	
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LinearLayout alertView;
		
		Expense expense = getItem(position);
		
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
		
		TextView expenseName = (TextView)alertView.findViewById(R.id.expensename);
		TextView expenseDate = (TextView)alertView.findViewById(R.id.expensedate);
		TextView expenseAmount = (TextView)alertView.findViewById(R.id.expenseamount);
		
		expenseName.setText(expense.getName());
		expenseDate.setText(DataManager.dateToString(expense.getDate()));
		expenseAmount.setText("$ " + Double.toString(expense.getAmount()));
		
		return alertView;
		
	}
	
	
}
