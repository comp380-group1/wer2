package main;

import java.util.Collections;
import java.util.List;

public class Reconciler {
	
	private static void simpleReconcile(List<Participant> input, List<Payment> output) {
		
		Collections.sort(input);
		for (int i = 0; i < input.size() && input.get(i).getCurrentBalance() < 0; i++) {
			
			for (int j = input.size() - 1; j > i && input.get(j).getCurrentBalance() > 0; j--) {
				
				double result = input.get(i).getCurrentBalance() + input.get(j).getCurrentBalance();
				if (result == 0 && input.get(i).getCurrentBalance() != 0 && input.get(j).getCurrentBalance() != 0) {
					
					//output.add((new Payment(String.valueOf(input.get(j).getId()), String.valueOf(input.get(i).getId()), input.get(j).getCurrentBalance())));
					output.add((new Payment(input.get(j).getName(), input.get(i).getName(), input.get(j).getCurrentBalance(), input.get(j).getPhoneNumber())));
					input.get(i).setBalance(0);
					input.get(j).setBalance(0);
					break;
				}
			}
		}
		Collections.sort(input);
	}

	public static void reconcile(List<Participant> input, List<Payment> output) {
		
		simpleReconcile(input, output);
		for (int i = 0; i < input.size() && input.get(i).getCurrentBalance() < 0; i++) {
			
			for (int j = input.size() - 1; j > i && input.get(j).getCurrentBalance() > 0; j--) {
				
				double result = input.get(i).getCurrentBalance() + input.get(j).getCurrentBalance();
				if (result < 0 && input.get(i).getCurrentBalance() != 0 && input.get(j).getCurrentBalance() != 0) {
					
					//output.add(new Payment(String.valueOf(input.get(j).getId()), String.valueOf(input.get(i).getId()), input.get(j).getCurrentBalance()));
					output.add(new Payment(input.get(j).getName(), input.get(i).getName(), input.get(j).getCurrentBalance(), input.get(j).getPhoneNumber()));
					input.get(i).setBalance(result);
					input.get(j).setBalance(0);
					simpleReconcile(input, output);
					i = 0;
					j = input.size();
				}
				else if (result > 0 && input.get(i).getCurrentBalance() != 0 && input.get(j).getCurrentBalance() != 0) {
					
					//output.add(new Payment(String.valueOf(input.get(j).getId()), String.valueOf(input.get(i).getId()), Math.abs(input.get(i).getCurrentBalance())));
					output.add(new Payment(input.get(j).getName(), input.get(i).getName(), Math.abs(input.get(i).getCurrentBalance()), input.get(i).getPhoneNumber()));
					input.get(i).setBalance(0);
					input.get(j).setBalance(result);
					simpleReconcile(input, output);
					i = 0;
					j = input.size();
				}
			}
		}
	}
}