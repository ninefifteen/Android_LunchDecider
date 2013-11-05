<?
/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*
//	
//	Query Handler
//
//	Micro Android Developers
//	Advanced Software Engineering
//	Lunch Decider
//
//	Query Handler provides the main functionality of the Lunch Decider
//	application.  The phone builds a url that includes the restaurant
//	selection criteria and the phone user's current latitude and longitude.
//  The Query Handler obtains these values from the GET array and uses them
// 	to query the mySQL database (built by the dbconstruct.php script).  The
//	Query Handler also performs all distance calculations using with the
//	assistance of the distance function.  The Query Handler randomly selects
// 	a restaurant from the set of restaurants matching the user input criteria.
//	The necessary data for the randomly selected restaurant is then json
// 	encoded and returned to the phone. 
/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/


/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
/*::                                                                         :*/
/*::  Distance Function:                                                     :*/
/*::  this routine calculates the distance between two points (given the     :*/
/*::  latitude/longitude of those points). it is being used to calculate     :*/
/*::  the distance between two zip codes or postal codes using our           :*/
/*::  zipcodeworld(tm) and postalcodeworld(tm) products.                     :*/
/*::                                                                         :*/
/*::  definitions:                                                           :*/
/*::    south latitudes are negative, east longitudes are positive           :*/
/*::                                                                         :*/
/*::  passed to function:                                                    :*/
/*::    lat1, lon1 = latitude and longitude of point 1 (in decimal degrees)  :*/
/*::    lat2, lon2 = latitude and longitude of point 2 (in decimal degrees)  :*/
/*::    unit = the unit you desire for results                               :*/
/*::           where: 'm' is statute miles                                   :*/
/*::                  'k' is kilometers (default)                            :*/
/*::                  'n' is nautical miles                                  :*/
/*::  united states zip code/ canadian postal code databases with latitude & :*/
/*::  longitude are available at http://www.zipcodeworld.com                 :*/
/*::                                                                         :*/
/*::  For enquiries, please contact sales@zipcodeworld.com                   :*/
/*::                                                                         :*/
/*::  official web site: http://www.zipcodeworld.com                         :*/
/*::                                                                         :*/
/*::  hexa software development center © all rights reserved 2004            :*/
/*::                                                                         :*/
/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
function distance($lat1, $lon1, $lat2, $lon2, $unit) { 

  $theta = $lon1 - $lon2; 
  $dist = sin(deg2rad($lat1)) * sin(deg2rad($lat2)) +  cos(deg2rad($lat1)) * cos(deg2rad($lat2)) * cos(deg2rad($theta)); 
  $dist = acos($dist); 
  $dist = rad2deg($dist); 
  $miles = $dist * 60 * 1.1515;
  $unit = strtoupper($unit);

  if ($unit == "K") {
    return ($miles * 1.609344); 
  } else if ($unit == "N") {
      return ($miles * 0.8684);
    } else {
        return $miles;
      }
}
/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/



/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*
//	
//	Query Handler
//
//	Below is the main Query Handler code.
/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

// Assign values sent from phone from GET array.
$restaurantType = $_GET["restaurantType"];
$foodType = $_GET["foodType"];
$maxDistance = $_GET["maxDistance"];
$slMaxDistance = $maxDistance * 0.80;	// Reduce $maxDistance by 20% to compensate for straight line measurement.
$phoneLatLon = $_GET["latlon"];
$phoneLatLonArray = explode(",", $phoneLatLon);
$phoneLat = $phoneLatLonArray[0];
$phoneLon = $phoneLatLonArray[1];

// Connect to mysql database.
$connect = mysql_connect('localhost', 'sseals', 'n6488r') or die (mysql_error());
$db = mysql_select_db('sseals') or die (mysql_error());

$matchesRTFT = array();

// Obtain all matches of restaurantType from Restaurant_by_type table.
if($restaurantType != 'any')
{
	$dataRT = mysql_query("SELECT * FROM Restaurant_by_type WHERE restaurantType = '$restaurantType'") or die(mysql_error());
	while($infoRT = mysql_fetch_array($dataRT))
	{
		$matchesRT[] = $infoRT[0];	// $matchesRT is an array of all restaurantType matching addresses.
	}
}

// Obtain all matches of foodType from Restaurant_by_food table.
if($foodType != 'any')
{
	$dataFT = mysql_query("SELECT * FROM Restaurant_by_food WHERE foodType = '$foodType'") or die(mysql_error());
	while($infoFT = mysql_fetch_array($dataFT))
	{
		$matchesFT[] = $infoFT[0];	// $matchesFT is an array of all foodType matching addresses.
	}
}

// If a both a restaurantType and foodType were selected, create an array of addresses that match both 
// foodType and restaurantType.
if($restaurantType != 'any' && $foodType != 'any')
{
	foreach($matchesRT as $addressRT)
	{
		if(in_array($addressRT, $matchesFT))
		{
			$matchesRTFT[] = $addressRT;
		}
	}
}
// If only a foodType was selected, create an array of addresses that match foodType.
elseif($restaurantType == 'any' && $foodType != 'any')
{
	foreach($matchesFT as $addressFT)
	{
		$matchesRTFT[] = $addressFT;
	}
}
// If only a restaurantType was selected, create an array of addresses that match restaurantType.
elseif($restaurantType != 'any' && $foodType == 'any')
{
	foreach($matchesRT as $addressRT)
	{
		$matchesRTFT[] = $addressRT;
	}
}
// Else neither restaurantType or foodType were selected.  Addresses of all restaurants in 
// database are loaded into $matchesRTFT array.
else
{
	$dataRTFT = mysql_query("SELECT * FROM Restaurants WHERE 1") or die(mysql_error());
	while($infoRTFT = mysql_fetch_array($dataRTFT))
	{
		$matchesRTFT[] = $infoRTFT[2];
	}
}

// If a maximum distance was selected by the user.
if($maxDistance != 0 && count(matchesRTFT) != 0)
{
	// Obtain the latitude and longitude of each address in the $matchesRTFT array.
	foreach($matchesRTFT as $address1)
	{
		$dataLatLon = mysql_query("SELECT * FROM Restaurants WHERE address1 = '$address1'") or die(mysql_error());
		$infoLatLon = mysql_fetch_array($dataLatLon);
		$matchesLat[] = $infoLatLon[5];
		$matchesLon[] = $infoLatLon[6];
	}
	// Calculate the straight line distance ($slDist) from phone user to all matching restaurants and
	// insert addresses of restaurants which are within $maxDistance into $matchesAll array.
	$ind = 0;	// indexing variable
	foreach($matchesRTFT as $address)
	{
		$slDist = distance($matchesLat[$ind], $matchesLon[$ind], $phoneLat, $phoneLon, 'm');
		if($slDist <= $slMaxDistance)
		{
			$matchesAll[] = $address;
		}
		$ind++;
	}
}
// Else the user did not select a maximum distance.
elseif(count($matchesRTFT) != 0)
{
	foreach($matchesRTFT as $address)
	{
		$matchesAll[] = $address;
	}
}

// Select random restaurant from $matchesAll array
if(count($matchesAll) != 0)
{
	$i = 0;
	$randNumArray = array();
	while($i < 4 && $i < count($matchesAll))
	{
		$randNum = rand(0, ((count($matchesAll))-1));
		while(in_array($randNum, $randNumArray))
		{
			$randNum = rand(0, ((count($matchesAll))-1));
		}
		$randNumArray[] = $randNum;
		$selectedAddress = $matchesAll[$randNum];
	
		$selectedRestaurant = mysql_query("SELECT * FROM Restaurants WHERE address1 = '$selectedAddress'") or die(mysql_error());
		$selectedRestaurantData = mysql_fetch_array($selectedRestaurant);
	
		$returnArray[] = $selectedRestaurantData;
		$i++;
	}
	
	$fillerQuery = mysql_query("SELECT * FROM Restaurants WHERE 1") or die(mysql_error());
	while($filler = mysql_fetch_array($fillerQuery))
	{
		$fillerAddressArray[] = $filler[2];
	}
	for($i = 0; $i < (4 - count($matchesAll)); $i++)
	{
		$fillerAddress = $fillerAddressArray[rand(0, ((count($fillerAddressArray))-1))];
		
		$fillerRestaurant = mysql_query("SELECT * FROM Restaurants WHERE address1 = '$fillerAddress'") or die(mysql_error());
		$fillerRestaurantData = mysql_fetch_array($fillerRestaurant);
	
		$returnArray[] = $fillerRestaurantData;
	}
	
	echo '{restaurants:';
	echo json_encode($returnArray);
	echo '}';
}
// Else, no restaurants match user input criteria.
else
{
	echo '{restaurants:[]}';
}

mysql_close($connect);
?>