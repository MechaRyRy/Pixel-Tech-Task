package com.meowmakers.pixel.domain.usecases

class FakeFetchTopUsersUseCase : FetchTopUsersUseCase {
    var fetchCalledCount = 0

    override suspend fun fetch() {
        fetchCalledCount++
    }
}