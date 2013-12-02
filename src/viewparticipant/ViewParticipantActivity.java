package viewparticipant;

import wer.main.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ViewParticipantActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewparticipant_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_participant, menu);
		return true;
	}

}
