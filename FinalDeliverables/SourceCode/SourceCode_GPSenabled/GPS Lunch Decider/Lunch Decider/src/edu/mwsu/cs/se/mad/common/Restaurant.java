package edu.mwsu.cs.se.mad.common;

/**
 * The restaurant data model class. This class represents the restaurant
 * instances that are returned from the database server.
 * 
 */
public class Restaurant {
	/**
	 * The name of the restaurant
	 */
	private String title;

	private String address1;
	private String address2;
	private String phone;
	private String latitude;
	private String longitude;

	/**
	 * Set the values to null
	 */
	public Restaurant() {
		setTitle(null);
		setAddress1(null);
		setAddress2(null);
		setPhone(null);
		setLatitude(null);
		setLongitude(null);
	}

	/**
	 * constructor that initializes the restaurant data with the parameters
	 * provided
	 * 
	 * @param title
	 *            The title of the restaurant
	 * @param address1
	 *            the street address of the restaurant
	 * @param address2
	 *            the city and state part of the address
	 * @param phone
	 *            The phone number of the restaurant
	 * @param latitude
	 *            the latitude value for the restaurant
	 * @param longitude
	 *            the longitude value for the restaurant
	 */
	public Restaurant(String title, String address1, String address2,
			String phone, String latitude, String longitude) {
		super();
		this.title = title;
		this.address1 = address1;
		this.address2 = address2;
		this.phone = phone;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	/**
	 * get the restaurant name
	 * 
	 * @return The restaurant name
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * sets the restaurant name
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * gets the street address of the restaurant
	 * 
	 * @return
	 */
	public String getAddress1() {
		return address1;
	}

	/**
	 * sets the street address of the restaurant
	 * 
	 * @param address1
	 */
	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	/**
	 * get the city and state of the restaurant
	 * 
	 * @return the city and the restaurant
	 */
	public String getAddress2() {
		return address2;
	}

	/**
	 * sets the city and state of the restaurant
	 * 
	 * @param address2
	 *            the city and state of the address
	 */
	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	/**
	 * gets the phone number of the restaurant
	 * 
	 * @return the phone number of the restaurant
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * sets the phone number of the restaurant
	 * 
	 * @param phone
	 *            the phone number of the restaurant
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * gets the latitude of the restaurant
	 * 
	 * @return latitude of the restaurant
	 */
	public String getLatitude() {
		return latitude;
	}

	/**
	 * sets latitude of the restaurant
	 * 
	 * @param latitude
	 *            latitude of the restaurant
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	/**
	 * gets longitude of the restaurant
	 * 
	 * @return longitude of the restaurant
	 */
	public String getLongitude() {
		return longitude;
	}

	/**
	 * sets longitude of the restaurant
	 * 
	 * @param longitude
	 *            longitude of the restaurant
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

}
