package edu.mwsu.cs.se.mad;

/**
 * The Help class constructs the user interface that displays
 * the help info to the user
 * The interface implements a button that takes the user back to the
 *  UserSelection screen
 *
 */
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Help extends Activity {

	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		/**
		 * java object instance of the button that takes the user back to the
		 * UserSelection screen
		 */
		Button helpBack = (Button) findViewById(R.id.help_back);
		/**
		 * sets the listener class for the button
		 */
		helpBack.setOnClickListener(new HelpBackListener());
	}

	/**
	 * The listener class for the back button. The method implements the
	 * OnclickListener interface and the method onClick, the call back method
	 * that is called when a click event is generated by the button.
	 * 
	 */
	class HelpBackListener implements OnClickListener {

		/**
		 * the Method finishes the activity and takes the user back to the
		 * UserSelection screen
		 */
		@Override
		public void onClick(View v) {
			Help.this.finish();

		}

	}

}
