package main;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;
import android.util.Log;

public class DataManager extends SQLiteOpenHelper {
	private final static String DATABASE_NAME = "dbfile";
	private final static int DATABASE_VERSION = 18;
	private final static String TAG = "com.group1.wer.DataManager";
	
	private final static String TABLE_PARTICIPANTS = "Participants";
	private final static String TABLE_EVENTS = "Events";
	private final static String TABLE_EXPENSES = "Expenses";
	private final static String TABLE_EXPENSE_PARTICIPANTS="ExpenseParticipants";
	private final static String TABLE_PAYMENTS = "Payments";
	
	public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd",Locale.US);
	
	public DataManager(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public static Date stringToDate(String dbDate) {
		Date date = null;
		try {
			date = dateFormat.parse(dbDate);
		} catch (ParseException e) {
			date = null;
		}
		return date;
	}
	
	public static String dateToString(Date date) { 
		String stringDate = dateFormat.format(date);
		return stringDate;
	}
	
	public Event getEvent(long id) {
		Event event = null;
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query(TABLE_EVENTS,
				 new String[] {"id", "name", "date", "isReconciled"}, 
				 "id=?",
				 new String[] {Long.toString(id)},
				 null, null, null);
		
		if (cursor.moveToFirst()) {
			event = new Event(cursor.getLong(0),
	  				cursor.getString(1),
	  				stringToDate(cursor.getString(2)),
	  				(cursor.getLong(3) == 0 ? false : true));
			
			Log.i(TAG, "Selecting " + event.toString());
			
			//pull nested expenses, participants
			List<Expense> expenses = null;
			try {
				expenses = getExpensesByEventId(id);
			} catch (Exception e) {		
				expenses = new ArrayList<Expense>();
			}
			
			List<Participant> participants = null;
			try {
				participants = getParticipantsByEventId(id);
			} catch (Exception e) {
				participants = new ArrayList<Participant>();
			}
			
			event.setExpenses(expenses);
			event.setParticipants(participants);
		}
		
		return event;
	}
	
	public List<Event> getAllEvents() throws Exception {
		List<Event> entities = new ArrayList<Event>();
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query(TABLE_EVENTS, null, null, null, null, null, null);
		
		while (cursor.moveToNext()) {
			Event event = new Event(cursor.getLong(0),
	  				cursor.getString(1),
	  				stringToDate(cursor.getString(2)),
	  				(cursor.getLong(3) == 0 ? false : true));
			
			Log.i(TAG, "Selecting " + event.toString());	
			
			entities.add(event);
		}		
		return entities;
	}
	
	public long saveEvent(Event	event) {
		long id = -1;
		
		if (event.getId() > -1) {
			updateEvent(event);
			id = event.getId();
		} else {
			id = insertEvent(event);
		}
		
		return id;
	}
	
	public long insertEvent(Event event) {
		long id = -1;
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues insertValues = new ContentValues();
		
		insertValues.put("name", event.getName());
		insertValues.put("date", dateToString(event.getDate()));
		insertValues.put("isReconciled", (event.getIsReconciled() ? 1 : 0));
		
		id = db.insert(TABLE_EVENTS, null, insertValues);		
		event.setId(id);
		
		Log.i(TAG, "Inserted " + event.toString());
		return id;
	}
	
	public int updateEvent(Event event) {
		int rowsAffected = 0;
		SQLiteDatabase db = this.getWritableDatabase();
				
		ContentValues updateValues = new ContentValues();
		updateValues.put("name", event.getName());
		updateValues.put("date", dateToString(event.getDate()));
		updateValues.put("isReconciled", (event.getIsReconciled() ? 1 : 0));
		
		rowsAffected = db.update(TABLE_EVENTS, updateValues, "id=?", new String[] {Long.toString(event.getId())});
		
		Log.i(TAG, "Updated " + event.toString());
		return rowsAffected;		
	}

	public int deleteEvent(Event event) {
		int rowsAffected = 0;
		if (event.getId() != -1) {
			rowsAffected = deleteEvent(event.getId());
		}
		
		return rowsAffected;
	}	
	
	public int deleteEvent(long eventId) {
		int rowsAffected = 0;
		SQLiteDatabase db = this.getWritableDatabase();
		rowsAffected = db.delete(TABLE_EVENTS, "id=" + eventId,null);
		Log.i(TAG,"Deleted Event (Id = " + eventId);
		return rowsAffected;
	}
	
	public Expense getExpense(long id) {
		Expense expense = null;
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query(TABLE_EXPENSES,
				 new String[] {"id", "eventId", "name", "date", "amount"}, 
				 "id=?",
				 new String[] {Long.toString(id)},
				 null, null, null);
		
		if (cursor.moveToFirst()) {
			expense = new Expense(cursor.getLong(0),
	  							  cursor.getLong(1),
	  							  cursor.getString(2),
	  							  stringToDate(cursor.getString(3)),
	  							  cursor.getDouble(4));
			Log.i(TAG, "Selecting " + expense.toString());			
		}
		
		return expense;
	}
	
	public List<Expense> getExpensesByEventId(long eventId) throws Exception {
		List<Expense> entities = new ArrayList<Expense>();
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query(TABLE_EXPENSES,
				 null,  
				 "eventId=?",
				 new String[] {Long.toString(eventId)},
				 null, null, null);
		
		while (cursor.moveToNext()) {
			Expense expense = new Expense(cursor.getLong(0),
								  cursor.getLong(1),
								  cursor.getString(2),
								  stringToDate(cursor.getString(3)),
								  cursor.getDouble(4));
			
			Log.i(TAG, "Selecting " + expense.toString());
			
			//Load nested ExpenseParticipants
			List<ExpenseParticipant> expenseParticipants = null;
			try {
				expenseParticipants = getExpenseParticipantsByExpenseId(expense.getId());
			} catch (Exception e) {		
				expenseParticipants = new ArrayList<ExpenseParticipant>();
			}
			
			expense.setParticipants(expenseParticipants);	

			entities.add(expense);
		}		
		return entities;
	}
	
	public long saveExpense(Expense expense) {
		long id = -1;
		
		if (expense.getId() > -1) {
			updateExpense(expense);
			id = expense.getId();
		} else {
			id = insertExpense(expense);
		}
		
		return id;
	}
	
	public long insertExpense(Expense expense) {
		long id = -1;
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues insertValues = new ContentValues();
		
		insertValues.put("eventId", expense.getEventId());
		insertValues.put("name", expense.getName());
		insertValues.put("date", dateToString(expense.getDate()));
		insertValues.put("amount", expense.getAmount());
		
		id = db.insert(TABLE_EXPENSES, null, insertValues);		
		expense.setId(id);
		
		Log.i(TAG, "Inserted " + expense.toString());
		return id;
	}
	
	public long updateExpense(Expense expense) {
		int rowsAffected = 0;
		SQLiteDatabase db = this.getWritableDatabase();
				
		ContentValues updateValues = new ContentValues();

		updateValues.put("eventId", expense.getEventId());
		updateValues.put("name", expense.getName());
		updateValues.put("date", dateToString(expense.getDate()));
		updateValues.put("amount", expense.getAmount());
		
		rowsAffected = db.update(TABLE_EXPENSES, updateValues, "id=?", new String[] {Long.toString(expense.getId())});
		
		Log.i(TAG, "Updated " + expense.toString());
		return rowsAffected;
	}
	
	public ExpenseParticipant getExpenseParticipant(long id) {
		ExpenseParticipant expenseParticipant = null;
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query(TABLE_EXPENSE_PARTICIPANTS,
				 new String[] {"id", "eventId", "participantId", "paid", "allottedAmount", "participating"}, 
				 "id=?",
				 new String[] {Long.toString(id)},
				 null, null, null);
		
		if (cursor.moveToFirst()) {
			expenseParticipant = new ExpenseParticipant(cursor.getLong(0),
													    cursor.getLong(1),
													    cursor.getLong(2),
													    cursor.getDouble(3),
													    cursor.getDouble(4),
													    (cursor.getInt(5) == 0 ? false : true));
			Log.i(TAG, "Selecting " + expenseParticipant.toString());			
		}
		
		return expenseParticipant;
	}	
	
	public List<ExpenseParticipant> getExpenseParticipantsByParticipantId(long participantId) throws Exception {
		List<ExpenseParticipant> entities = new ArrayList<ExpenseParticipant>();
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query(TABLE_EXPENSE_PARTICIPANTS,
				 null,  
				 "participantId=?",
				 new String[] {Long.toString(participantId)},
				 null, null, null);
		
		while (cursor.moveToNext()) {
			ExpenseParticipant expenseParticipant = new ExpenseParticipant(cursor.getLong(0),
																	       cursor.getLong(1),
																	       cursor.getLong(2),
																	       cursor.getDouble(3),
																	       cursor.getDouble(4),
																	       (cursor.getInt(5) == 0 ? false : true));
			
			Log.i(TAG, "Selecting " + expenseParticipant.toString());
			
			entities.add(expenseParticipant);
		}		
		return entities;
	}

	public List<ExpenseParticipant> getExpenseParticipantsByExpenseId(long expenseId) throws Exception {
		List<ExpenseParticipant> entities = new ArrayList<ExpenseParticipant>();
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query(TABLE_EXPENSE_PARTICIPANTS,
				 null,  
				 "expenseId=?",
				 new String[] {Long.toString(expenseId)},
				 null, null, null);
		
		while (cursor.moveToNext()) {
			ExpenseParticipant expenseParticipant = new ExpenseParticipant(cursor.getLong(0),
																	       cursor.getLong(1),
																	       cursor.getLong(2),
																	       cursor.getDouble(3),
																	       cursor.getDouble(4),
																	       (cursor.getInt(5) == 0 ? false : true));
			
			Log.i(TAG, "Selecting " + expenseParticipant.toString());
			
			entities.add(expenseParticipant);
		}		
		return entities;
	}
	
	public long saveExpenseParticipant(ExpenseParticipant expenseParticipant) {
		long id = -1;
		
		if (expenseParticipant.getId() > -1) {
			updateExpenseParticipant(expenseParticipant);
			id = expenseParticipant.getId();
		} else {
			id = insertExpenseParticipant(expenseParticipant);
		}
		
		return id;
	}
	
	public long insertExpenseParticipant(ExpenseParticipant expenseParticipant) {
		long id = -1;
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues insertValues = new ContentValues();
		
		insertValues.put("eventId", expenseParticipant.getEventId());
		insertValues.put("participantId", expenseParticipant.getParticipantId());
		insertValues.put("paid", expenseParticipant.getPaid());
		insertValues.put("allottedAmount", expenseParticipant.getAllottedAmount());
		insertValues.put("participating", (expenseParticipant.isParticipating() ? 1 : 0));
		
		id = db.insert(TABLE_EXPENSE_PARTICIPANTS, null, insertValues);		
		expenseParticipant.setId(id);
		
		Log.i(TAG, "Inserted " + expenseParticipant.toString());
		return id;
	}
	
	public int updateExpenseParticipant(ExpenseParticipant expenseParticipant) {
		int rowsAffected = 0;
		SQLiteDatabase db = this.getWritableDatabase();
				
		ContentValues updateValues = new ContentValues();
		updateValues.put("eventId", expenseParticipant.getEventId());
		updateValues.put("participantId", expenseParticipant.getParticipantId());
		updateValues.put("paid", expenseParticipant.getPaid());
		updateValues.put("allottedAmount", expenseParticipant.getAllottedAmount());
		updateValues.put("participating", (expenseParticipant.isParticipating() ? 1 : 0));
		
		rowsAffected = db.update(TABLE_EXPENSE_PARTICIPANTS, updateValues, "id=?", new String[] {Long.toString(expenseParticipant.getId())});
		
		Log.i(TAG, "Updated " + expenseParticipant.toString());
		return rowsAffected;		
	}
	
	public int deleteExpenseParticipant(ExpenseParticipant expenseParticipant) {
		int rowsAffected = 0;
		if (expenseParticipant.getId() != -1) {
			rowsAffected = deleteExpenseParticipant(expenseParticipant.getId());
		}
		
		return rowsAffected;
	}	
	
	public int deleteExpenseParticipant(long expenseParticipantId) {
		int rowsAffected = 0;
		SQLiteDatabase db = this.getWritableDatabase();
		rowsAffected = db.delete(TABLE_EXPENSE_PARTICIPANTS, "id=" + expenseParticipantId,null);
		Log.i(TAG,"Deleted ExpenseParticipant (Id = " + expenseParticipantId);
		return rowsAffected;
	}
		
	public Participant getParticipant(long id) {
		Participant participant = null;
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query(TABLE_PARTICIPANTS,
				 new String[] {"id", "eventId", "name", "phoneNumber", "currentBalance"}, 
				 "id=?",
				 new String[] {Long.toString(id)},
				 null, null, null);
		
		if (cursor.moveToFirst()) {
			participant = new Participant(cursor.getLong(0),
										  cursor.getLong(1),
										  cursor.getString(2),
										  cursor.getString(3),
										  cursor.getDouble(4));
			Log.i(TAG, "Selecting " + participant.toString());			
		}
		
		return participant;
	}	
	
	public List<Participant> getAllParticipants() throws Exception {
		List<Participant> entities = new ArrayList<Participant>();
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query(TABLE_PARTICIPANTS, null, null, null, null, null, null);
		
		while (cursor.moveToNext()) {
			Participant participant = new Participant(cursor.getLong(0),
										  			  cursor.getLong(1),
										  			  cursor.getString(2),
										  			  cursor.getString(3),
										  			  cursor.getDouble(4));
			Log.i(TAG, "Selecting " + participant.toString());	
			
			entities.add(participant);
		}		
		return entities;
	}
	
	public List<Participant> getParticipantsByEventId(long eventId) throws Exception {
		List<Participant> entities = new ArrayList<Participant>();
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query(TABLE_PARTICIPANTS,
				 null,  
				 "eventId=?",
				 new String[] {Long.toString(eventId)},
				 null, null, null);
		
		while (cursor.moveToNext()) {
			Participant participant = new Participant(cursor.getLong(0),
										  			  cursor.getLong(1),
										  			  cursor.getString(2),
										  			  cursor.getString(3),
										  			  cursor.getDouble(4));
			Log.i(TAG, "Selecting " + participant.toString());
			
			entities.add(participant);
		}		
		return entities;
	}

	public long saveParticipant(Participant participant) {
		long id = -1;
		
		if (participant.getId() > -1) {
			updateParticipant(participant);
			id = participant.getId();
		} else {
			id = insertParticipant(participant);
		}
		
		return id;
	}	

	public void saveParticipants(List<Participant> participants) {
		for (int i = 0; i < participants.size(); i++) {
			saveParticipant(participants.get(i));
		}
	}

	public int updateParticipant(Participant participant) {
		int rowsAffected = 0;
		SQLiteDatabase db = this.getWritableDatabase();
				
		ContentValues updateValues = new ContentValues();
		updateValues.put("name", participant.getName());
		updateValues.put("eventId", participant.getEventId());
		updateValues.put("phoneNumber", participant.getPhoneNumber());
		updateValues.put("currentBalance", participant.getCurrentBalance());
		
		rowsAffected = db.update(TABLE_PARTICIPANTS, updateValues, "id=?", new String[] {Long.toString(participant.getId())});
		
		Log.i(TAG, "Updated " + participant.toString());
		return rowsAffected;		
	}

	public long insertParticipant(Participant participant) {
		long id = -1;
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues insertValues = new ContentValues();
		
		insertValues.put("eventId", participant.getEventId());
		insertValues.put("name", participant.getName());
		insertValues.put("phoneNumber", participant.getPhoneNumber());
		insertValues.put("currentBalance", participant.getCurrentBalance());
		
		id = db.insert(TABLE_PARTICIPANTS, null, insertValues);		
		participant.setId(id);
		
		Log.i(TAG, "Inserted " + participant.toString());
		return id;
	}

	public int deleteParticipant(Participant participant) {
		int rowsAffected = 0;
		if (participant.getId() != -1) {
			rowsAffected = deleteParticipant(participant.getId());
		}
		
		return rowsAffected;
	}
	
	public int deleteParticipant(long participantId) {
		int rowsAffected = 0;
		SQLiteDatabase db = this.getWritableDatabase();
		rowsAffected = db.delete(TABLE_PARTICIPANTS, "id=" + participantId,null);
		Log.i(TAG,"Deleted Participant (Id = " + participantId);
		return rowsAffected;
	}
	
	public Payment getPayment(long id) {
		Payment payment = null;
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query(TABLE_PAYMENTS,
				 new String[] {"id", "eventId", "toName", "fromName", "amount"}, 
				 "id=?",
				 new String[] {Long.toString(id)},
				 null, null, null);
		
		if (cursor.moveToFirst()) {
			payment = new Payment(cursor.getLong(0),
								  cursor.getLong(1),
								  cursor.getString(2),
								  cursor.getString(3),
								  cursor.getDouble(4));
			Log.i(TAG, "Selecting " + payment.toString());			
		}
		
		return payment;	
	}
	
	public List<Payment> getPaymentsByEventId(long eventId) throws Exception {
		List<Payment> entities = new ArrayList<Payment>();
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query(TABLE_PAYMENTS,
				 new String[] {"id", "eventId", "toName", "fromName", "amount"}, 
				 "eventId=?",
				 new String[] {Long.toString(eventId)},
				 null, null, null);
		
		while (cursor.moveToNext()) {
			Payment payment = new Payment(cursor.getLong(0),
					  cursor.getLong(1),
					  cursor.getString(2),
					  cursor.getString(3),
					  cursor.getDouble(4));
			Log.i(TAG, "Selecting " + payment.toString());
			
			entities.add(payment);
		}		
		return entities;
	}	

	public long savePayment(Payment payment) {
		long id = -1;
		
		if (payment.getId() > -1) {
			updatePayment(payment);
			id = payment.getId();
		} else {
			id = insertPayment(payment);
		}
		
		return id;
	}
	
	public int updatePayment(Payment payment) {
		int rowsAffected = 0;
		SQLiteDatabase db = this.getWritableDatabase();
				
		ContentValues updateValues = new ContentValues();
		updateValues.put("eventId", payment.getEventId());
		updateValues.put("toName", payment.getTo());
		updateValues.put("fromName", payment.getFrom());
		updateValues.put("amount", payment.getAmount());
		
		rowsAffected = db.update(TABLE_PAYMENTS, updateValues, "id=?", new String[] {Long.toString(payment.getId())});
		
		Log.i(TAG, "Updated " + payment.toString());
		return rowsAffected;	
	}
	
	public long insertPayment(Payment payment) {
		long id = -1;
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues insertValues = new ContentValues();
		
		insertValues.put("eventId", payment.getEventId());
		insertValues.put("toName", payment.getTo());
		insertValues.put("fromName", payment.getFrom());
		insertValues.put("amount", payment.getAmount());
		
		id = db.insert(TABLE_PAYMENTS, null, insertValues);		
		payment.setId(id);
		
		Log.i(TAG, "Inserted " + payment.toString());
		return id;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {		
		String sql;	
		
		// Build Event Participants Table
		sql = "CREATE TABLE " + TABLE_EVENTS + " (" +
			  "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
			  "name TEXT, " +
			  "date TEXT, " +
			  "isReconciled INTEGER)";
		db.execSQL(sql);
		
		// Build Expense Table
		sql = "CREATE TABLE " + TABLE_EXPENSES + " (" +
				  "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				  "eventId INTEGER, " +
				  "name TEXT, " +
				  "amount REAL)";
		db.execSQL(sql);
		
		// Build Expense Participants Table
		sql = "CREATE TABLE " + TABLE_EXPENSE_PARTICIPANTS + " (" +
				  "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				  "eventId INTEGER, " +
				  "participantId INTEGER, " +
				  "paid REAL, " +
				  "allottedAmount REAL, " +
				  "participating INTEGER)";
		db.execSQL(sql);
		
		// Build Participants table
		sql = "CREATE TABLE " + TABLE_PARTICIPANTS + " (" +
			  "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
			  "eventId INTEGER, " +
			  "name TEXT, " +
			  "phoneNumber TEXT, " +
			  "currentBalance REAL)";
		db.execSQL(sql);
		
		// Build Payments table
		sql = "CREATE TABLE " + TABLE_PAYMENTS + " (" +
			  "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
			  "eventId INTEGER, " +
			  "toName TEXT, " +
			  "fromName TEXT, " +
			  "amount REAL)";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(TAG, "Upgrading db to version " + newVersion);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSE_PARTICIPANTS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTICIPANTS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYMENTS);
		onCreate(db);
	}	
}
