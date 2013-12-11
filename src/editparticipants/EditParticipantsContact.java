package editparticipants;

/**
 * Todo: COMPLETED
 * 
 * @author Matt Hamersky
 * @info Helper class for EditEventAdapterActivity.class.  Provides an easy way of passing data to the
 * adapter to display the phones contacts.
 *
 */

public class EditParticipantsContact {

	private String name;
	private String number;
	private boolean isAlreadyInEvent;
	private long id;
	
	public EditParticipantsContact(String name, String number, boolean isAlreadyInEvent, long id) {
		
		this.name = name;
		this.number = number;
		this.isAlreadyInEvent = isAlreadyInEvent;
		this.id = id;
		
	}
	
	public String getName() {	return name;	}
	public String getPhoneNumber() {	return number;	}
	public boolean isAlreadyInEvent() {	return isAlreadyInEvent;	}
	public long getId() {	return id;	}
	
	public void setName(String name) {	this.name = name;	}
	public void setPhoneNumber(String number) {	this.number = number;	}
	public void isAlreadyInEvent(boolean status) {	this.isAlreadyInEvent = status;	}
	public void setId(long id) {	this.id = id;	}
	public void changeAlreadyInEventToOpposite() {
		if(isAlreadyInEvent)
			isAlreadyInEvent = false;
		else
			isAlreadyInEvent = true;
	}
	
	
}
