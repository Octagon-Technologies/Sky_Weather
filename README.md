# Sky Weather
A beautifully designed weather app created with XML for the UI and Kotlin for the logic.

<img width="920" alt="play_store_graphic (1)" src="https://github.com/Octagon-Technologies/Sky_Weather/assets/62815445/3eb4a5ce-d11a-4a20-a69b-268b3269cd17">

## Setup
1) Fork the repository
2) Sync and build project
3) NO API key registration needed since the app is using a free tier API - OpenMeteo (A free weather API)

## App Screenshots
<img src="https://github.com/Octagon-Technologies/Sky_Weather/assets/62815445/d581d793-93da-4de2-a524-67a8003a4510" width="250">
<img src="https://github.com/Octagon-Technologies/Sky_Weather/assets/62815445/4e7b8eaa-3728-41d6-af0a-e8f859008ce4" width="250">
<img src="https://github.com/Octagon-Technologies/Sky_Weather/assets/62815445/7107ede8-a6e5-4dcb-a111-5a64d6d039fc" width="250">
<img src="https://github.com/Octagon-Technologies/Sky_Weather/assets/62815445/92dbf7ca-b3ae-4ea0-9c48-d9f00b11d92c" width="250">

## Technologies used
- Kotlin
- Retrofit for network calls
- Moshi for json deserialization
- Room for local caching of weather data
- Datastore for storing user settings
- GeoLocation API

 ## Issues and TO-FIX items
 - URGENT: Workmanger is not refreshing data AT ALL
 - The initial Location selection screen isn't very user-friendly
 - Enable notifications is faulty (shows it's on despite the user not granting permission)
 - Re-instate the Add-Widget feature (Make the widgets look like Iphone widgets)
