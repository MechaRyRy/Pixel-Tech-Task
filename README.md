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

