package com.meowmakers.pixel

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
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
}