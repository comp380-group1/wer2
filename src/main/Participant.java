package main;

public class Participant implements Comparable<Participant> {
	
	private long id = -1;
	private long eventId = -1;
	private String name;
	private String phoneNumber;
	private double currentBalance;
	

	public Participant(long id, long eventId, String name, String phoneNumber, double currentBalance) {
		this.id = id;
		this.eventId = eventId;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.currentBalance = currentBalance;
	}
	
	public Participant(String name, long eventId, String phoneNumber, double currentBalance) {
		this.id = -1;
		this.eventId = eventId;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.currentBalance = currentBalance;
	}
	
	public Participant(String name) {
		this.name = name;
		currentBalance = 0.0;
	}
	
	public void setId(long id) {
		this.id = id;		
	}
	
	public void setEventId(long eventId) {
		this.eventId = eventId;		
	}
	
	public void addPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public void setBalance(double newBalance) {
		currentBalance = newBalance;
	}
	
	public void updateBalance(double newBalance) {
		currentBalance += newBalance;
	}
	
	public int compareTo(Participant participant) {
		if (this.currentBalance < participant.getCurrentBalance()) return -1;
		if (this.currentBalance > participant.getCurrentBalance()) return 1;
		return 0;
	}
	
	public String getName() { return name; }
	public long getId() { return id; }
	public long getEventId() { return eventId; }
	public String getPhoneNumber() { return phoneNumber; }
	public double getCurrentBalance() { return currentBalance; }

	
	public String toString() {
		return "Participant (id=" + getId() + ",eventId=" + getEventId() + ",name=" + getName() + ",phoneNumber=" + getPhoneNumber() + ",currentBalance=" + getCurrentBalance() + ")";
	}	
}
