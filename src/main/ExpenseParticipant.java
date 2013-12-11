package main;

import main.Participant;

public class ExpenseParticipant {

	private long id = -1;
	private long eventId;
    private long expenseId;
	private long participantId;
	private double paid;
	private double allottedAmount; //used to keep track of how much this participant is paying/needs to be paid (after the per expense split)
	private boolean participating;
	
	private Participant participant;	
	
	public ExpenseParticipant(long id, long eventId, long expenseId, long participantId, double paid, double allottedAmount, boolean participating) {
		this.id = id;
		this.eventId = eventId;
		this.expenseId = expenseId;
		this.participantId = participantId;
		this.paid = paid;
		this.allottedAmount = allottedAmount;
		this.participating = participating;
	}
	
	public ExpenseParticipant(long eventId, long expenseId, long participantId, double paid, double allottedAmount, boolean participating) {
		this.eventId = eventId;
		this.expenseId = expenseId;
		this.participantId = participantId;
		this.paid = paid;
		this.allottedAmount = allottedAmount;
		this.participating = participating;
	}
	
	public void setId(long id) {
		this.id = id;		
	}
	
	public void setEventId(long eventId) {
		this.eventId = eventId;		
	}
	
	public void setParticipantId(long participantId) {
		this.participantId = participantId;		
	}
	
	public void setParticipant(Participant participant) {	this.participant = participant;	}
	public void setAmount(double amount) { paid = amount;	}
	public void updateAmount(double amount) { paid += amount;	}
	public void setAllottedAmount(double amount) {	allottedAmount = amount;	}
	public void updateAllottedAmount(double amount) {	allottedAmount += amount;	}
	public void setParticipating(boolean isParticipating) {	participating = isParticipating;	}
	public long getExpenseId() {
		return expenseId;
	}

	public void setExpenseId(long expenseId) {
		this.expenseId = expenseId;
	}
	public long getId() { return id; }
	public long getEventId() { return eventId; }
	public long getParticipantId() { return participantId; }
	public Participant getParticipant() { return participant;	}
	public double getAmount() {	return paid; }
	public double getPaid() { return paid; }
	public double getAllottedAmount() {	return allottedAmount;	}
	public boolean isParticipating() { return participating; }
	public boolean getParticipating() {	return participating;	}
	
	public String toString() {
		return "ExpenseParticipant (id=" + getId() + ",eventId=" + getEventId() + ",expenseId=" + getExpenseId() + ",participantId=" + getParticipantId() + ",paid=" + getPaid() + ",allottedAmount=" + getAllottedAmount() + ",participating=" + isParticipating() +")";
	}
	
}
