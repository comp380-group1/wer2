package main;

public class Payment implements Comparable<Payment> {

	//private Participant to;
	//private long to;
	private String to;
	//private Participant from;
	//private long from;
	private String from;
	private double amount;
	
	/*public Payment(Participant to, Participant from, double amount) {
		
		this.to = to;
		this.from = from;
		this.amount = amount;
		
	}*/
	
	/*public Payment(long to, long from, double amount) {
		
		this.to = to;
		this.from = from;
		this.amount = amount;
		
	}*/
	
	public Payment(String to, String from, double amount) {
		
		this.to = to;
		this.from = from;
		this.amount = amount;
		
	}
	
	public int compareTo(Payment payment)
	{
		return this.from.compareTo(payment.getFrom());
	}
	
	//public Participant getTo() { return to; }
	//public long getTo() { return to; }
	public String getTo() { return to; }
	//public Participant getFrom() { return from; }
	//public long getFrom() { return from; }
	public String getFrom() { return from; }
	public double getAmount() { return amount; }
	
}
