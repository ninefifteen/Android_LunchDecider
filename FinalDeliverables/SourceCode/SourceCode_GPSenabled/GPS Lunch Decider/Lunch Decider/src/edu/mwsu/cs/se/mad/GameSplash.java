package edu.mwsu.cs.se.mad;

/**
 * The GameSplah class constructs the user interface that dialogs 
 * showing the progress of the location search and restaurant search
 * the help info to the user
 * The interface implements a button that takes the user back to the
 * UserSelection screen
 *
 */
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import edu.mwsu.cs.se.mad.common.RestaurantSearchManager;
import edu.mwsu.cs.se.mad.common.SelectionCriateriaAdapter;
import edu.mwsu.cs.se.mad.common.SelectionCriteria;
import edu.mwsu.cs.se.mad.common.UserLocationManager;

/**
 * The GameSplah class constructs the user interface that dialogs showing the
 * progress of the location search and restaurant search
 * 
 */
public class GameSplash extends Activity {

	private SelectionCriateriaAdapter sc;
	private SelectionCriteria sd;
	private UserLocationManager lm;
	private Location finalLocation;
	private Builder builder;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_splash);

		builder = new Builder(GameSplash.this);

		sc = new SelectionCriateriaAdapter();
		sd = sc.getSelectionData();
		lm = new UserLocationManager();
		finalLocation = lm.queryUserCurrentLocation(this);
		/**
		 * execute the location search thread
		 */
		new LocationControl().execute();

	}

	@Override
	protected void onPause() {
		super.onPause();
		GameSplash.this.finish();
	}

	private static void startNewActivity(Context currentContext,
			Class<?> activityURI) {
		Intent intent = new Intent(currentContext, activityURI);
		currentContext.startActivity(intent);
	}

	/**
	 * thread class that wait until location information retrived from the GPS
	 * provider. It desplays an error message if loaction can not be retrived
	 * 
	 * 
	 * 
	 */
	private class LocationControl extends AsyncTask<Void, Void, Void> {
		private ProgressDialog messageDialog;

		protected void onPreExecute() {
			messageDialog = new ProgressDialog(GameSplash.this);
			messageDialog.setMessage("Determining your location...");
			messageDialog.setIndeterminate(true);
			messageDialog.setCancelable(false);
			messageDialog.show();
		}

		protected Void doInBackground(Void... params) {
			Long t = Calendar.getInstance().getTimeInMillis();
			while (!UserLocationManager.locationUpdated
					&& Calendar.getInstance().getTimeInMillis() - t < 3000) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			;
			return null;
		}

		protected void onPostExecute(Void loc) {
			if (messageDialog.isShowing()) {
				messageDialog.dismiss();
			}
			UserLocationManager.locationUpdated = false;
			GameSplash.this.checkLocationFound(); // search the restuaurant if
													// location can not be found
		}

	}

	/**
	 * This method start searching calls the RestaurantSearchManager class to
	 * start searching the restaurants only if the user location is found
	 */
	private void checkLocationFound() {
		if (finalLocation != null) {
			startRestaurantSearch();
		} else {
			showLocationNotFoundDialog();
		}

	}
/**
 * execute the restaurant search thread
 */
	private void startRestaurantSearch() {
		new SearchControl().execute();
	}

	private class SearchControl extends AsyncTask<Void, Void, Void> {

		private ProgressDialog messageDialog;

		@Override
		protected void onPreExecute() {

			messageDialog = new ProgressDialog(GameSplash.this);
			messageDialog.setMessage("Searching Restaurants...");
			messageDialog.setIndeterminate(true);
			messageDialog.setCancelable(false);
			messageDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {

			Thread searchRestThread = new Thread(new Runnable() {
				RestaurantSearchManager sm;

				@Override
				public void run() {

					sm = new RestaurantSearchManager();
					sm.searchRestaurant(sd, finalLocation);

				}

			});
			searchRestThread.start();
			Long t = Calendar.getInstance().getTimeInMillis();
			while (!RestaurantSearchManager.isSearchOK()
					&& Calendar.getInstance().getTimeInMillis() - t < 15000) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
			if (!RestaurantSearchManager.isSearchOK())
				searchRestThread.interrupt();

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (messageDialog.isShowing()) {
				messageDialog.dismiss();
			}
			if (!RestaurantSearchManager.isSearchOK()) {
				RestaurantSearchManager.setSearchOK(false);
				showCanNotFindRestaurantDialog();
			} else if (!RestaurantSearchManager.isRestaurantFound()) {
				RestaurantSearchManager.setRestaurantFound(false);
				showNoRestaurantFoundDialog();
			} else {
				RestaurantSearchManager.setSearchOK(false);
				RestaurantSearchManager.setRestaurantFound(false);
				startNewActivity(GameSplash.this, Game.class);
			}

		}

	}
/**
 * Dialog shown if loaction is not found
 */
	private void showLocationNotFoundDialog() {
		builder.setMessage("Location infromation not found.")
				.setCancelable(false)
				.setPositiveButton("Retry",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
								new LocationControl().execute();

							}
						})
				.setNegativeButton("Close",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								GameSplash.this.finish();

							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}
/**
 * dialog  shown if restaurants are not found  
 */
	public void showNoRestaurantFoundDialog() {
		builder.setMessage(
				"No restaurants can be found.\n Please Change the search criteria")
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						GameSplash.this.finish();

					}
				});
		AlertDialog alert = builder.create();
		alert.show();

	}

	private void showCanNotFindRestaurantDialog() {
		builder.setMessage("Error in searching Restaurants.")
				.setCancelable(false)
				.setPositiveButton("Retry",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
								startRestaurantSearch();

							}
						})
				.setNegativeButton("Close",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								GameSplash.this.finish();

							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}

}
