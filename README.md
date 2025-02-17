# MangaShelff

1. Setup and Running the Application:

Prerequisites
* Android Studio(Hedgehog |2023.1.1RC 3
* Java/Kotlin Development Kit (JDK) installed
* A device or emulator.
* Internet connection for dependencies
Steps to Run
1. Clone the repository
2. Open(Manag folder inside Manga Folder) the project in Android Studio.
3. Sync gradle by clicking File → Sync Project with gradle Files.
4. Run the app using Run Command 

2. Architecture:

Project Structure with Clean Architecture

1. Presentation Layer (Jetpack Compose, ViewModel, StateFlow, LiveData)
    * Handles UI-related logic
    * Uses ViewModel to manage UI state
    * Observes data from the UseCase layer
2. Domain Layer (Use Cases, Business Logic, Models)
    * Defines the core business logic
    * Contains UseCase classes that interact with repositories
    * Independent of any framework or library
3. Data Layer (Repositories, Room, Retrofit)
    * Handles data sources (local & remote)
    * Uses Repository pattern to fetch and store data
    * Implements database (Room) and API calls (Retrofit)
3. Libraries Used.
* Jetpack Compose → UI Development (Presentation Layer)
* Retrofit → API Calls (Data Layer)
* Room → Local Database (Data Layer)
* Coroutines → Asynchronous Operations
* Navigation Component → Screen Navigation
* LiveData/StateFlow → Data Handling
* Coil → Image Loading in Jetpack Compose
* Clean Architecture → Organises code into layers for better maintainability.

TestCases(Edge Cases) :
	1. Launch the App without Internet After 1st installation.
	2. Launch the App without Internet after data Loaded .
	3. Turn Off internet while using the app.
	4.  Rotation of the screen and More …
