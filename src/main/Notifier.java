package main;

import java.util.List;

import android.telephony.SmsManager;
import android.widget.Toast;

public class Notifier {
	
	public static void notifyParticipants(Event event, List<Payment> payments) {

		String payerName, payeeName;
		double amount;
		for(int i = 0; i < payments.size(); i++) {
			payerName = payments.get(i).getFrom();
			payeeName = payments.get(i).getTo();
			amount = payments.get(i).getAmount();
			
			try {
				SmsManager sms = SmsManager.getDefault();
				sms.sendTextMessage(payments.get(i).getToPhoneNumber(), null, "You owe " + payerName + " $" + amount + ", for " + event.getName() + ".", null, null);
			} catch(Exception e) {
				e.printStackTrace();
			}
			
	        //might have to and from switched, have to check on that
		}
		
	}

}
