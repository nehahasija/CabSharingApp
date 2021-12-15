# CabSharingApp
* Create CabSharing Android Clone App
* Fetch and show user current location
* Fetch and show nearby cabs on Google Map as pins
* Clicking on a particular cab will take user to its detail view where user can rent that cab

## ARCHITECTURE
This Project uses the MVVM repository pattern along with retrofit for building. A Repository helps to manage data updates in the local database or remote data stores.
The View layers observe the LiveData in the ViewModel. And new data send along in the LiveData object, which causes the view update with the new data.
ONE OF THE MAIN BENEFITS OF USING THE ARCHITECTURE COMPONENT IS THAT THEY ARE LIFECYCLE AWARE AND ALLOW DEVELOPER TO AVOID THE NEED TO HANDEL THE CONFIGURATION CHANGES SUCH AS DEVICE ROTATION.
Code is seperated in diffrent layers which is always good for testablity and maintainence of app.

OPtimization scope- You can also use MVI architecture pattern. States and events in MVi can make app more testable and readable. Also it is a good approach if you plan on working with features in your app.

## DEPENDENCY INJECTION
Hilt is used for Dependency Injection as a wrapper on top of Dagger.
Most of the dependencies are injected with @Singleton scope 
For ViewModels, we use the out-of-the-box @HiltViewModel annotation that injects them with the scope of the navigation graph composables that represent the screens.

## Building the project
* Every feature is done in a different branch so that it will be easy to follow.
* Clone the project, the `master` branch has the latest code.
* This App uses the Google API Key for Maps. Get the API key from the Google Cloud Developer console after enabling the Maps feature for your project. Refer this [link](https://developers.google.com/maps/documentation/directions/get-api-key). And put that key in the local.properties file in your project:
Your gradle.properties will like below:
```
sdk.dir=PATH_TO_ANDROID_SDK_ON_YOUR_LOCAL_MACHINE    
apiKey=YOUR_API_KEY
```

## OPTIMIZATIONS
* Battery optimization- You can specify location accuracy using the setPriority() method, passing one of the following values as the argument. Here we are using PRIORITY_HIGH_ACCURACY but you can also use PRIORITY_BALANCED_POWER_ACCURACY provides accurate location while optimizing for power depending on your requirement.
* Code Optimization- We have used features like Kotlin coroutines, and view binding for our code to be optimized but there is always a scope to improvise.
   You can always add two way dataBinding to avoid boiler plate code. Here we are not using data binding because app does not have many UI screens where data needs to be updated.
