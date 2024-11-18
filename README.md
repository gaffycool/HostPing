# HostPing
- HostPing is an app designed to ping set URL domains 5 times and provide an average latency 
- The user is able to pull-to-refresh the latency to run all pinging concurrently, as well as running individual URL pinging by clicking each tile
- The user is also able to sort the host by latency by clicking the sorting icon
- Implemented PingHostsInteractor to support multithreading approach using Dispatchers.IO to run the network operation in the background thread so this doesn't cause any UI issues
- As per the requirement, I decided to use flatMapMerge with flow so it can emmit each result as the task completes
- As per the requirement, I have added comments where necessary to provide a better understanding on the specific highlighted line of code
- Total development time around 3.5 hours

Things I am proud of:
- Followed the best architecture MVVM and using Jetpack compose and Jetpack navigation so this will reduce refactoring in the future.

If more time:
- If there was more time allowed, I would add a secondary tab to show recently pinged list
- Adding a user search option so the user can specify the URl to ping instead of using the hard codded URL list

https://github.com/user-attachments/assets/2fafb303-1398-43e5-9ac2-8b7413a474d5

**App Architecture:**
This repository contains an implementation of a clean architecture for Android applications using Compose, MVVM, Hilt, Coroutines, Kotlin Flow, Repository, Retrofit, Mockk and JUnit.

The project is divided into several modules, including:
- `app`: The main application module, responsible for defining the UI using Compose and coordinating with the presentation layer.
- `commonDomain`: The domain module, responsible for defining the business logic of the application and exposing it through interfaces.
    - `interactor`: The Interactors are responsible for call repository functions and do the business logics and return required models to viewmodel.
- `commonData`: The data module, responsible for defining the data repository, api services and DTOs.
    - `data-repository`: The repository module, responsible for implementing the interfaces defined in the domain module and providing data from both the local and remote data sources.
    - `data-remote`: The remote data source module, responsible for implementing the logic to access data from a remote API, using Retrofit as the network client.

**Diagram of HostPing App Architecture:**
![hostPing app diagram](https://github.com/user-attachments/assets/f5182271-c5bc-4400-9a6f-d116adb2b590)

The project follows a layered architecture approach, with each layer (presentation, domain, repository and data) having its own set of responsibilities and being completely decoupled from the other layers. The communication between the layers is done through well-defined interfaces, allowing for easy testing and future modifications.

**Dependencies**
Public API: https://gist.githubusercontent.com/anonymous/290132e587b77155eebe44630fcd12cb/raw/777e85227d0c1c16e466475bb438e0807900155c/sk_hosts

**Unit testing:**
- Unit testing with JUnit 100% coverage for all Viewmodels, Repositories and Interactors
- Unit testing with Screenshot testing using Paparazzi https://betterprogramming.pub/sanely-test-your-android-ui-libraries-with-paparazzi-b6d46c55f6b0 



