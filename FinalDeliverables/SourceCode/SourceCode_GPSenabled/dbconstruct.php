<html>
<?
/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
//	
//	Database Constuctor
//
//	Micro Android Developers
//	Advanced Software Engineering
//	Lunch Decider
//
//	Database Constructor builds three mySQL database tables for the Lunch
//	Decider application.  The Restaurants table stores the name, address,
//	and latitude and longitude or all Wichita Falls restaurants.  The 
//	Restaurants_by_food table stores the restaurant address and the 
//	corresponding food type that goes with the restaurant at that address.
//	The Restaurants_by_type table stores the restaurant address and the 
//	corresponding restaurant type that goes with the restaurant at that
//	address.  The Database Constructor uses data results returned by the 
//	Google Local Search API to fill the database.  The search is performed
//	repeatedly for all keywords for multiple coordinates covering the city
//	of Wichita Falls, TX.  Using crontab, Database Constructor will be run 
//	periodically to keep the database up to date.
//	
/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

//  Establish connection to mySQL database.
$connect = mysql_connect('localhost', 'sseals', 'n6488r') or die (mysql_error());
$db = mysql_select_db('sseals') or die (mysql_error());

//	Clear all tables for update.
$result = mysql_query("delete from Restaurants");
$result = mysql_query("delete from Restaurant_by_food");
$result = mysql_query("delete from Restaurant_by_type");


if($_POST["submit"]){
	//  Assign search keywords to array.
	$keywordArray = array('american','asian','barbecue','buffet','burgers','chicken','chinese','deli','fast+food','italian','mexican','pizza','seafood','steak');
	//  Assign several evenly spaced Wichita Falls latitudes and logitudes to array.
	$latlonArray = array("33.86642460450753,-98.57002258300781","33.86585445407186,-98.53569030761719","33.863573814253485,-98.50135803222656","33.892647407997344,-98.48762512207031","33.900626661568744,-98.54118347167969","33.92626920481366,-98.50547790527344","33.96500329452543,-98.52470397949219");

	$ip = getenv("REMOTE_ADDR");

	$id = 1;	// Restaurant id number for SQL
	
	// Search for all keywords in the keywordArray over several Wichita Falls locations defined in the latlonArray.
	foreach($keywordArray as $keyword)
	{
		foreach($latlonArray as $latlon)
		{
			// Create search url for Google API.
			$url = 'http://ajax.googleapis.com/ajax/services/search/local?v=1.0&q='.$keyword.'+restaurant&sll='.$latlon.'&rsz=8&key=ABQIAAAAJg15qk-wldvCjMJSlrO4nxTrjPOjwSnQq_1j3n8BrPij3fY26BQwP_jcCGm588z8rg4hidb-qqHsow&userip='.$ip; 

			// Perform Google search.
			$ch = curl_init(); 
			curl_setopt($ch, CURLOPT_URL, $url); 
			curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1); 
			curl_setopt($ch, CURLOPT_REFERER, 'http://cs2.mwsu.edu/~sseals/SE/index4.php'); 
			$body = curl_exec($ch); curl_close($ch); 
	
			// Decode the json string.
			$json = json_decode($body); 
			
			// Process the search results that are returned 8 at a time.
			for($j = 0; $j < 8; $j++)
			{
				$title = $json->responseData->results[$j]->titleNoFormatting;
				$address1 = $json->responseData->results[$j]->addressLines[0];
				$address2 = $json->responseData->results[$j]->addressLines[1];
				$phone = $json->responseData->results[$j]->phoneNumbers[0]->number;
				$latitude = $json->responseData->results[$j]->lat;
				$longitude = $json->responseData->results[$j]->lng;
				echo $title;
				echo '<br>';
				echo $keyword;
				echo '<br>';
				echo $address1;
				echo '<br>';
				echo $address2;
				echo '<br>';
				echo $phone;
				echo '<br>';
				echo $latitude;
				echo '<br>';
				echo $longitude;
				echo '<br><br>';
				$title = addslashes($title);
				$address1 = addslashes($address1);
				$address2 = addslashes($address2);
				$phone = addslashes($phone);
				
				// Checks if restaurant is already in the database.  If it is, the new keyword is added to the
				// appropriate database field.
				if(mysql_num_rows(mysql_query("SELECT address1 FROM Restaurants WHERE address1 = '$address1'")))
				{
					if($keyword == 'buffet')
					{
						mysql_query("INSERT INTO Restaurant_by_type VALUES ('$address1','$keyword')");		
					}
					elseif($keyword == 'fast+food')
					{
						mysql_query("INSERT INTO Restaurant_by_type VALUES ('$address1','fastfood')");		
					}
					else
					{	
						mysql_query("INSERT INTO Restaurant_by_food VALUES ('$address1','$keyword')");			
					}
				}
				
				// Else a new restaurant is added to the database.
				elseif($keyword == 'buffet')
				{
					mysql_query("INSERT INTO Restaurants (id,title,address1,address2,phone,latitude,longitude) VALUES ('$id','$title','$address1','$address2','$phone','$latitude','$longitude')");
					mysql_query("INSERT INTO Restaurant_by_type VALUES ('$address1','$keyword')");					
					$id++;
				}
				elseif($keyword == 'fast+food')
				{
					mysql_query("INSERT INTO Restaurants (id,title,restaurantType,address1,address2,phone,latitude,longitude) VALUES ('$id','$title','$address1','$address2','$phone','$latitude','$longitude')");
					mysql_query("INSERT INTO Restaurant_by_type VALUES ('$address1','fastfood')");					
					$id++;					
				}
				else
				{
					mysql_query("INSERT INTO Restaurants (id,title,address1,address2,phone,latitude,longitude) VALUES ('$id','$title','$address1','$address2','$phone','$latitude','$longitude')");
					mysql_query("INSERT INTO Restaurant_by_food VALUES ('$address1','$keyword')");					
					$id++;					
				}
			}
			//print_r($json);	
			sleep(1);
		}
	}
}
mysql_close($connect);

?>
<head>
	<title>Database Constructor</title>
</head>

<body>

<form method = "POST" action = "">
	
	<input type = "submit" value = "Build Database" name = "submit">
	
</form>
</body>
</html>