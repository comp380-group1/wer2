package main;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Event {
	
	private long id = -1;
	private String name;
	private Date date = null;
	private boolean isReconciled;
	private boolean isNotified;
	private List<Expense> expenses = new ArrayList<Expense>();
	private List<Participant> participants = new ArrayList<Participant>();
	private List<Payment> payments;	

	public Event(long id, String name, Date date, boolean isReconciled, boolean isNotified) {
		this.id = id;
		this.name = name;
		this.date = date;
		this.isReconciled = isReconciled;
		this.isNotified = isNotified;
	}
	
	public Event(String name, Date date, boolean isReconciled, boolean isNotified) {
		this.name = name;
		this.date = date;
		this.isReconciled = isReconciled;
		this.isNotified = isNotified;
	}
	
	public void addParticipant(Participant newParticipant) { 
		
		participants.add(newParticipant); 
		for(int i = 0; i < expenses.size(); i++) {
			expenses.get(i).addNewParticipant(newParticipant);
		}
		
	}
	
	public void addExpense(Expense newExpense) {
		newExpense.setEventId(this.id);
		expenses.add(newExpense);
	}
	
	public void reconcile() {
		payments = new ArrayList<Payment>();
		Reconciler.reconcile(this.getAllParticipants(), payments);
		isReconciled = true;
		
	}
	
	
	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////
	
	public List<Expense> getExpenses() {
		return expenses;
	}

	public void setExpenses(List<Expense> expenses) {
		this.expenses = expenses;
	}

	public void setParticipants(List<Participant> people) {
		participants = people;
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
	//public boolean getIsReconciled() {	return isReconciled;	}
	public boolean isReconciled() {	return isReconciled; }
	public boolean isNotified() { return isNotified; }
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
	
	public void removeExpenseById(long id) {
		for(int i = 0; i < expenses.size(); i++) {
			if(expenses.get(i).getId() == id) {
				expenses.remove(i);
				return;
			}
		}
	}
	
	public void removeParticipantById(long id) {
		for(int i = 0; i < participants.size(); i++) {
			if(participants.get(i).getId() == id) {
				participants.remove(i);
				return;
			}
		}
	}
	public String getParticipantsAsString() {
		String people = "";
		for(int i = 0; i < participants.size(); i++) {
			if((i + 1) == participants.size()) {
				people += participants.get(i).getName();
				break;
			}
			people += participants.get(i).getName() + ", ";
		}
		return people;
	}
	
	public Participant findParticipant(String name, String number) {
		for(int i = 0; i < participants.size(); i++) {
			if(participants.get(i).getName().equals(name) &&
					participants.get(i).getPhoneNumber().equals(number)) {
				return participants.get(i);
			}
		}
		return null;
	}

	public String toString() {
		return "Event (id=" + getId() + ",name=" + getName() + ",date=" + getDate() + ",isReconciled=" + isReconciled() + ")";
	}	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
