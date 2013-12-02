package main;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;
import android.util.Log;

public class DataManager extends SQLiteOpenHelper {
	private final static String DATABASE_NAME = "dbfile";
	private final static int DATABASE_VERSION = 1;
	private final static String TAG = "com.group1.wer.DataManager";
	
	private final static String TABLE_PARTICIPANTS = "Participants";
	private final static String TABLE_EVENTS = "Events";
	private final static String TABLE_EXPENSES = "Expenses";
	private final static String TABLE_EXPENSE_PARTICIPANTS="ExpenseParticipants";
	private final static String TABLE_PAYMENTS = "Payments";
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd",Locale.US);
	
	public DataManager(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public Event getEvent(long id) {
		Event event = null;
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query(TABLE_EVENTS,
				 new String[] {"id", "name", "isReconciled"}, 
				 "id=?",
				 new String[] {Long.toString(id)},
				 null, null, null);
		
		if (cursor.moveToFirst()) {
			event = new Event(cursor.getLong(0),
							  cursor.getString(1),
							  (cursor.getLong(2) == 0 ? false : true));
			Log.i(TAG, "Selecting " + event.toString());			
		}
		
		return event;
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
	
	public Participant getParticipant(long id) {
		Participant participant = null;
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query(TABLE_PARTICIPANTS,
				 new String[] {"id", "name", "phoneNumber", "currentBalance"}, 
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
		
	@Override
	public void onCreate(SQLiteDatabase db) {		
		String sql;	
		
		// Build Event Participants Table
		sql = "CREATE TABLE " + TABLE_EVENTS + " " +
			  "(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, isReconciled INTEGER)";
		db.execSQL(sql);
		
		// Build Expense Participants Table
		//sql = "CREATE TABLE " + TABLE_EXPENSE_PARTICIPANTS + " " +
		//		  "(id INTEGER PRIMARY KEY AUTOINCREMENT, pdarticipantId INTEGER, phoneNumber TEXT, currentBalance REAL)";
		//db.execSQL(sql);
		
		// Build Participants table
		sql = "CREATE TABLE " + TABLE_PARTICIPANTS + " " +
			  "(id INTEGER PRIMARY KEY AUTOINCREMENT, eventId INTEGER, name TEXT, phoneNumber TEXT, currentBalance REAL)";
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
