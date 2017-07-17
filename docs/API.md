1. Autocomplete Places API
--------------------------
  Provides the list of cities and/or states and/or countries that matches with the string provided
* **URL**

  /rest/autocomplete/places?str=<text>&region=<region-type>

* **Method:**
  
  `GET`
  
*  **URL Params**

   **Required:**
 
   str=[String] The partial string for which the matching places need to be provided
   
   region=[integer] The type of regions that need to be returned. 2 for states, 4 for states and 8 for countries. These numbers can be added to get a combination of different regions.


* **Success Response:**

  * **Code:** 200 
  
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
  