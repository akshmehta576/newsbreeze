# NewsBreeze
NewsBreeze is an app that gives the latest breaking news to you.

With NewsBreeze, you can search news by your choice, sort news according to date & publish and you can save news for read it later.

### **Note** 
The mobile sections of this guide focus on Android devices and currently, you can't run this app in dark mode.

## <h2><br/>ScreenShots:iphone: 

<img src="https://user-images.githubusercontent.com/71426030/190719861-90e302bc-6318-4b54-992d-f56534cf0d4f.jpg" width="200" height="400" />  <img src="https://user-images.githubusercontent.com/71426030/190720218-736a3645-7895-4149-bd90-82dcfbbaa491.jpg" width="200" height="400" />  <img src="https://user-images.githubusercontent.com/71426030/190721130-0568068a-a008-4154-bc8f-ce83aad72116.jpg" width="200" height="400" />  

  ## <h2><br/>Techstack - libraries used:hammer:
* AndroidX - Android library for core functionalities.
* Cardview - Widget in Android that can be used to display any sort of data
  by providing a rounded corner layout along with a specific elevation.
* GSON - It convert Java Objects into their JSON representation.
* Design - It helps you implement shiny, interactive Material Design component.
* Glide - Image Loader Library for Android.
* RoomDatabase - For storing data offline in database.
* Navigation Components - Navigation occurs between your app's destinations—that is, 
  anywhere in your app to which users can navigate.
* DaggerHilt - Dependency Injection library for Android that reduces the boilerplate
  of doing manual dependency injection in your project.
* Retrofit - A type-safe HTTP client for Android.
  
  ## <h2><br/>How I worked.:point_down:
* Setup all dependencies in project first.
* Used MVVM architecture so made all packages.
* And through newsApi got data on Model Class by Retrofit.
* Fetch it in repository.
* Then from repository got data in ViewModel and then to Fragments.
* Used navigation components for navigating from one fragment to another.
* Instance of room database is made where all the data will be saved offline in the database.
* So that we can fetch it when no internet is there.
* Used PopUp Menu for options of sorting data.
* Then implemented all the sorting by calling there particular api.
* Then in last done some UI tweaks and resolve some errors. 
  
  ## <h2><br/>Problem Faced:construction::construction:
- [ ] Content of detail news by API is not sufficient.
- [ ] Some data is not there so used default things.
  
  ## <h2><br/>Contact:link: 
  * Aksh Mehta - akshmehta576@gmail.com
