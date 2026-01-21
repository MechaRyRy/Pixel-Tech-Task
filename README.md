# Readme

## Dependency Assumptions

> No 3rd party frameworks - we want to see what you can do!

I've assumed that the following are ok based on the tech task:

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
- **Data**

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
mechanism to the test dependency graph developers can state what response they want for a given request.