package features.tabs.domain

interface UserRepository {
    suspend fun logout():Result<Unit>
}