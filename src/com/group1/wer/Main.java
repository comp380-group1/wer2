package com.group1.wer;

import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

public class Main {

	//negative = owed money
	//positive = owe money
	
	private static Scanner scan;
	private static ArrayList<Event> events = new ArrayList<Event>();
	
	public static void main(String[] args) {
		
		System.out.println("WeR^2");
		
		options();
		
	}
	
	private static void options() {
		scan = new Scanner(System.in);
		int input = 1;
		while(input > 0 && input < 4) {
			System.out.println("Please select an option");
			System.out.println("1) Create event");
			System.out.println("2) View event");
			System.out.println("3) Exit");
			
			input = Integer.parseInt(scan.nextLine());
			
			switch(input) {
				case 1:
					createEvent();
					break;
				case 2:
					viewEvent();
					break;
				case 3:
					System.exit(0);
				default:
					break;
			}
		}
	}
	
	private static void createEvent() {
		System.out.print("Enter an event name: ");
		String newEventName = scan.nextLine();
		events.add(new Event(newEventName));
		System.out.println("Event successfully created");
	}
	
	private static void viewEvent() {
		for(int i = 0; i < events.size(); i++) {
			System.out.println((i+1) +") " + events.get(i).getEventName());
		}
		System.out.println(events.size() + 1 + ") Return");
		int input = Integer.parseInt(scan.nextLine());
		
		if((input - 1) == events.size()) {
			return;
		}
		
		eventOptions(input - 1);
	}
	
	private static void eventOptions(int input) {

		System.out.println("1) Add Participants");
		System.out.println("2) View Participants");
		System.out.println("3) Add Expenses");
		System.out.println("4) View Expenses");
		System.out.println("5) Reconcile");
		System.out.println("6) Return");
		
		int in = Integer.parseInt(scan.nextLine());
		switch(in) {
			case 1:
				addParticipants(input);
				break;
			case 2:
				viewParticipants(input);
				break;
			case 3:
				addExpenses(input);
				break;
			case 4:
				viewExpenses(input);
				break;
			case 5:
				reconcile(input);
				break;
			case 6:
				return;
			default:
				break;
		}
	}
	
	private static void addParticipants(int input) {
		System.out.print("Add participants by typing their names (type \"null\" to end): ");
		String participantsName;
		while(!(participantsName = scan.nextLine()).equals("null")) {
			events.get(input).addParticipant(new Participant(participantsName));
		}
	}
	
	private static void viewParticipants(int input) {
		List<Participant> temp = events.get(input).getAllParticipants();
		for(int i = 0; i < temp.size(); i++) {
			System.out.println((i+1) + ") " + temp.get(i).getName());
		}
		System.out.println(temp.size() + 1 + ") Return");
		
		int in = Integer.parseInt(scan.nextLine());
		if(in - 1 == temp.size()) {
			return;
		}
		viewParticipantStatus(temp.get(in - 1));
	}
	
	private static void addExpenses(int input) {
		System.out.print("Add expenses by typing the name (type \"null\" to end): ");
		String name;
		while(!(name = scan.nextLine()).equals("null")) {
			events.get(input).addExpense(new Expense(events.get(input), name));
		}
	}
	
	private static void viewExpenses(int input) {
		List<Expense> temp = events.get(input).getAllExpenses();
		for(int i = 0; i < temp.size(); i++) {
			System.out.println((i+1) + ") " + temp.get(i).getName() + " - $" + temp.get(i).getAmount());
		}
		System.out.println(temp.size() + 1 + ") Return");
		
		int in = Integer.parseInt(scan.nextLine());
		if(in - 1 == temp.size()) {
			return;
		}
		viewExpenseStatus(temp.get(in - 1));
	}
	
	private static void reconcile(int input) {
		List<Payment> payments = new ArrayList<Payment>();
		Reconciler.reconcile(events.get(input).getAllParticipants(), payments);
		for(Payment payment : payments) {
			System.out.println("To: " + payment.getTo() + " From: " + payment.getFrom() + " Amount: " + payment.getAmount());
		}
		
		
		
	}
	
	private static void viewParticipantStatus(Participant participant) {
		System.out.println(participant.getName());
		System.out.println("Balance: $" + participant.getCurrentBalance());
		System.out.println("1) Return");
		int in = Integer.parseInt(scan.nextLine());
		return;
	}
	
	private static void viewExpenseStatus(Expense expense) {
		List<Participant> temp1 = expense.getAllParticipants();
		List<Double> temp2 = expense.getAllAmountPaid();
		List<Boolean> temp3 = expense.getAllInOrOut();
		
		System.out.println(expense.getName() + ": $" + expense.getAmount());
		for(int i = 0; i < temp1.size(); i++) {
			System.out.println((i+1) + ") " + temp1.get(i).getName() + " - " + -temp2.get(i) + " - " + temp3.get(i));
		}
		System.out.println(temp1.size() + 1 + ") Return");
		int in = Integer.parseInt(scan.nextLine());
		if(in - 1 == temp1.size()) {
			return;
		}
		setExpensePerson(expense, in - 1);
	}
	
	private static void setExpensePerson(Expense expense, int personIndex) {
		
		System.out.println(expense.getName() + ": $ " + expense.getAmount());
		System.out.print("How much does " + expense.getParticipant(personIndex).getName() + " pay: ");
		double amount = Double.parseDouble(scan.nextLine());
		expense.setAmountPaid(personIndex, amount);
		expense.onClose();
		
	}
}
