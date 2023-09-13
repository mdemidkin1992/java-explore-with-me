## Event Listings (java-explore-with-me)
This application empowers users to post events, register for participation, and browse event lists by location. 
The backend for this web application is developed using Java/SpringBoot and leverages Docker, PostgreSQL, and Hibernate.

The application's microservices infrastructure is implemented using Docker and docker-compose.yml:
- The primary service resides in the ```main``` module and handles the core business logic of the application
- The statistics service, located in the ```stats``` module, collects data on user event views
- Each service has its PostgreSQL database, configured in the ```application.properties``` files

___
### Key Features of ```explore-with-me-main```
API is divided into three parts:
- ```Public```: Accessible to any network user without registration.
- ```Private```: Accessible only to authorized users.
- ```Admin```: Intended for service administrators.

**```Public``` API functionality**
- Sorting event lists by view count or event dates
- Viewing detailed information about specific events
- Retrieving all available event categories and collections
- **Requests for event lists or complete event information are logged in the statistics service**

**```Private``` API functionality**
- Adding/modifying/viewing events
- Submitting applications for participation in events of interest
- Event creators have the ability to confirm applications submitted by other service users

**```Admin``` API functionality**
- Adding/modifying/removing event categories.
- Adding/removing/pinning event collections on the homepage.
- Moderation capabilities for user-posted events, including publication or rejection.
- Adding/activating/viewing/removing users.

**Database Structure**
- ```User```: Users are categorized as ```admin```, ```public```, or ```private```.
- ```Event```: Events posted by registered ```private``` users.
- ```Location```: Locations where events take place (see "Additional Functionality" for more details).
- ```Category```: Event categories.
- ```Compilation```: Event collections created by administrators.
- ```ParticipationRequest```: Participation requests for events, if this option is enabled for the event.

<img width="873" alt="Screenshot 2023-09-07 at 19 27 13" src="https://github.com/mdemidkin1992/java-explore-with-me/assets/118021621/6ba23e4e-5770-4b92-9038-6ee0f99accb1">

**Http-client**

To send requests to record statistics from the ```main``` service to ```stats```, ```RestTemplate``` is used. 
The ```StatsClient``` class is implemented in a separate submodule called ```stats``` and is imported as a dependency in ```main```.
___

### Stats Service ```explore-with-me-stats``` Functionality
- Recording information about processed API endpoint requests
- Providing statistics for selected dates and endpoints
___

#### Additional Functionality ```location_processing```
1. Administrators can add new locations ```Location```
2. Registered users, when posting events, specify their coordinates (```lat```, ```lon```):
   - Based on geolocation, the nearest locations where events occur are calculated (```locationList```)
   - Each location also has a list of events associated with it (```eventList```), creating a ```many-to-many``` relationship.
3. Regular users can view a list of ```APPROVED_BY_ADMIN``` locations and lists of ```PUBLISHED``` events in those locations:
   - The list of ```APPROVED_BY_ADMIN``` locations includes an event counter (```events```)
   - Lists of events that users can view must be approved by an administrator

![Untitled](https://github.com/mdemidkin1992/java-explore-with-me/assets/118021621/e41ad2ab-cd7a-4232-b1b1-67df8ae2c545)

#### Newly Added Controllers
#### Admin
* ```POST /admin/locations```
* ```UPDATE /admin/locations/{id}```
* ```DELETE /admin/locations/{id}```

#### Public
* ```GET /locations``` – Retrieve a list of all approved locations
* ```GET /events/{locationId}/locations``` – Retrieve a list of published events in a location

#### Updated Data Model

Location, as added by an administrator ```Location```: ```name```, ```lat```, ```lon```, ```rad```.

When viewing the list of locations, a ```LocationDtoWithEventsCount``` DTO is returned, including:
* ```name``` Location name
* ```events``` Number of events in the location

Only locations approved by administrators with the status ```APPROVED_BY_ADMIN``` are displayed.
