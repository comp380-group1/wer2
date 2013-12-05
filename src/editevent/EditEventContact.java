package editevent;

/**
 * Todo: COMPLETED
 * 
 * @author Matt Hamersky
 * @info Helper class for EditEventAdapterActivity.class.  Provides an easy way of passing data to the
 * adapter to display the phones contacts.
 *
 */

public class EditEventContact {

	private String name;
	private String number;
	private boolean isAlreadyInEvent;
	
	public EditEventContact(String name, String number, boolean isAlreadyInEvent) {
		
		this.name = name;
		this.number = number;
		this.isAlreadyInEvent = isAlreadyInEvent;
		
	}
	
	public String getName() {	return name;	}
	public String getPhoneNumber() {	return number;	}
	public boolean isAlreadyInEvent() {	return isAlreadyInEvent;	}
	
	public void setName(String name) {	this.name = name;	}
	public void setPhoneNumber(String number) {	this.number = number;	}
	public void isAlreadyInEvent(boolean status) {	this.isAlreadyInEvent = status;	}
	public void changeAlreadyInEventToOpposite() {
		if(isAlreadyInEvent)
			isAlreadyInEvent = false;
		else
			isAlreadyInEvent = true;
	}
	
	
}
