# Readme

## TODO

- Add an app bar.

## Dependency Assumptions

> No 3rd party frameworks - we want to see what you can do!

I've assumed that the following are ok based on the t ech task:

**Kotlin Standard Library:** Since the test specifically asks for Kotlin, use of Coroutines for
background threading and Flow for data streams should be allowed.

**Jetpack Compose:** This is explicitly requested in the requirements.

**AndroidX/Jetpack Libraries:** Standard Google-maintained libraries like ViewModel,
Navigation-Compose, and Room (for the required data persistence) as they are part of the modern
official Android toolset BUT no other libraries are allowed?

---

**Confirmed with Ben Price**

> Those assumptions are correct and also Retrofit and Ktor are fine for networking should that be a
> preference.

_Ben Price_

---

### Specific Dependencies Added

**androidx-lifecycle-viewmodel-ktx** -> Easier to launch coroutine scopes from ViewModel without
needing to create independent scopes / manage lifecycle.

**androidx.navigation.navigation-compose** -> Easier to create compose navigation, no need to create
emit and handle state changes manually.

**androidx.compose.material.material-icons-extended** -> Just for importing a few more available
icons.

**io.ktor.ktor-client-content-negotiation** -> Automatic content negotiation with ktor clients.

**io.ktor.ktor-client-okhttp** -> http engine backing ktor.

**io.ktor.ktor-serialization-kotlinx-json** -> JSON Parser for ktor.

**org.jetbrains.kotlinx.kotlinx-serialization-json** -> Transitive dependency of
`io.ktor.ktor-serialization-kotlinx-json` but can be imported to allow greater customization.

## Architecture

### Structure

- **Presentation**
    - Theming
    - Screens
        - View Models
        - UI
            - Top Level Screen UI
            - Loading Screen State UI
            - Loaded Screen State UI
            - Error Screen State UI

- **Domain**
    - Entities - Business models. What we want to display / perform business logic on before giving
      to the UI layer to render or to the repository layer to do other stuff with.
    - Repositories - Loads data from various data sources to create the domain model required by the
      contract. I have seen repositories that either focus on one model e.g. User or on the API e.g.
      Stackoverflow
    - Usecases - Breaks down business logic into reusable components, smaller components. In my
      experience of clean architecture I have seen this just become a proxy layer to the repo
- **Data**
    - data_sources - Contains the various data sources used to communicate outside of our system
        - rest
        - persistence
    - repositories - implements the contract from domain

### Notable Decisions

#### Screens Package

Splitting a screen into content components allows us to see the previews for each different state
and means a file has exactly one reason to change. You could do this with a
Provider, but I found it incredibly difficult to work with, it was breaking for seemingly no reason.

#### Mock data

A snapshot of the API data is stored in a debug source-set and loaded in the composable previews.
This allows developers to start thinking about how the data might be structured right away, and how
to deal with different states.

#### Testing

> Your code should be covered by unit tests.

This application is predominantly tested using Android UI tests, I have included a couple of "unit
tests" to demonstrate how they differ from an Android UI test, but for brevity I have not
implemented tests over the whole system.

For unit / UI tests everything is real except for the system boundaries of the application. There's
a custom Android test runner that deliberately switches the manual dependency injection graph to a
Test implementation that mocks the internals of the ktor rest client. I've also introduced a dequeue
mechanism to the test dependency graph developers can state what response they want for a given
request.

> But you haven't done a unit test because it does not test the smallest unit

Isn't this the debate of all time 😅 what constitutes a unit. I would just say that you make the
architecture and patterns work for you, not the other way around. If you can test your application
in the simplest way whilst covering all edge cases then you do it however works best and allows you
to ship fast, high quality code.

#### Unidirectional Data Flow

Data moves in a single, predictable direction through the application making it easier for
developers to track, debug and manage state changes. Events, such as `retry` flow up to the
repository layer and state flows back down through `observable` `Flow` pipes.

#### Navigation

Usually I would add a Nav controller and add additional routes, but given this is a single screen
application I have just coded so that it opens that screen only.

#### Persistence

You are probably surprised to see `SharedPreferences`, but given that the data we need to persist is
super simple it would be overkill at this point to start working with a DB. You could create
another implementation of the `Persistence` contract focussed on a DB implementation. You could also
introduce a delegate `Persistence` such that you could delegate some calls to one implementation and
other calls to another.