package com.group1.wer;

import java.util.List;

import android.telephony.SmsManager;

public class Notifier {
	
	public static void notifyParticipants(Event event) {
		
		List<Payment> payments = event.getAllPayments();
		
		Participant payer;
		String payerName, payeeName;
		double amount;
		for(int i = 0; i < payments.size(); i++) {
			payerName = payments.get(i).getFrom();
			payeeName = payments.get(i).getTo();
			amount = payments.get(i).getAmount();
			
			payer = event.getParticipant(payerName);
			
			SmsManager sms = SmsManager.getDefault();
	        sms.sendTextMessage(payer.getPhoneNumber(), null, "You owe " + payeeName + " $" + amount + ".", null, null);
		}
		
	}

}
