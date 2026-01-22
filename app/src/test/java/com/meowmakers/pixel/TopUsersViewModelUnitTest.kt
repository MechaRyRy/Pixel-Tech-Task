package com.meowmakers.pixel

import com.meowmakers.pixel.domain.entities.TopUser
import com.meowmakers.pixel.domain.entities.TopUsers
import com.meowmakers.pixel.domain.usecases.FakeFetchTopUsersUseCase
import com.meowmakers.pixel.domain.usecases.FakeObserveTopUsersUseCase
import com.meowmakers.pixel.domain.usecases.FakeToggleFavoriteUseCase
import com.meowmakers.pixel.presentation.screens.top_users.view_models.TopUsersScreenState
import com.meowmakers.pixel.presentation.screens.top_users.view_models.TopUsersViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class TopUsersViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: TopUsersViewModel

    private val observeFake = FakeObserveTopUsersUseCase()
    private val fetchFake = FakeFetchTopUsersUseCase()
    private val toggleFake = FakeToggleFavoriteUseCase()

    private val mockUsers =
        TopUsers(
            listOf(
                TopUser(
                    "1", "link", "Ryan", 1000, false
                )
            )
        )

    @Test
    fun `initial state is Loading`() = runTest {
        viewModel = TopUsersViewModel(observeFake, fetchFake, toggleFake)

        val currentState = viewModel.state.value
        assertTrue(currentState is TopUsersScreenState.Loading)
    }

    @Test
    fun `state updates to Loaded when observe emits success`() = runTest {
        viewModel = TopUsersViewModel(observeFake, fetchFake, toggleFake)


        observeFake.flow.emit(Result.success(mockUsers))
        runCurrent()

        val currentState = viewModel.state.value
        assertTrue(currentState is TopUsersScreenState.Loaded)
        assertEquals(mockUsers, (currentState as TopUsersScreenState.Loaded).users)
        assertEquals(1, fetchFake.fetchCalledCount)
    }

    @Test
    fun `state updates to Error when observe emits failure`() = runTest {
        viewModel = TopUsersViewModel(observeFake, fetchFake, toggleFake)

        val errorMessage = "Network Timeout"

        observeFake.flow.emit(Result.failure(Exception(errorMessage)))
        runCurrent()

        val currentState = viewModel.state.value
        assertTrue(currentState is TopUsersScreenState.Error)
        assertEquals(errorMessage, (currentState as TopUsersScreenState.Error).errorMessage)
    }

    @Test
    fun `retry sets Loading state and calls fetch`() = runTest {
        viewModel = TopUsersViewModel(observeFake, fetchFake, toggleFake)

        observeFake.flow.emit(Result.success(mockUsers))
        runCurrent()

        viewModel.retry()

        assertEquals(TopUsersScreenState.Loading, viewModel.state.value)
        assertEquals(2, fetchFake.fetchCalledCount)
    }

    @Test
    fun `toggleFavorite calls use case`() = runTest {
        viewModel = TopUsersViewModel(observeFake, fetchFake, toggleFake)

        viewModel.toggleFavorite("user_1", true)

        assertEquals(1, toggleFake.toggleFavoriteCalledCount)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule : TestWatcher() {
    private val testDispatcher = UnconfinedTestDispatcher()

    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}