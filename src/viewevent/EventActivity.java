package viewevent;

import java.util.List;

import payments.ViewPaymentsActivity;
import reconcile.ReconciledActivity;

import editevent.EditEventAdapterActivity;
import editevent.EditEventContact;
import editexpense.EditExpenseActivity;
import main.DataManager;
import main.Event;
import main.Expense;
import viewevents.MainActivity;
import wer.main.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

/**
 * Todo:
 * -Basically everything...
 * 
 * @author Matt Hamersky
 * @info Displays information about a particular event including name, participants, and expenses.
 * The user is allowed to tap a participant/expense and another activity is launched displaying more
 * information about it.
 * Uses layouts: viewevent_main
 */

public class EventActivity extends Activity {

	ListView expensesList;
	Button addExpensesButton;
	Button viewPaymentsButton;
	TextView eventName;
	
	ViewEventAdapter adapter;
	
	Event event = null;
	List<Expense> listOfExpenses;
	long id = -1;
	
	DataManager dm;
	
	public static final int ADD_EXPENSE = 34238629;
	public static final int EDIT_EXPENSE = 342875212;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewevent_main);
		
		dm = new DataManager(this.getApplicationContext());
		
		expensesList = (ListView)findViewById(R.id.expenselist);
		addExpensesButton = (Button)findViewById(R.id.addexpense);
		viewPaymentsButton = (Button)findViewById(R.id.viewpayments);
		eventName = (TextView)findViewById(R.id.eventname);
		
		Intent intent = getIntent();
		id = intent.getLongExtra("event_id", -1);
		if(id == -1) {
			return;
		}
		event = dm.getEvent(id);
		
		if(!event.isReconciled()) { //disable/enable buttons depending on the status of the event
			viewPaymentsButton.setText("Reconcile Event");
			addExpensesButton.setEnabled(true);
		}
		else {
			viewPaymentsButton.setEnabled(true);
			addExpensesButton.setEnabled(false);
		}
		
		try {
			listOfExpenses = dm.getExpensesByEventId(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		eventName.setText(event.getName());
		
		adapter = new ViewEventAdapter(this, R.layout.viewevent_list_view_components, listOfExpenses);
		expensesList.setAdapter(adapter);
		registerForContextMenu(expensesList);
		
		expensesList.setOnItemClickListener(new OnItemClickListener() {
		    @Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		    	Expense tempExpense = (Expense) parent.getItemAtPosition(position);
		    	if(event.isReconciled()) {
	        		Toast.makeText(getApplicationContext(), "Reconciled events are not eligible to be edited",
	        				   4).show();
	        		return;
	        	}
		    	
		    	Intent intent = new Intent(EventActivity.this, EditExpenseActivity.class);
		    	intent.putExtra("expense_id", tempExpense.getId());
		        intent.putExtra("event_id", tempExpense.getEventId());
		        startActivityForResult(intent, EDIT_EXPENSE);
		    }

		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.event, menu);
		return true;
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
	    menu.setHeaderTitle(listOfExpenses.get(info.position).getName());
	    String[] menuItems = {"Edit", "Delete", "Cancel"};
	    for (int i = 0; i<menuItems.length; i++) {
	      menu.add(Menu.NONE, i, i, menuItems[i]);
	    }
	}
	
	@Override
    public boolean onContextItemSelected(MenuItem item){
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		Expense tempExpense = (Expense)expensesList.getAdapter().getItem(info.position);
		
        switch(item.getItemId()){
	        case 0:  //edit
	        	if(event.isReconciled()) {
	        		Toast.makeText(getApplicationContext(), "Reconciled events are not eligible to be edited",
	        				   4).show();
	        		break;
	        	}
	            Intent intent = new Intent(EventActivity.this, EditExpenseActivity.class);
	            intent.putExtra("expense_id", tempExpense.getId());
	            intent.putExtra("event_id", id);
	            startActivityForResult(intent, EDIT_EXPENSE);
	            break;
	        case 1: //delete
	        	event = dm.getEvent(id); //refresh the locally stored event
	        	event.removeExpenseById(tempExpense.getId());
	        	listOfExpenses.remove(tempExpense);
	        	dm.deleteExpense(tempExpense);
	        	refreshListView();
	        	break;
	        default: //cancel
	        	break;
        } 
        return true;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode) {
			case EDIT_EXPENSE:
				if (resultCode == Activity.RESULT_OK) {
					try {
						listOfExpenses = dm.getExpensesByEventId(id);
						adapter = new ViewEventAdapter(this, R.layout.viewevent_list_view_components, listOfExpenses);
						expensesList.setAdapter(adapter);
						refreshListView();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			    break;
			case ADD_EXPENSE:
				if(resultCode == Activity.RESULT_OK) {
					try {
						listOfExpenses = dm.getExpensesByEventId(id);
						adapter = new ViewEventAdapter(this, R.layout.viewevent_list_view_components, listOfExpenses);
						expensesList.setAdapter(adapter);
						refreshListView();
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
				
				break;
			default:
				break;
			    	
		}
	}
	
	public void addExpense(View view) {
		Intent intent = new Intent(EventActivity.this, EditExpenseActivity.class);
		intent.putExtra("event_id", id);
		startActivityForResult(intent, ADD_EXPENSE);
	}
	
	public void viewPayments(View view) {
		if(event.isReconciled()) {
			Intent intent = new Intent(EventActivity.this, ViewPaymentsActivity.class);
			intent.putExtra("event_id", id);
			startActivity(intent);
		}
		else {
			Intent intent = new Intent(EventActivity.this, ReconciledActivity.class);
			intent.putExtra("event_id", id);
			startActivity(intent);
		}
	}
	
	private void refreshListView() {
		runOnUiThread(new Runnable() {
	        @Override
	        public void run() {
	                adapter.notifyDataSetChanged();
	        }
	    });
		expensesList.invalidate();
		expensesList.invalidateViews();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		dm.close();
	}
	

}
