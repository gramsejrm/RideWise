# RideWise Android App

RideWise is a Kotlin-based Android app that integrates with the Strava API to provide cycling enthusiasts with a personalized dashboard of their activities, local segments, and club statistics. This demo project is intended as a technical showcase for Android development, API integration, and modern architecture patterns.

Clone this repo and load it into an emulator or physical device. Requires an internet connection for API authentication via web browser and usage. At the time this was published, testing authentication via the Strava app was incomplete.

---

## Setup & Usage

While this app does provide a demo mode, none of the OAuth2 or API calls will work without Strava API credentials. It is strongly recommended to add these to the local.properties file.

### 1. Real Mode (requires Strava credentials)
1. Create a Strava API application [here](https://developers.strava.com/).  
2. Add your `client_id` and `client_secret` to your `local.properties`:

```properties
STRAVA_CLIENT_ID=YOUR_CLIENT_ID
STRAVA_CLIENT_SECRET=YOUR_CLIENT_SECRET
```

## Demo Mode

- The app automatically detects the absence of `STRAVA_CLIENT_ID` and `STRAVA_CLIENT_SECRET`.  
- No login is required in demo mode; the user is taken directly to the main screen.  
- All API calls (profile, segments, etc.) return mocked data that looks like real Strava responses.  
- Reviewers can explore the app immediately without any external setup.

---

## Features

- **Login / OAuth2 Flow**: Authenticate with Strava to access your data.  
- **Profile Tab**: Displays your cycling stats, including miles ridden, elevation gain, and ride count.  
- **Segments Tab**: Shows Strava segments near your favorite rides.  
- **Club Tab (Future)**: Visualizes club stats and friends’ activities.  
- **Demo Mode**: If Strava credentials are not provided, the app automatically runs in demo mode using mocked data for all screens. This allows the app to be explored fully without requiring a Strava account.

## Future Improvements

While the current version of RideWise includes a working Profile and Segments tab, several enhancements are planned for future iterations:

- **Kudos Tab**: Display the names of athletes who have given kudos on the user’s most recent rides, with a filter for the last X rides.  
- **Club Stats Tab**: Provide a landing page for the user’s Strava club, including aggregated metrics, leaderboard-style rankings, and friends’ activity.  
- **Extended Metrics**: Include more detailed ride statistics such as average speed, cadence, and power output.  
- **Custom Favorites**: Allow users to mark favorite routes or locations and see nearby segments tailored to those locations.  
- **Offline Caching**: Cache athlete and segment data for faster load times and partial offline use.  
- **UI Enhancements**: Add charts, graphs, and interactive elevation profiles for segments to improve visualization.  
- **Unit & Integration Tests**: Expand automated tests to cover repository, network, and UI layers for more robust validation.  

These improvements would make RideWise a more complete and interactive cycling companion while maintaining the same architecture and demo-friendly capabilities.
