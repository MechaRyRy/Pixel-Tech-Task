@file:OptIn(ExperimentalTestApi::class)

package com.meowmakers.pixel.presentation.screens

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import com.meowmakers.pixel.Fixtures
import com.meowmakers.pixel.PixelActivity
import com.meowmakers.pixel.injection.MockResponse
import com.meowmakers.pixel.injection.TestApplicationContainer
import com.meowmakers.pixel.injection.dependencies
import com.meowmakers.pixel.injection.reset
import com.meowmakers.pixel.presentation.screens.top_users.ui.CustomTestTag
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TopUsersScreenTest {

    @get:Rule
    val composeTestRule = createEmptyComposeRule()

    private val dependencies: TestApplicationContainer
        get() = InstrumentationRegistry.getInstrumentation().dependencies()

    @Before
    fun setup() {
        InstrumentationRegistry.getInstrumentation().reset()
    }

    @Test
    fun showsLoadingSpinner() {
        ActivityScenario.launch(PixelActivity::class.java).use {
            composeTestRule
                .onNodeWithTag("loading_content")
                .assertIsDisplayed()
        }
    }

    @Test
    fun showsLoaded_whenFetchingIsSuccessful() {
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

        ActivityScenario.launch(PixelActivity::class.java).use {
            composeTestRule
                .onNodeWithTag("loading_content")
                .assertIsDisplayed()

            composeTestRule.waitUntilExactlyOneExists(
                hasTestTag("loaded_content"),
                timeoutMillis = 3000
            )

            composeTestRule.onAllNodesWithTag("user_item")
                .assertCountEquals(5)
        }
    }

    @Test
    fun showsError_whenFetchingIsUnsuccessful() {
        dependencies.responseQueue.add(
            MockResponse(
                "/2.2/users",
                { scope ->
                    scope.respondError(HttpStatusCode.Forbidden)
                }
            )
        )

        ActivityScenario.launch(PixelActivity::class.java).use {
            composeTestRule
                .onNodeWithTag("loading_content")
                .assertIsDisplayed()

            composeTestRule.waitUntilExactlyOneExists(
                hasTestTag("error_content"),
                timeoutMillis = 3000
            )

            composeTestRule
                .onNodeWithTag("error_content")
                .isDisplayed()
        }
    }

    @Test
    fun showsLoaded_whenFetchingIsSuccessful_afterUserTapsRetry() {
        dependencies.responseQueue.add(
            MockResponse(
                "/2.2/users",
                { scope ->
                    scope.respondError(HttpStatusCode.Forbidden)
                }
            )
        )
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

        ActivityScenario.launch(PixelActivity::class.java).use {
            composeTestRule.waitUntilExactlyOneExists(hasTestTag("try_again"), timeoutMillis = 5000)

            composeTestRule
                .onNodeWithTag("try_again")
                .performClick()

            composeTestRule.waitUntilExactlyOneExists(
                hasTestTag("loaded_content"),
                timeoutMillis = 3000
            )

            composeTestRule.onAllNodesWithTag("user_item")
                .assertCountEquals(5)
        }
    }

    @Test
    fun tappingAnUnfavoritedUser_willAddItAsFavorited() {
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

        ActivityScenario.launch(PixelActivity::class.java).use {
            composeTestRule
                .onNodeWithTag("loading_content")
                .assertIsDisplayed()

            composeTestRule.waitUntilExactlyOneExists(
                hasTestTag("loaded_content"),
                timeoutMillis = 3000
            )

            composeTestRule.onAllNodes(
                SemanticsMatcher.expectValue(
                    CustomTestTag,
                    "not_favorite_toggle"
                )
            )
                .assertCountEquals(5)

            composeTestRule
                .onNodeWithTag("11683_toggle")
                .performClick()


            composeTestRule.onAllNodes(
                SemanticsMatcher.expectValue(
                    CustomTestTag,
                    "not_favorite_toggle"
                )
            )
                .assertCountEquals(4)

            composeTestRule
                .onNodeWithTag("11683_favorite")
                .isDisplayed()
        }
    }
}