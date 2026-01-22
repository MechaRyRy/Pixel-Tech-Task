package com.meowmakers.pixel.domain.usecases

import com.meowmakers.pixel.domain.entities.TopUsers
import kotlinx.coroutines.flow.MutableSharedFlow

class FakeObserveTopUsersUseCase : ObserveTopUsersUseCase {
    val flow = MutableSharedFlow<Result<TopUsers>>()

    override suspend fun observe() = flow
}