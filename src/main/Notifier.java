package main;

import java.util.List;

import android.telephony.SmsManager;

public class Notifier {
	
	public static void notifyParticipants(List<Payment> payments) {
		
		Participant payer;
		String payerName, payeeName;
		double amount;
		for(int i = 0; i < payments.size(); i++) {
			payerName = payments.get(i).getFrom();
			payeeName = payments.get(i).getTo();
			amount = payments.get(i).getAmount();
			
			//payer = event.getParticipant(payerName);
			
			SmsManager sms = SmsManager.getDefault();
	        //sms.sendTextMessage(payer.getPhoneNumber(), null, "You owe " + payeeName + " $" + amount + ".", null, null);
	        //might have to and from switched, have to check on that
		}
		
	}

}
