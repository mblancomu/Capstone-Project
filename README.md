Udacity NanoDegree Capstone Project

API Keys: You'll need to obtain the following API keys in order to successfully build and run the app:

    Backendless API Key
    Google Places API for Android
    Google Maps API for Android
    Google Analytics API for Android

The Backendless API Key can be obtained by following the instructions here: https://backendless.com/mobile-developers/ For the Google APIs you need access with your account to: https://console.developers.google.com/apis/dashboard
,and put Enable the APIs for Maps and Places for Android. For Analytics you can follow the next link for read more and put your json file and xml tracker file: https://developers.google.com/analytics/devguides/collection/android/v4/

All key data should be inserted into the file app/src/main/res/values/google_maps_key.xml & 
in main/res/values/backendless_api.xml , also you need the google-services.json for  Analytics.

In this project also is adding the database tables that are use in the backend service, you only should import this information into your environment in Backendless. This files are adding in the folder backend, into the app folder.

The project incorporates all the points requested for this project:

Thanks everyone!!!

Features

Create a trip when your information(photo, recommends, description, phone, etc..) and share this travel with the community. You can vote your favorite trip and see if there is any trip near your position. All the information is offline when you download the first time th info, into a database local. You can select as favorite a trip, select by country or select by tourism type. You can do login with Facebook, Twitter or with the login app. In this project I will demonstrate the ability to create and app that meet the following requirement :

Core Platform Development

App integrates a library App validates all input from servers and users. If data does not exist or is in the wrong format, the app logs this fact and does not crash. App keeps all strings in a strings.xml file and enables RTL layout switching on all layouts App provides a widget to provide relevant information to the user on the home screen.

Google Play Services

App integrates two or more Google services Each service imported in the build.gradle is used in the app. If Location is used, the app customizes the userâ��s experience by using their location. If Admob is used, the app displays test ads. If admob was not used, student meets specifications. If Analytics is used, the app creates only one analytics instance. If analytics was not used, student meets specifications. If Maps is used, the map provides relevant information to the user. If maps was not used, student meets specifications. If Identity is used, the userâ��s identity influences some portion of the app. If identity was not used, student meets specifications.

Material Design

App theme extends AppCompat. App uses an app bar and associated toolbars. App uses standard and simple transitions between activities.

Building

App builds from a clean repository checkout with no additional configuration. App builds and deploys using the installRelease Gradle task. App is equipped with a signing configuration, and the keystore and passwords are included in the repository. Keystore is referred to by a relative path. All app dependencies are managed by Gradle.

Data Persistence

App implements a ContentProvider to access locally stored data. Must implement at least one of the three If it regularly pulls or sends data to/from a web service or API, app updates data in its cache at regular intervals using a SyncAdapter. OR If it needs to pull or send data to/from a web service or API only once, or on a per request basis (such as a search application), app uses an IntentService to do so. OR It it performs short duration, on-demand requests(such as search), app uses an AsyncTask. App uses a Loader to move its data to its views.
