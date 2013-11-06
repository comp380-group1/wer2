
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Expense {

	private List<Participant> participants = new ArrayList<Participant>();
	private List<Double> amountPaid;
	private List<Boolean> inOrOut;
	private String name;
	private double amount;
	
	public Expense(Event event, String name) {
		
		this.name = name;
		this.amount = 0.0;
		
		for(int i = 0; i < event.getAllParticipants().size(); i++) {
			participants.add(event.getParticipant(i));
		}
		amountPaid = new ArrayList<Double>();
		inOrOut = new ArrayList<Boolean>();
		initializeLists(); //initialize all to $0.0, initialize everyone to pitching in
	}
	
	private void initializeLists() {
		for(int i = 0; i < participants.size(); i++) {
			amountPaid.add(0.0);
			inOrOut.add(true);
		}
	}
	
	public void addNewParticipant(Participant newParticipant) {
		participants.add(newParticipant);
		
		//update amountPaid array
		amountPaid.add(0.0);
		
		//update inOrOut array
		inOrOut.add(false);
		
	}
	
	/* Need to implement this method.  Needs to check and see who the participant owes */
	public void removeParticipant(Participant toBeRemoved) {
		
	}
	
	/**
	 * Updates the amount that the participant is paying.
	 * @param index Index of participant being updated
	 * @param amount New balance for this participant
	 */
	public void setAmountPaid(int index, double amount) {
		double previousBalanceAtIndex = amountPaid.get(index);
		double newAmount = amount - previousBalanceAtIndex;
		amountPaid.set(index, -amount); //negative = owed this amount of money
		inOrOut.set(index, true); //if the person tries to chip in money, automatically add them to be participating (really only for the times when a person defaults to not being in and tries to pay)
		//participants.get(index).updateBalance(-newAmount);
	}
	
	public void onClose() {
		//add up total amount that each participant paid
		double tempAmount = 0.0;
		for(int i = 0; i < amountPaid.size(); i++) {
			tempAmount += Math.abs(amountPaid.get(i));
		}
		amount = tempAmount;
		
		int in = numberInOrOut(); //number of people participating in this expense
		if(in == 0) //if no one is participating, quit without doing anything
			return;
		int isEvenSplit = (int) ((100 * amount) % in); //number of remaining cents left over after dividing the cost among the participants
		double evenDistributionAmount = (amount - (isEvenSplit / 100.0)) / in; //amount that all participants need to evenly pay (subtracting the odd number of cents)
		
		if(isEvenSplit != 0) { //only if there are additional cents to be given to someone
			List<Boolean> penniesLeftOver = new ArrayList<Boolean>(participants.size()); 
			penniesLeftOver.addAll(inOrOut); //copy the list of booleans determining if people are taking part in this expense
			Random random = new Random();
			while(isEvenSplit > 0) {
				//int number = 0;
				int number = random.nextInt(participants.size());
				if(penniesLeftOver.get(number)) {
					//amountPaid.set(number, amountPaid.get(number) + .01);
					participants.get(number).updateBalance(.01);
					penniesLeftOver.set(number, true);
					isEvenSplit--;
				}
			}
		}
		
		for(int i = 0; i < participants.size(); i++) {
			if(inOrOut.get(i)) {
				participants.get(i).updateBalance(amountPaid.get(i) + evenDistributionAmount);
			}
		}
	}
	
	private int numberInOrOut() {
		int in = 0;
		for(int i = 0; i < inOrOut.size(); i++) {
			if(inOrOut.get(i)) {
				in++;
			}
		}
		return in;
	}
	
	public String getName() { return name; }
	public double getAmount() { return amount; }
	public List getAllParticipants() { return participants; }
	public Participant getParticipant(int i) { return participants.get(i); }
	public List getAllAmountPaid() { return amountPaid; }
	public double getAmountPaid(int i) { return amountPaid.get(i); }
	public List getAllInOrOut() { return inOrOut; }
	public boolean getInOrOut(int i) { return inOrOut.get(i); }
	
}
