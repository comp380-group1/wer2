package main;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Event {
	
	private long id = -1;
	private String name;
	private Date date = null;
	private boolean isReconciled;
	private List<Expense> expenses = new ArrayList<Expense>();
	private List<Participant> participants = new ArrayList<Participant>();
	private List<Payment> payments;	

	public Event(long id, String name, Date date, boolean isReconciled) {
		this.id = id;
		this.name = name;
		this.date = date;
		this.isReconciled = isReconciled;
	}
	
	public Event(String name, Date date, boolean isReconciled) {
		this.name = name;
		this.date = date;
		this.isReconciled = isReconciled;
	}
	
	public Event(String name, boolean isReconciled) {
		this.name = name;
		this.isReconciled = isReconciled;
	}
	
	public Event(String name) {
		this.name = name;
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
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public void setIsReconciled(boolean isReconciled) {
		this.isReconciled = isReconciled;
	}
	
	public long getId() {	return id;	}
	public String getName() { return name; }
	public Date getDate() { return date; }
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
		return "Event (id=" + getId() + ",name=" + getName() + ",date=" + getDate() + ",isReconciled=" + getIsReconciled() + ")";
	}	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
