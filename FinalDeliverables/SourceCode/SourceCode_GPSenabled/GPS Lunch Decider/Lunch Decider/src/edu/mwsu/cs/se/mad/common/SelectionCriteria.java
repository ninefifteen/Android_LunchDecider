package edu.mwsu.cs.se.mad.common;

/**
 * Class that represents the selection criteria of the user. the class also
 * checks the inputs of the interface and changes the values to the appropriate
 * values that can be used for quering the database server
 * 
 */
public class SelectionCriteria {

	private String typeOfFood = "";
	private String typeOfRestaurant = "";
	private int drivingDistance = 0;
	private final String ANY = "any";
	private final String SELECT = "any";

	public SelectionCriteria(String typeOfFood, String typeOfRestaurant,
			String drivingDistance) {
		this.setTypeOfFood(typeOfFood);
		this.setTypeOfRestaurant(typeOfRestaurant);
		this.setDrivingDistance(drivingDistance);
	}

	public void resetAll() {
		setTypeOfFood("");
		setTypeOfRestaurant("");
		setDrivingDistance("");
	}

	/**
	 * This method sets the type of food value passed by the parameter
	 * typeOfFood. It changes null values to empty strings.
	 * 
	 * @param typeOfFood
	 *            the typeOfFood to set
	 * 
	 */
	public void setTypeOfFood(String typeOfFood) {
		if (typeOfFood == null || typeOfFood.equalsIgnoreCase("")) {
			this.typeOfFood = "";
		} else if (typeOfFood.equalsIgnoreCase(SELECT))
			this.typeOfFood = ANY;
		else {
			typeOfFood = typeOfFood.replaceAll(" ", "");
			typeOfFood = typeOfFood.toLowerCase();
			this.typeOfFood = typeOfFood;
		}
	}

	/**
	 * @return the typeOfFood
	 */
	public String getTypeOfFood() {
		return typeOfFood;
	}

	/**
	 * @param typeOfRestaurant
	 *            the typeOfRestaurant to set
	 */
	public void setTypeOfRestaurant(String typeOfRestaurant) {
		if (typeOfRestaurant == null || typeOfRestaurant.equalsIgnoreCase("")) {
			this.typeOfRestaurant = "";
		} else if (typeOfRestaurant.equalsIgnoreCase(SELECT))
			this.typeOfRestaurant = ANY;
		else {
			typeOfRestaurant = typeOfRestaurant.replaceAll(" ", "");
			typeOfRestaurant = typeOfRestaurant.toLowerCase();
			this.typeOfRestaurant = typeOfRestaurant;
		}
	}

	/**
	 * @return the typeOfRestaurant
	 */
	public String getTypeOfRestaurant() {
		return typeOfRestaurant;
	}

	/**
	 * @param drivingDistance
	 *            the drivingDistance to set
	 */
	public void setDrivingDistance(String drivingDistance) {
		if (drivingDistance == null || drivingDistance.equalsIgnoreCase("")) {
			this.drivingDistance = 0;
		} else if (drivingDistance.equalsIgnoreCase(SELECT))
			this.drivingDistance = 0;
		else {
			drivingDistance = drivingDistance.substring("less than ".length(),
					"less than ".length() + 1);
			this.drivingDistance = Integer.parseInt(drivingDistance);
		}
	}

	/**
	 * @return the drivingDistance
	 */
	public int getDrivingDistance() {
		return drivingDistance;
	}

}
