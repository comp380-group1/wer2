package editexpense;

import wer.main.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class EditExpenseActivity extends Activity {

	ListView expenseParticipantsListView;
	Button addExpenseButton;
	TextView expenseName;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editexpense_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.edit_expense, menu);
		return true;
	}

}
