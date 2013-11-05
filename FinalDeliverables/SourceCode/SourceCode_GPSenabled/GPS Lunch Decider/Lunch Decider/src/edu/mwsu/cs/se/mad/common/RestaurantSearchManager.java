package edu.mwsu.cs.se.mad.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;

/**
 * The RestaurantSearchManager class accepts the users current location and
 * search criteria to query the restaurants available
 * 
 * 
 * 
 */
public class RestaurantSearchManager {

	private static List<Restaurant> restaurantList = new ArrayList<Restaurant>();
	private HttpClient httpClient;
	private HttpGet httpGet;
	private HttpResponse httpResponse;
	private BufferedReader bufferedReader;
	private URI uri;
	/**
	 * The query parameter names
	 */
	private final String URL_PARAM_REST_TYPE = "restaurantType";
	private final String URL_PARAM_FOOD_TYPE = "foodType";
	private final String URL_PARAM_DIST = "maxDistance";
	private final String URL_PARAM_LAT_LONG = "latlon";
	// the search URL
	private final String QUERY_URI_BASE = "http://cs2.mwsu.edu/~sseals/SE/queryhandler.php?";
	/**
	 * The json data paramaetr names
	 */
	private final String JSON_PARAM_TITLE = "title";
	private final String JSON_PARAM_ADDRESS1 = "address1";
	private final String JSON_PARAM_ADDRESS2 = "address2";
	private final String JSON_PARAM_PHONE = "phone";
	private final String JSON_PARAM_LATITUDE = "latitude";
	private final String JSON_PARAM_LONGITUDE = "longitude";
	/**
	 * boolean to check if the search is successful or not
	 */
	private static boolean searchOK = false;
	/**
	 * boolean to check if restaurants were found or not
	 */
	private static boolean restaurantFound = false;

	public RestaurantSearchManager() {
		httpClient = new DefaultHttpClient();
		httpGet = new HttpGet();
	}

	/**
	 * Searches restaurants based on the search criteria and user's current
	 * location
	 * 
	 * @param selectedCriteria
	 *            the search criteria
	 * @param lastLocation
	 *            the user location
	 */
	public void searchRestaurant(SelectionCriteria selectedCriteria,
			Location lastLocation) {
		/**
		 * build the uri for the data provider server
		 */
		uri = buildURI(selectedCriteria, lastLocation);
		if (uri == null) {
			setSearchOK(false);
			return;
		}
		/**
		 * query the server
		 */
		httpGet.setURI(uri);
		try {
			httpResponse = httpClient.execute(httpGet);
			InputStream serverResultJson = httpResponse.getEntity()
					.getContent();

			bufferedReader = new BufferedReader(new InputStreamReader(
					serverResultJson));

			String jsonDataLine = bufferedReader.readLine();
			StringBuilder jsonDataBuild = new StringBuilder();
			/**
			 * read each line of response from the server
			 */
			while (jsonDataLine != null) {
				jsonDataBuild.append(jsonDataLine);
				jsonDataBuild.append("\n");
				jsonDataLine = bufferedReader.readLine();
			}
			/**
			 * parse the Json data and save the information in list of
			 * restaurant objects
			 */
			parseJson(jsonDataBuild);
			if (!restaurantList.isEmpty())
				restaurantFound = true;
			setSearchOK(true);

		} catch (IOException io) {
			setSearchOK(false);
		} catch (JSONException e) {
			setSearchOK(false);
		}

	}

	/**
	 * This method parses the Json data retived from the server. It puts the
	 * data back to an instance of restaurants data model object and saves each
	 * instance in a list
	 * 
	 * @param jsonDataBuild
	 *            the json string to be parsed
	 * @throws JSONException
	 *             if the string is not prasable by the Json parsor
	 */
	private void parseJson(StringBuilder jsonDataBuild) throws JSONException {

		restaurantList.clear();

		JSONObject object = new JSONObject(jsonDataBuild.toString());
		JSONArray jsonRestaurantList = object.getJSONArray("restaurants");
		for (int index = 0; index < jsonRestaurantList.length(); index++) {
			JSONObject jsonRestaurant = jsonRestaurantList.getJSONObject(index);

			Restaurant restaurantItem = new Restaurant();
			restaurantItem.setTitle(jsonRestaurant.getString(JSON_PARAM_TITLE));
			restaurantItem.setAddress1(jsonRestaurant
					.getString(JSON_PARAM_ADDRESS1));
			restaurantItem.setAddress2(jsonRestaurant
					.getString(JSON_PARAM_ADDRESS2));
			restaurantItem.setPhone(jsonRestaurant.getString(JSON_PARAM_PHONE));
			restaurantItem.setLatitude(jsonRestaurant
					.getString(JSON_PARAM_LATITUDE));
			restaurantItem.setLongitude(jsonRestaurant
					.getString(JSON_PARAM_LONGITUDE));

			restaurantList.add(restaurantItem);
		}

	}

	/**
	 * This method builds the database server query url. The url contains the
	 * user selection criateria and the user's current location
	 * 
	 * @param selectedCriteria
	 *            the user selection criateria fetched from the UserSelection
	 *            screen
	 * @param lastLocation
	 *            the users current location
	 * @return the URI cotaining the search criteria and user's location
	 */
	private URI buildURI(SelectionCriteria selectedCriteria,
			Location lastLocation) {

		String latLong = String.valueOf(lastLocation.getLatitude()) + ","
				+ String.valueOf(lastLocation.getLongitude());

		StringBuilder queryUrl = new StringBuilder(QUERY_URI_BASE);

		queryUrl.append(URL_PARAM_REST_TYPE);
		queryUrl.append("=");
		queryUrl.append(selectedCriteria.getTypeOfRestaurant());
		queryUrl.append("&");
		queryUrl.append(URL_PARAM_FOOD_TYPE);
		queryUrl.append("=");
		queryUrl.append(selectedCriteria.getTypeOfFood());
		queryUrl.append("&");
		queryUrl.append(URL_PARAM_DIST);
		queryUrl.append("=");
		queryUrl.append(selectedCriteria.getDrivingDistance());
		queryUrl.append("&");
		queryUrl.append(URL_PARAM_LAT_LONG);
		queryUrl.append("=");
		queryUrl.append(latLong);

		try {
			URL getURL = new URL(queryUrl.toString());
			URI getURI = new URI(getURL.getProtocol(), getURL.getHost(),
					getURL.getPath(), getURL.getQuery(), null);
			return getURI;
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 
	 * @return true if search is succssful
	 */
	public static boolean isSearchOK() {
		return searchOK;
	}

	/**
	 * @return the list of restaurants found
	 */
	public static List<Restaurant> getRestaurantList() {
		return restaurantList;
	}

	/**
	 * @return true if at least on restaurnat is found, false otherwise
	 */
	public static boolean isRestaurantFound() {
		return restaurantFound;
	}

	/**
	 * @param searchOK
	 *            the searchOK value to set. True if search is successful,
	 *            otherwaise false
	 */
	public static void setSearchOK(boolean searchOK) {
		RestaurantSearchManager.searchOK = searchOK;
	}

	/**
	 * 
	 * @param restaurantFound
	 *            the restaurantFound value to set. True if at least one
	 *            restauran is found, otherwaise false
	 */
	public static void setRestaurantFound(boolean restaurantFound) {
		RestaurantSearchManager.restaurantFound = restaurantFound;

	}

}
