package editexpense;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import main.DataManager;
import main.Event;
import main.Expense;
import main.ExpenseParticipant;
import main.Participant;

import wer2.main.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class EditExpenseActivity extends Activity {

	ListView expenseParticipantsListView;
	Button addExpenseButton;
	EditText expenseName;
	TextView expenseTotal;
	
	EditExpenseAdapter adapter;
	
	List<ExpenseParticipant> expenseParticipantsList;
	List<Participant> eventParticipantsList;
	Event event = null;
	Expense expense = null;
	long expense_id = -1;
	long event_id = -1;
	
	boolean isFinished = false;
	
	DataManager dm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editexpense_main);
		
		dm = new DataManager(this.getApplicationContext());
		
		expenseParticipantsListView = (ListView)findViewById(R.id.expenseparticipantslist);
		addExpenseButton = (Button)findViewById(R.id.finish);
		expenseName = (EditText)findViewById(R.id.expensename);
		expenseTotal = (TextView)findViewById(R.id.expensetotalnumber);
		
		Intent intent = getIntent();
		expense_id = intent.getLongExtra("expense_id", -1);
		event_id = intent.getLongExtra("event_id", -1);
		event = dm.getEvent(event_id);
		try {
			eventParticipantsList = dm.getParticipantsByEventId(event_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(expense_id == -1) { //this is a new expense
			addExpenseButton.setText("Add Expense");
			expense = new Expense(event_id, "", new Date(), 0.0);
			expense_id = dm.saveExpense(expense);
			expenseTotal.setText("$ 0.00");
			turnParticipantsIntoExpenseparticipants();
		}
		else {
			isFinished = true;
			addExpenseButton.setText("Finish");
			expense = dm.getExpense(expense_id);
			try {
				expenseParticipantsList = dm.getExpenseParticipantsByExpenseId(expense_id);
			} catch (Exception e) {
				e.printStackTrace();
			}
			expenseName.setText(expense.getName());
			DecimalFormat f = new DecimalFormat("##.00");
			expenseTotal.setText("$ " + f.format(expense.getAmount()));
		}
		
		setExpenseParticipantsParticipant();
		
		adapter = new EditExpenseAdapter(EditExpenseActivity.this, R.layout.editexpense_list_view_components, expenseParticipantsList);
		expenseParticipantsListView.setAdapter(adapter);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.edit_expense, menu);
		return true;
	}
	
	private void turnParticipantsIntoExpenseparticipants() {
		expenseParticipantsList = new ArrayList<ExpenseParticipant>();
		for(int i = 0; i < eventParticipantsList.size(); i++) {
			ExpenseParticipant expensePerson = new ExpenseParticipant(event_id, expense_id, eventParticipantsList.get(i).getId(), 0.0, 0.0, true);
			dm.saveExpenseParticipant(expensePerson);
			expenseParticipantsList.add(expensePerson);
		}
	}
	
	private void setExpenseParticipantsParticipant() {
		for(int i = 0; i < expenseParticipantsList.size(); i++) {
			expenseParticipantsList.get(i).setParticipant(dm.getParticipant(expenseParticipantsList.get(i).getParticipantId()));
		}
	}
	
	public void finished(View view) {
		expense.setName(expenseName.getText().toString());

	    for (int i = 0; i < expenseParticipantsList.size(); i++) {
	        dm.saveExpenseParticipant(expenseParticipantsList.get(i));
	    }
	    event = dm.getEvent(event_id); //refresh event
	    onExit();
	    dm.saveExpense(expense);
	    dm.saveEvent(event); //save EVERYTHING!!!
	    
	    Intent resultIntent = new Intent();
	    resultIntent.putExtra("expense_id", expense_id);
		setResult(Activity.RESULT_OK, resultIntent);
		isFinished = true;
		finish();
	}
	
	private void onExit() {
		//add up total amount that each participant paid
		List<ExpenseParticipant> tempExpenseParticipants = new ArrayList<ExpenseParticipant>();
		double tempAmount = 0.0;
		for(int i = 0; i < expenseParticipantsList.size(); i++) {
			ExpenseParticipant ep = expenseParticipantsList.get(i);
			tempAmount += Math.abs(ep.getAmount());
			ep.getParticipant().updateBalance(-ep.getAllottedAmount());
			ep.setAllottedAmount(0.0);
			if(ep.getPaid() > 0) {
				ep.setAllottedAmount(-ep.getPaid());
				ep.getParticipant().updateBalance(-ep.getPaid());
			}
			if(ep.isParticipating()) {
				tempExpenseParticipants.add(ep);
			}
			else {
				dm.saveExpenseParticipant(ep);
				dm.saveParticipant(ep.getParticipant());
			}
		}
		expense.setAmount(tempAmount);	
		
		
		int in = tempExpenseParticipants.size(); //number of people participating in this expense
		if(in == 0) //if no one is participating, quit without doing anything
			return;
		int isEvenSplit = (int) ((100 * expense.getAmount()) % in); //number of remaining cents left over after dividing the cost among the participants
		double evenDistributionAmount = (expense.getAmount() - (isEvenSplit / 100.0)) / in; //amount that all participants need to evenly pay (subtracting the odd number of cents)
		
		if(isEvenSplit != 0) { //only if there are additional cents to be given to someone
			List<Boolean> penniesLeftOver = copyBooleans(in);
			Random random = new Random();
			while(isEvenSplit > 0) {
				int number = random.nextInt(in);
				if(penniesLeftOver.get(number)) {
					tempExpenseParticipants.get(number).getParticipant().updateBalance(.01);
					tempExpenseParticipants.get(number).updateAllottedAmount(.01);
					penniesLeftOver.set(number, false);
					isEvenSplit--;
				}
			}
		}
		for(int i = 0; i < tempExpenseParticipants.size(); i++) {
			//participants.get(i).getParticipant().updateBalance(participants.get(i).getAmount() + evenDistributionAmount);
			tempExpenseParticipants.get(i).getParticipant().updateBalance(evenDistributionAmount);
			tempExpenseParticipants.get(i).updateAllottedAmount(evenDistributionAmount);
			dm.saveExpenseParticipant(tempExpenseParticipants.get(i));
			dm.saveParticipant(tempExpenseParticipants.get(i).getParticipant());
		}
	}
	
	private List<Boolean> copyBooleans(int num) {
		List<Boolean> temp = new ArrayList<Boolean>();
		for(int i = 0; i < num; i++) {
			temp.add(true);
		}
		return temp;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(!isFinished) {
			dm.deleteExpense(expense);
		}
		dm.close();
	}

}
