package main;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import main.Event;
import main.Participant;



/* TODO: If the expense payment is changed, the cost is added to the participants total cost instead of recalculating the difference.
 * i.e. if the original bill was for $10 and it is changed to $6, the $6 is added to all the participants running balance instead
 * of first getting rid of the old amount ($10) and starting fresh with the $6.  This involves figuring out the split again so we
 * know how much to subtract from everyone's amount.
 */
public class Expense {

	private long id = -1;
	private long eventId;
	private String name;
	private Date date;
	private double amount;
	
	private Event event;
	private List<ExpenseParticipant> participants = new ArrayList<ExpenseParticipant>();

	public Expense(long id, long eventId, String name, Date date, double amount) {
		this.setId(id);
		this.setEventId(eventId);
		this.name = name;
		this.setDate(date);
		this.amount = amount;
	}
	
	public Expense(long eventId, String name, Date date, double amount) {
		this.setEventId(eventId);
		this.name = name;
		this.setDate(date);
		this.amount = amount;
	}
	
	public List<ExpenseParticipant> getParticipants() {
		return participants;
	}

	public void setParticipants(List<ExpenseParticipant> participants) {
		this.participants = participants;
	}
	
	public boolean isParticipantParticipating(long id) {
		for(int i = 0; i < participants.size(); i++) {
			if(id == participants.get(i).getParticipantId() && participants.get(i).isParticipating()) {
				return true;
			}
		}
		return false;
	}
	
	public ExpenseParticipant returnAndRemoveParticipantById(long id) {
		ExpenseParticipant temp = null;
		for(int i = 0; i < participants.size(); i++) {
			if(participants.get(i).getId() == id) {
				temp = participants.get(i);
				participants.remove(i);
			}
		}
		return temp;
	}
	
	
	//////////////////////////////////////////////////////////////////////////////
	
	
	
	public void setAmount(double amount) {	this.amount = amount;	}
	public void setName(String name) {	this.name = name;	}
	public String getName() { return name; }
	public double getAmount() { return amount; }
	public Participant getParticipant(int i) { return participants.get(i).getParticipant(); }
	public List getAllParticipants() { 
		
		List<Participant> temp = new ArrayList<Participant>();
		for(int i = 0; i < participants.size(); i++) {
			temp.add(participants.get(i).getParticipant());
		}
		
		return temp; 
	}	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getEventId() {
		return eventId;
	}

	public void setEventId(long eventId) {
		this.eventId = eventId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public String toString() {
		return "Expense (id=" + getId() + ",eventId=" + getEventId() + ",name=" + getName() + ",date=" + getDate() + ",amount=" + getAmount() + ")";
	}
	
}
