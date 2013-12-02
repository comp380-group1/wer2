package main;

import java.util.List;
import java.util.ArrayList;

public class Event {
	
	private long id;
	private String name;
	private boolean isReconciled;
	private List<Expense> expenses = new ArrayList<Expense>();
	private List<Participant> participants = new ArrayList<Participant>();
	private List<Payment> payments;	

	public Event(long id, String eventName, boolean isReconciled) {
		this.id = id;
		this.name = eventName;
		this.isReconciled = isReconciled;
	}
	
	public Event(String eventName, boolean isReconciled) {
		this.name = eventName;
		this.isReconciled = isReconciled;
	}
	
	public Event(String eventName) {
		name = eventName;
		isReconciled = false;
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
		isReconciled = true;
		
	}
	
	public void setId(long id) {
		this.id = id;		
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setIsReconciled(boolean isReconciled) {
		this.isReconciled = isReconciled;
	}
	
	public long getId() {	return id;	}
	public String getName() { return name; }
	public boolean getIsReconciled() {	return isReconciled;	}
	public boolean isReconciled() {	return isReconciled;	}
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
	public String getParticipantsAsString() {
		String people = "";
		for(int i = 0; i < participants.size(); i++) {
			people += participants.get(i).getName() + ", ";
		}
		return people;
	}

	public String toString() {
		return "Event (id=" + getId() + ",name=" + getName() + ",isReconciled=" + getIsReconciled() + ")";
	}	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
