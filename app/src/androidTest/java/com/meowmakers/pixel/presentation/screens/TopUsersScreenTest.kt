package com.meowmakers.pixel.presentation.screens

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.platform.app.InstrumentationRegistry
import com.meowmakers.pixel.Fixtures
import com.meowmakers.pixel.PixelActivity
import com.meowmakers.pixel.injection.MockResponse
import com.meowmakers.pixel.injection.dependencies
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import org.junit.Rule
import org.junit.Test

class TopUsersScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<PixelActivity>()

    @Test
    fun showsLoadingSpinner() {
        composeTestRule
            .onNodeWithTag("loading_spinner")
            .assertIsDisplayed()
    }

    @Test
    fun showsLoaded_whenFetchingIsSuccessful() {
        val dependencies = InstrumentationRegistry.getInstrumentation().dependencies()
        dependencies.responseQueue.add(
            MockResponse(
                "/2.2/users",
                { scope ->
                    scope.respond(
                        content = Fixtures.topUsersJson.raw,
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }
            )
        )

        composeTestRule
            .onNodeWithTag("loading_spinner")
            .assertIsDisplayed()

        composeTestRule.waitUntil(timeoutMillis = 3000) {
            composeTestRule
                .onNodeWithTag("loaded_content")
                .isDisplayed()
        }

        composeTestRule.onAllNodesWithTag("user_item")
            .assertCountEquals(5)
    }

    @Test
    fun showsError_whenFetchingIsUnsuccessful() {
        val dependencies = InstrumentationRegistry.getInstrumentation().dependencies()
        dependencies.responseQueue.add(
            MockResponse(
                "/2.2/users",
                { scope ->
                    scope.respondError(HttpStatusCode.Forbidden)
                }
            )
        )

        composeTestRule
            .onNodeWithTag("loading_spinner")
            .assertIsDisplayed()

        composeTestRule.waitUntil(timeoutMillis = 3000) {
            composeTestRule
                .onNodeWithTag("error_content")
                .isDisplayed()
        }
    }
}