package edu.mwsu.cs.se.mad;

/**
 * The UserSelection class constructs the user interface that accepts 
 * user selection data and transfers it to the gameSplash class. The 
 * interface accepts user criteria using drop down lists that contain the 
 * type of food, type of restaurant and maximum driving distance. The 
 * interface also implements buttons that allows the user to open the help
 * and setting screens. 
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import edu.mwsu.cs.se.mad.common.SelectionCriateriaAdapter;
import edu.mwsu.cs.se.mad.common.SelectionCriteria;

/**
 * 
 * The UserSelection calls that extends from the android.app.Activity class It
 * handles user interface events such as button clicks. It also opens the
 * GameSplash class and transfers the user selection data to the GameSplash
 * interface
 */
public class UserSelection extends Activity {
	// java object instance of the button that opens the settings screen
	private Button setting;
	// java object instance of the button that opens the GameSplash screen
	private Button startGame;
	// java object instance of the button that opens the help screen
	private Button help;
	// java object instance of the drop-down list that contains type of food
	// criteria
	private Spinner typeOfFoodSpinner;
	// java object instance of the drop-down list that contains type of
	// restaurant
	private Spinner typeOfRestaurantSpinner;
	// java object instance of the drop-down list that contains the driving
	// direction
	private Spinner drivingDistanceSpinner;
	// Manager class for the selection criteria class object instance
	private SelectionCriateriaAdapter selCrInstance;
	// The ArrayList adapter object used as a backing instance for the
	// typepOfFood spinner
	private ArrayAdapter<CharSequence> typeOfFoodAdapter;
	// The ArrayList adapter object used as a backing instance for the
	// typepOfFood spinner
	private ArrayAdapter<CharSequence> typeOfRestAdapter;
	// The ArrayList adapter object used as a backing instance for the
	// typepOfFood spinner
	private ArrayAdapter<CharSequence> drivingDistanceAdapter;
	// constant indicating specific spinner item style
	private final int SPINNER_ITEM_STYLE = android.R.layout.simple_spinner_item;
	// constant indicating the spinner's style
	private final int SPINNER_DROPDOWN_STYLE = android.R.layout.simple_spinner_dropdown_item;
	SelectionCriateriaAdapter criteriaModelAdapter;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);// sets the view to this layout

		/*
		 * gets the resource from the xml layout and assigns to the java
		 * representation of the resource
		 */
		setting = (Button) getItemById(R.id.settings);
		startGame = (Button) getItemById(R.id.startGame);
		help = (Button) getItemById(R.id.help);
		typeOfFoodSpinner = (Spinner) getItemById(R.id.type_of_food);
		typeOfRestaurantSpinner = (Spinner) getItemById(R.id.type_of_restaurant);
		drivingDistanceSpinner = (Spinner) getItemById(R.id.driving_distance);

		/*
		 * assigning drop-down data to the spinner array adapters
		 */
		typeOfFoodAdapter = setAdapterData(R.array.type_of_food,
				SPINNER_ITEM_STYLE, SPINNER_DROPDOWN_STYLE);
		typeOfRestAdapter = setAdapterData(R.array.type_of_rest,
				SPINNER_ITEM_STYLE, SPINNER_DROPDOWN_STYLE);
		drivingDistanceAdapter = setAdapterData(R.array.driving_distance,
				SPINNER_ITEM_STYLE, SPINNER_DROPDOWN_STYLE);
		/*
		 * set the adapter to the spinners(drop-down)
		 */
		setSpinnerAdapter(typeOfFoodSpinner, typeOfFoodAdapter);
		setSpinnerAdapter(typeOfRestaurantSpinner, typeOfRestAdapter);
		setSpinnerAdapter(drivingDistanceSpinner, drivingDistanceAdapter);
		/*
		 * set listener classes to the button objects
		 */
		setting.setOnClickListener(new SettingListener());
		startGame.setOnClickListener(new GameLauncherListener());
		help.setOnClickListener(new HelpListener());
		selCrInstance = new SelectionCriateriaAdapter();

	}

	/**
	 * Gets the item from the XML file that defines the view as a View object
	 * having the id specified by the itemId parameter
	 * 
	 * @param itemId
	 *            The item Id to be checked by the
	 * @return the view object that matches the id
	 */
	private View getItemById(int itemId) {

		return findViewById(itemId);
	}

	/**
	 * The setSpinnerAdapter method sets the array adapters that contain the
	 * spinner list to the spinner objects
	 * 
	 * @param spinner
	 *            the spinner object in which the adapter will be assigned
	 * @param spinnerAdapter
	 *            the array adapter to be assigned for the spinner
	 */
	private void setSpinnerAdapter(Spinner spinner,
			ArrayAdapter<CharSequence> spinnerAdapter) {

		spinner.setAdapter(spinnerAdapter);
	}

	/**
	 * The setAdapterData method sets the selection options to the array
	 * adapters and defines the drop down view resource.
	 * 
	 * @param arrayResId
	 *            array of resource from the string.xml file that defines the
	 *            selection criteria of each spinner
	 * @param viewResId
	 *            the layout id to be used for the array adapter elements
	 * @param viewResource
	 *            the layout id to be used for the array adapter itself
	 * @return the newly created array adapter
	 */
	private ArrayAdapter<CharSequence> setAdapterData(int arrayResId,
			int viewResId, int viewResource) {
		ArrayAdapter<CharSequence> localAdapter = ArrayAdapter
				.createFromResource(this, arrayResId, viewResId);
		localAdapter.setDropDownViewResource(viewResource);
		return localAdapter;
	}

	/**
	 * The startNewActivity creates a new activity using the current activity
	 * defined by the activityURI argument
	 * 
	 * @param currentContext
	 *            the current context to be used for creating the new activity
	 * @param activityURI
	 *            the URI for new activity
	 */
	private static void startNewActivity(Context currentContext,
			Class<?> activityURI) {
		Intent intent = new Intent(currentContext, activityURI);
		currentContext.startActivity(intent);
	}

	/**
	 * 
	 * The listener class for the game startGame button. The method implements
	 * the OnclickListener interface and the method onClick, the call back
	 * method that is called when a click event is generated by the button.
	 * 
	 */
	class GameLauncherListener implements OnClickListener {

		/**
		 * The call back method that handles the click event. This method gets
		 * the restaurant data from the restaurant drop-down lists, creates a
		 * new SelectionCriteria data model object to collect data and transfers
		 * the model object to the SelectionCriteriaAdapter class. The method
		 * then creates the GameSplash activity.
		 */
		@Override
		public void onClick(View v) {
			/*
			 * the SelectionCriteria object used to collect the user selection
			 * data
			 */
			SelectionCriteria selecCrDataInstance;
			/*
			 * temporary string values used to collect the user imput data
			 */
			String typeOfFoodSelected;
			String typeOfRestSelected;
			String drivingDistSelecetd;
			/**
			 * getting the user selection from the spinner objects
			 */
			typeOfFoodSelected = typeOfFoodSpinner.getSelectedItem().toString();
			typeOfRestSelected = typeOfRestaurantSpinner.getSelectedItem()
					.toString();
			drivingDistSelecetd = drivingDistanceSpinner.getSelectedItem()
					.toString();
			/**
			 * assigning the selected values to the SelectionCriteria data model
			 * object
			 */
			selecCrDataInstance = new SelectionCriteria(typeOfFoodSelected,
					typeOfRestSelected, drivingDistSelecetd);
			/**
			 * send the data model to the SelectionCriteriaAdapter so that the
			 * data will be available to the other Activities(User interfaces)
			 */
			selCrInstance.setSelectionData(selecCrDataInstance);
			/**
			 * create the new GameSplash activity
			 */
			startNewActivity(v.getContext(), GameSplash.class);
		}

	}

	/**
	 * The listener class for the setting button. The method implements the
	 * OnclickListener interface and the method onClick, the call back method
	 * that is called when a click event is generated by the button.
	 * 
	 */
	class SettingListener implements OnClickListener {

		/**
		 * The call back method that handles the click event. The method creates
		 * the Setting activity.
		 */
		@Override
		public void onClick(View v) {
			startNewActivity(UserSelection.this, Setting.class);
		}

	}

	/**
	 * The listener class for the Help button. The method implements the
	 * OnclickListener interface and the method onClick, the call back method
	 * that is called when a click event is generated by the button.
	 * 
	 */
	class HelpListener implements OnClickListener {

		/**
		 * The call back method that handles the click event. The method creates
		 * the Help activity.
		 */
		@Override
		public void onClick(View v) {
			startNewActivity(v.getContext(), Help.class);
		}

	}

}