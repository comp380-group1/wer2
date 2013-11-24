package com.group1.wer;

import com.group1.wer.Participant;

public class ExpenseParticipant {

	private Participant participant;
	private double paid;
	private double allottedAmount; //used to keep track of how much this participant is paying/needs to be paid (after the per expense split)
	private boolean participating;
	
	public ExpenseParticipant(Participant participant, double paid, boolean participating) {
		
		this.participant = participant;
		this.paid = paid;
		this.allottedAmount = 0.0;
		this.participating = participating;
		
	}
	
	public void setAmount(double amount) { paid = amount;	}
	public void updateAmount(double amount) { paid += amount;	}
	public void setAllottedAmount(double amount) {	allottedAmount = amount;	}
	public void updateAllottedAmount(double amount) {	allottedAmount += amount;	}
	public void setParticipating(boolean isParticipating) {	participating = isParticipating;	}
	
	public Participant getParticipant() { return participant;	}
	public double getAmount() {	return paid;	}
	public double getAllottedAmount() {	return allottedAmount;	}
	public boolean isParticipating() {	return participating;	}
	
	
}
