package main;

public class Payment implements Comparable<Payment> {

	private long id = -1;
	private long eventId;
	private String to;
	private String from;
	private double amount;
	
	public Payment(long id, long eventId, String to, String from, double amount) {
		this.id = id;
		this.eventId = eventId;
		this.to = to;
		this.from = from;
		this.amount = amount;
	}
	
	public Payment(long eventId, String to, String from, double amount) {
		this.eventId = eventId;
		this.to = to;
		this.from = from;
		this.amount = amount;
	}
	
	public Payment(String to, String from, double amount) {
		this.to = to;
		this.from = from;
		this.amount = amount;
	}
	
	public void setId(long id) {
		this.id = id;		
	}
	
	public void setEventId(long eventId) {
		this.eventId = eventId;		
	}
	
	@Override
	public int compareTo(Payment payment) {
		return this.from.compareTo(payment.getFrom());
	}

	public long getId() { return id; }
	public long getEventId() { return eventId; }
	public String getTo() { return to; }
	public String getFrom() { return from; }
	public double getAmount() { return amount; }
	
	public String toString() {
		return "Payment (id=" + getId() + ",eventId=" + getEventId() + ",to=" + getTo() + ",from=" + getFrom() + ",amount=" + getAmount() + ")";
	}
	
}
