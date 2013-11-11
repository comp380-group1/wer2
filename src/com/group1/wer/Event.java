package com.group1.wer;

import java.util.List;
import java.util.ArrayList;

public class Event {

	
	private String name;
	private List<Expense> expenses = new ArrayList<Expense>();
	private List<Participant> participants = new ArrayList<Participant>();
	private List<Payment> payments;
	
	public Event(String eventName) {
		
		name = eventName;
		
	}
	
	public void addParticipant(Participant newParticipant) { 
		
		participants.add(newParticipant); 
		for(int i = 0; i < expenses.size(); i++) {
			expenses.get(i).addNewParticipant(newParticipant);
		}
		
	}
	public void removeParticipant(Participant toBeRemoved) { 
		
		for(int i = 0; i < expenses.size(); i++) {
			expenses.get(i).removeParticipant(toBeRemoved); //check if we want this functionality
		}
		participants.remove(toBeRemoved);
		
	}
	
	public void addExpense(Expense newExpense) {
		
		expenses.add(newExpense);
		
	}
	
	public void reconcile() {
		
		payments = new ArrayList<Payment>();
		Reconciler.reconcile(this.getAllParticipants(), payments);
		
	}
	
	public String getEventName() { return name; }
	public List getAllPayments() { return payments;	}
	public List getAllParticipants() { return participants; }
	public List getAllExpenses() { return expenses; }
	public Participant getParticipant(int i) { return participants.get(i); }
	public Expense getExpense(int i) { return expenses.get(i); }
	public Participant getParticipant(String name) { 
		for(int i = 0; i < participants.size(); i++) {
			if(participants.get(i).getName().equals(name)) {
				return participants.get(i);
			}
		}
		return null;
	}
}
