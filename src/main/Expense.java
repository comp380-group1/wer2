package main;

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

	private List<ExpenseParticipant> participants = new ArrayList<ExpenseParticipant>();
	private long eventId;
	private Event event;
	private String name;
	private double amount;
	
	public Expense(Event event, String name) {
		
		this.name = name;
		this.amount = 0.0;
		
		for(int i = 0; i < event.getAllParticipants().size(); i++) {
			participants.add(new ExpenseParticipant(event.getParticipant(i), 0.0, true));
		}
	}
	
	public void addNewParticipant(Participant newParticipant) {
		
		participants.add(new ExpenseParticipant(newParticipant, 0.0, false));
		
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
		
		double previousBalanceAtIndex = participants.get(index).getAmount();
		double newAmount = amount - previousBalanceAtIndex;
		
		subtractPreviousBalance();
		
		participants.get(index).setAmount(-amount); //negative = owed this amount of money
		participants.get(index).setParticipating(true); //if the person tries to chip in money, automatically add them to be participating (really only for the times when a person defaults to not being in and tries to pay)
		participants.get(index).setAllottedAmount(-amount);
	}
	
	public void onClose() {
		//add up total amount that each participant paid
		double tempAmount = 0.0;
		for(int i = 0; i < participants.size(); i++) {
			tempAmount += Math.abs(participants.get(i).getAmount());
		}
		amount = tempAmount;
		
		int in = numberInOrOut(); //number of people participating in this expense
		if(in == 0) //if no one is participating, quit without doing anything
			return;
		int isEvenSplit = (int) ((100 * amount) % in); //number of remaining cents left over after dividing the cost among the participants
		double evenDistributionAmount = (amount - (isEvenSplit / 100.0)) / in; //amount that all participants need to evenly pay (subtracting the odd number of cents)
		
		if(isEvenSplit != 0) { //only if there are additional cents to be given to someone
			List<Boolean> penniesLeftOver = new ArrayList<Boolean>(participants.size()); 
			penniesLeftOver = copyBooleans(penniesLeftOver); //copy the list of booleans determining if people are taking part in this expense
			Random random = new Random();
			while(isEvenSplit > 0) {
				//int number = 0;
				int number = random.nextInt(participants.size());
				if(penniesLeftOver.get(number)) {
					//amountPaid.set(number, amountPaid.get(number) + .01);
					participants.get(number).getParticipant().updateBalance(.01);
					participants.get(number).updateAllottedAmount(.01);
					penniesLeftOver.set(number, true);
					isEvenSplit--;
				}
			}
		}
		for(int i = 0; i < participants.size(); i++) {
			if(participants.get(i).isParticipating()) {
				participants.get(i).getParticipant().updateBalance(participants.get(i).getAmount() + evenDistributionAmount);
				participants.get(i).updateAllottedAmount(evenDistributionAmount);
			}
		}
	}
	
	private int numberInOrOut() {
		int in = 0;
		for(int i = 0; i < participants.size(); i++) {
			if(participants.get(i).isParticipating()) {
				in++;
			}
		}
		return in;
	}
	
	private void subtractPreviousBalance() {
		
		for(int i = 0; i < participants.size(); i++) {
			
			double amount = participants.get(i).getAllottedAmount();
			participants.get(i).getParticipant().updateBalance(-amount);
			participants.get(i).setAllottedAmount(0.0); //fresh slate, ready for the new amount
			
		}
		
	}
	
	
	//////////////////////////////////////////////////////////////////////////////
	
	
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
	public double getAmountPaid(int i) { return participants.get(i).getAmount(); }
	public List getAllAmountPaid() { 
		List<Double> temp = new ArrayList<Double>();
		for(int i = 0; i < participants.size(); i++) {
			temp.add(participants.get(i).getAmount());
		}
		return temp;
	}
	public boolean getInOrOut(int i) { return participants.get(i).isParticipating(); }
	public List getAllInOrOut() { 
		
		List<Boolean> temp = new ArrayList<Boolean>();
		for(int i = 0; i < participants.size(); i++) {
			temp.add(participants.get(i).isParticipating());
		}
		return temp;
	}
	
	
	/////////////////////////////////////////////////////////////////////////////
	
	private List copyBooleans(List<Boolean> temp) {
		for(int i = 0; i < participants.size(); i++) {
			temp.add(participants.get(i).isParticipating());
		}
		return temp;
	}
	
}
