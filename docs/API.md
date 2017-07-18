1. Autocomplete Places API
--------------------------
  Provides the list of cities and/or states and/or countries that matches with the string provided
* **URL**

  /rest/autocomplete/places?str=<text>&region=<region-type>

* **Method:**
  
   GET
  
*  **URL Params**

   **Required:**
 
   str=[String] The partial string for which the matching places need to be provided
   
   region=[integer] The type of regions that need to be returned. 2 for states, 4 for states and 8 for countries. These numbers can be added to get a combination of different regions.


* **Success Response:**

  * **Code:** 200 
  
  **Content:** 
  
  {"destinations":
	[{"cities":
		[
			{
				"destination":<Destination>,
				"id":<Google Place Id>
			},
			{
				.......
			}
			.............
		]
	},
	{"states":[..........]}
	{"countries":[..........]}
  }
 
* **Error Response:**


* **Sample Call:**

  <server>/rest/autocomplete/places?str=bang&region=2
  
2. Near by places API sorted based on rating
--------------------------------------------
  API to get all the prominent places near a destination within a radius and sorted by rating

* **URL**

  /rest/nearbysearch/rating?place-id=<Google Id of the place>&radius=<radius in metres>

* **Method:**

   GET
  
*  **URL Params**

   **Required:**
 
   place-id=[string] The Google Id of the place
   
   radius=[integer] The radius within which to search in metres. Maximum allowed is 50000.

* **Success Response:**
  
  * **Code:** 200
  
    **Content:** 
	
	{"nearbyplaces":[
		{<Name of Destination>:
			{"location":
				{"lat":<latitude>,"lng":<longitude>},
				"name":<name>,
				"rating":<rating>,
				"types":[<list of place type this belongs to>]
			}
		},{...},
		]
	}
 
* **Error Response:**

  
* **Sample Call:**

  /rest/nearbysearch/rating?place-id=ChIJgWsCh7C4VTcRwgRZ3btjpY8&radius=50000

3. Near by places API sorted based on prominence
------------------------------------------------
  API to get all the prominent places near a destination within a radius and sorted by prominence.
  This option sorts results based on their importance. Ranking will favor prominent places within the specified area. 
  Prominence can be affected by a place's ranking in Google's index, global popularity, and other factors.

* **URL**

  /rest/nearbysearch/prominence?place-id=<Google Id of the place>&radius=<radius in metres>

* **Method:**

   GET
  
*  **URL Params**

   **Required:**
 
   place-id=[string] The Google Id of the place
   
   radius=[integer] The radius within which to search in metres. Maximum allowed is 50000.

* **Success Response:**
  
  * **Code:** 200
  
    **Content:** 
	
	{"nearbyplaces":[
		{<Name of Destination>:
			{"location":
				{"lat":<latitude>,"lng":<longitude>},
				"name":<name>,
				"rating":<rating>,
				"types":[<list of place type this belongs to>]
			}
		},{...},
		]
	}
 
* **Error Response:**

  
* **Sample Call:**

  /rest/nearbysearch/prominence?place-id=ChIJgWsCh7C4VTcRwgRZ3btjpY8&radius=50000
  
 4. Suggested Places API
------------------------------------------------
  API to get the optimal places to visit on a single day and the best route to take to cover these places.

* **URL**

  /rest/nearbysearch/tourist-places?destination=<destination>

* **Method:**

   GET
  
*  **URL Params**

   **Required:**
 
   destination=[string] The place to visit

* **Success Response:**
  
  * **Code:** 200
  
    **Content:** 
	
	{"result":[
		{
			"googleId":"ChIJ95mdmvyHBDkRDGfx0kRrtXg",
			"name":"\"Tourist Hotel\"",
			"rating":4.0,
			"latitute":"32.2481058",
			"longitude":"77.1854331",
			"TimeTakenToNextPlace":"00:47:00.000"
		},{...},...
	]}
 
* **Error Response:**

  
* **Sample Call:**
	/rest/nearbysearch/tourist-places?destination=manali