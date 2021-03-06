package edu.mwsu.cs.se.mad;

/**
 * The Setting class constructs the user interface that enables to change 
 * the settings of the application.
 * The interface enables users to turn the application sound on and off. The 
 * interface also implements a button that allows  users to return to the 
 * UserSelection screen 
 */
import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

public class Setting extends Activity {
	/**
	 * java object instance of the button that turns the sound on and off
	 */
	private ToggleButton soundOnOff;
	/**
	 * java object instance of the button that closes the settings screen and
	 * returns the user to the UserSelection screen
	 */
	private Button settingsBack;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		/*
		 * gets the resource from the xml layout and assigns to the java
		 * representation of the resource
		 */
		soundOnOff = (ToggleButton) findViewById(R.id.sound_on_off);
		settingsBack = (Button) findViewById(R.id.settings_back);
		/*
		 * set listener classes to the button and toggle-button objects
		 */
		soundOnOff
				.setOnCheckedChangeListener(new SoundOnOffCheckChangeListener());
		settingsBack.setOnClickListener(new SettingsBackListener());
	}

	/**
	 * The listener class for the soundOnOff button. The method implements the
	 * OnclickListener interface and the method onClick, the call back method
	 * that is called when a click event is generated by the button.
	 * 
	 */
	class SoundOnOffCheckChangeListener implements OnCheckedChangeListener {

		/**
		 * The call back method that handles the click event. The method turns
		 * the application sound on or off by getting the system audio manager.
		 * Note that the sound will be turned on by the system when the
		 * application is closed
		 * */
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			AudioManager appAudio = (AudioManager) buttonView.getContext()
					.getSystemService(Context.AUDIO_SERVICE);
			if (isChecked) {
				appAudio.setMicrophoneMute(false);
			} else
				appAudio.setMicrophoneMute(true);
		}

	}

	/**
	 * The listener class for the settingsBack button. The method implements the
	 * OnclickListener interface and the method onClick, the call back method
	 * that is called when a click event is generated by the button.
	 * 
	 */
	class SettingsBackListener implements OnClickListener {

		/**
		 * The call back method that handles the click event. The method closes
		 * the settings window and takes the user back to the UserSelection
		 * screen
		 * */
		@Override
		public void onClick(View v) {
			Setting.this.finish();

		}

	}
}
