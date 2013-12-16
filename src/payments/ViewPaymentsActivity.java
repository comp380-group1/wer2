package payments;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import main.DataManager;
import main.Event;
import main.Notifier;
import main.Payment;

import wer2.main.R;
import wer2.main.R.layout;
import wer2.main.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class ViewPaymentsActivity extends Activity {

	ListView paymentsListView;
	Button sendSMSButton;
	
	List<Payment> payments = null;
	ArrayAdapter<String> adapter = null;
	
	Event event = null;
	long id = -1;
	
	DataManager dm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.payments_main);
		
		dm = new DataManager(this.getApplicationContext());
		
		paymentsListView = (ListView)findViewById(R.id.paymentslist);
		sendSMSButton = (Button)findViewById(R.id.smsbutton);
		
		Intent intent = getIntent();
		id = intent.getLongExtra("event_id", -1);
		event = dm.getEvent(id);
		
		if(event.isNotified()) {
			sendSMSButton.setEnabled(false);
		}
		else {
			sendSMSButton.setEnabled(true);
		}
		
		try {
			payments = dm.getPaymentsByEventId(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		List<String> paymentsAsStrings = new ArrayList<String>();
		DecimalFormat f = new DecimalFormat("##.00");
		for(int i = 0; i < payments.size(); i++) {
			paymentsAsStrings.add(new String(payments.get(i).getTo() + " owes " + payments.get(i).getFrom() + " $" + f.format(payments.get(i).getAmount())));
		}
		
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, paymentsAsStrings);
		paymentsListView.setAdapter(adapter);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.view_payments, menu);
		return true;
	}
	
	public void sendSMS(View view) {
		event.setIsNotified(true);
		dm.saveEvent(event);
		
		Notifier.notifyParticipants(event, payments);
		
		Toast.makeText(this.getApplicationContext(), "Sending text(s)", 4).show();
		sendSMSButton.setEnabled(false);
	}

}
