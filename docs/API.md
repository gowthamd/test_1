**1. Autocomplete Places API**
----
  Provides the list of cities and/or states and/or countries that matches with the string provided
* **URL**

  /rest/autocomplete/places?str=\<text\>&region=\<region-type\>

* **Method**
  
   `GET`
  
*  **URL Params**

   **Required:**
 
   str=[String] The partial string for which the matching places need to be provided
   
   region=[integer] The type of regions that need to be returned. 2 for states, 4 for states and 8 for countries. These numbers can be added to get a combination of different regions.


* **Success Response:**

  * **Code** 200 
  
  **Content** 
  
  `{"destinations":
	[{"cities":
		[
			{
				"destination":\<Destination\>,
				"id":\<Google Place Id\>
			},
			{
				.......
			}
			.............
		]
	},
	{"states":[..........]}
	{"countries":[..........]}
  }`
 
* **Error Response:**


* **Sample Call:**

  \<server\>/rest/autocomplete/places?str=bang&region=2
  
**2. Near by places API sorted based on rating**
----
  API to get all the prominent places near a destination within a radius and sorted by rating

* **URL**

  /rest/nearbysearch/rating?place-id=\<Google Id of the place\>&radius=\<radius in metres\>

* **Method:**

   `GET`
  
*  **URL Params**

   **Required:**
 
   place-id=[string] The Google Id of the place
   
   radius=[integer] The radius within which to search in metres. Maximum allowed is 50000.

* **Success Response:**
  
  * **Code:** 200
  
    **Content:** 
	
	`{"nearbyplaces":[
		{\<Name of Destination\>:
			{"location":
				{"lat":\<latitude\>,"lng":\<longitude\>},
				"name":\<name\>,
				"rating":\<rating\>,
				"types":[\<list of place type this belongs to\>]
			}
		},{...},
		]
	}`
 
* **Error Response:**

  
* **Sample Call:**

  /rest/nearbysearch/rating?place-id=ChIJgWsCh7C4VTcRwgRZ3btjpY8&radius=50000

**3. Near by places API sorted based on prominence**
----
  API to get all the prominent places near a destination within a radius and sorted by prominence.
  This option sorts results based on their importance. Ranking will favor prominent places within the specified area. 
  Prominence can be affected by a place's ranking in Google's index, global popularity, and other factors.

* **URL**

  /rest/nearbysearch/prominence?place-id=\<Google Id of the place\>&radius=\<radius in metres\>

* **Method:**

   `GET`
  
*  **URL Params**

   **Required:**
 
   place-id=[string] The Google Id of the place
   
   radius=[integer] The radius within which to search in metres. Maximum allowed is 50000.

* **Success Response:**
  
  * **Code:** 200
  
    **Content:** 
	
	`{"nearbyplaces":[
		{\<Name of Destination\>:
			{"location":
				{"lat":\<latitude\>,"lng":\<longitude\>},
				"name":\<name\>,
				"rating":\<rating\>,
				"types":[\<list of place type this belongs to\>]
			}
		},{...},
		]
	}`
 
* **Error Response:**

  
* **Sample Call:**

  /rest/nearbysearch/prominence?place-id=ChIJgWsCh7C4VTcRwgRZ3btjpY8&radius=50000
  
**4. Suggested Places API**
----
  API to get the optimal places to visit on a single day and the best route to take to cover these places.

* **URL**

  /rest/nearbysearch/tourist-places?destination=\<destination\>

* **Method:**

   `GET`
  
*  **URL Params**

   **Required:**
 
   destination=[string] The place to visit

* **Success Response:**
  
  * **Code:** 200
  
    **Content:** 
	
	`{"result":[
		{
			"googleId":\<google-id\>,
			"name":\<name\>,
			"rating":\<rating\>,
			"latitute":\<latitude\>,
			"longitude":\<longitude\>,
			"TimeTakenToNextPlace":\<time-taken-to-next-place\>,
			"time-to-spent":{"hours":\<hours\>,"minutes":\<minutes\>}
		},{...},{...}
	]}`
 
* **Error Response:**

  
* **Sample Call:**
	/rest/nearbysearch/tourist-places?destination=manali

**5. Distance Between Two Places**
----
  API to get the distance and the time to travel between two places

* **URL**

  /rest/distance/find?origin=\<lat1,long1\>&destination=\<lat2,long2\>&unit=kms

* **Method:**

   `GET`
  
*  **URL Params**

   **Required:**
 
   lat1=[string] 	Latitude of Source
   
   long1=[string] 	Longitude of Source
   
   lat2=[string] 	Latitude of Destination
   
   long2=[string] 	Longitude of Destination

* **Success Response:**
  
  * **Code:** 200
  
    **Content:** 
	
	`{"distance":\<distance\>,"duration":\<duration\>}`
 
* **Error Response:**

  
* **Sample Call:**
	/rest/distance/find?origin=32.248105,77.1854331&destination=32.2417774,77.1889494&unit=kms
	
**6. All Tourist Places API**
----
  API to get all the tourist places in a destination

* **URL**

  /rest/nearbysearch/tourist-places-all?destination=\<destination\>

* **Method:**

   `GET`
  
*  **URL Params**

   **Required:**
 
   destination=[string] The Destination Place

* **Success Response:**
  
  * **Code:** 200
  
    **Content:** 
	
	`{"tourist-places":[
		{"geometry":{
			"location":{"lat":12.961937,"lng":77.63484699999999},
			"viewport":{
				"northeast":{"lat":12.9631570802915,"lng":77.6361762302915},
				"southwest":{"lat":12.9604591197085,"lng":77.63347826970849}}
			},
			"name":"Ezz Holidays",
			"rating":4.8,
			"opening_hours":{"open":"09:30:00.000","close":"20:00:00.000"},
			"google-id":\<google-id\>,
			"time-to-spent": { "hours": \<hours\>,	"minutes": \<minutes\>	}
		},
		{....},
		.....
		]
	}`
 
* **Error Response:**

  
* **Sample Call:**
	/rest/nearbysearch/tourist-places-all?destination=bangalore
	
**7. Modify Places to Visit API from the Suggested Route API Response**
----
  _The API is used to add or remove a place in the suggested route.
  The time to spent at each location can also be altered.
  This API can lead to changes in the order of the destinations in the suggested route.
  If all the places in the destination need to be included in the algorithm set re-run-algo flag to true_

* **URL**

  /rest/modifyroute/modify

* **Method:**
  
  `PUT`


* **Data Params**
{
	`"retained-places"`:`[
    {"googleId":<google-id>,"name":<name>,"rank":<rank>,"rating":<rating>,"location":{"lat":<latitude>,"lng":<longitude>},"time-to-spent":<time-to-spent>},
    {.......},
    {.......}
    ]`
  ,
	
    `"added-places"`:`[{"googleId":<google-id>,"name":<name>,"rank":<rank>,"rating":<rating>,"location":{"lat":<latitude>,"lng":<longitude>},"time-to-spent":<time-to-spent>},
    {.......},
    {.......}]`,		
		
  
	`"removed-place-ids"`:`[<removed-place-ids>]`,
  
	`"destination"`:`<destination>`,
  
	`"re-run-algo"`:`<true\false>`

}`

* **Success Response:**
  

  * **Code:** 200 <br />
    **Content:** 
	`{"result":`[
		{"googleId":\<google-id\>,"name":\<name\>,"rating":\<rating\>,"latitute":\<latitude\>,"longitude":\<longitude\,"TimeTakenToNextPlace":\<time-taken\>,"rank":\<rank\>,
		"time-to-spent":{"hours":\<hours\>,"minutes":\<minutes\>}},
		{....},
		{....}
		]`}`

* **Sample Call:**

	URL : /rest/modifyroute/modify
	Method : PUT
	Content-Type : application/json
	Request Body:
{
  "retained-places":
    `[
    {"googleId":"ChIJN1ZKKUkWrjsRzxIVM363-LE","name":"\"Bengaluru Palace\"","rating":4.1,"location":{"lat":"12.9986964","lng":"77.59202599999999"},"rank":1,"time-to-spent":"1:00"},
    {"googleId":"ChIJk0gN-2sWrjsRljNKfECgL9M","name":"\"Jawaharlal Nehru Planetarium\"","rating":4.2,"location":{"lat":"12.984865","lng":"77.5895718"},"rank":2,"time-to-spent":"2:00"},
    {"googleId":"ChIJL2fQ53MWrjsRuN9D6aalLMY","name":"\"Cubbon Park\"","rating":4.4,"location":{"lat":"12.9763472","lng":"77.59292839999999"},"rank":3,"time-to-spent":"1:15"},
    {"googleId":"ChIJHdPykcEVrjsRIr4v35kLEY4","name":"\"Lalbagh Botanical Garden\"","rating":4.4,"location":{"lat":"12.9507432","lng":"77.5847773"},"rank":4,"time-to-spent":"1:00"}
    ]`
  ,
    "removed-place-ids":`["ChIJqZQybIEWrjsRezNLL4Ju2Gk","ChIJVQ947HgWrjsRty7bPqHZG48","ChIJBw42C-09rjsRs7KmQUqyf3o","ChIJRzdYfjQ4rjsRc9kA7UZXzZk","ChIJlTBafjoUrjsRc99a3-6HCY4",
    "ChIJQWihIyoXrjsR-L_b7ztSjpY","ChIJ3RcPFRkUrjsRVJHcR7hCUUM"]`,
	
	"added-places":`[
    {"googleId":"","name":"Bannerghatta Biological Park","rating":4.1,"location":{"lat":12.8003592,"lng":77.57760979999999},"rank":5,"time-to-spent":"3:00"}
  ]`,
    
  `"destination"`:`"bangalore"`,
  
  `"do-refresh"`:`false`
  
    

}

**8. Add another day to the suggested Places API**
----
  _The API is used to add another day to the suggested route.
  If the UI is having the all places details, that can be provided to speed up the response._
  

* **URL**

  /rest/modifyroute/nextday

* **Method:**
  
  `PUT`


* **Data Params**

  `{
  "selected-place-ids":    `[<google-id-list>]`,
  "destination" : `<destination>`,
  "all-places"	: `[<all-places-list>]`
}`
	
	**_The all-places param can be used to provide all the places in the destination. This will make server to avoid querying for the places_**

* **Success Response:**
  

  * **Code:** 200 <br />
    **Content:** `{"result":[
		{"googleId":<google-id>,"name":<name>,"rating":<rating>,"latitute":<latitude>,"longitude":<longitude>,"TimeTakenToNextPlace":<time-taken>,"time-to-spent":{"hours":<hours>,"minutes":<minutes>}},
		{....},
		....
		]}`

* **Sample Call:**

	URL : /rest/modifyroute/nextday
	Method : PUT
	Content-Type : application/json
	Request Body:
`{
	"selected-place-ids":`["ChIJN1ZKKUkWrjsRzxIVM363-LE",
			"ChIJk0gN-2sWrjsRljNKfECgL9M",
			"ChIJL2fQ53MWrjsRuN9D6aalLMY",
			"ChIJHdPykcEVrjsRIr4v35kLEY4",
			"ChIJqZQybIEWrjsRezNLL4Ju2Gk",
			"ChIJVQ947HgWrjsRty7bPqHZG48",
			"ChIJ8VNf1mMWrjsRwsMEl564ksQ"]`,
	"destination":"bangalore"
}`

