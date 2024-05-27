package features.tabs.data.repository

import features.tabs.data.sqldelight.SqlDelight
import features.tabs.domain.UserRepository

class UserRepositoryImp (private val sqlDelight: SqlDelight) : UserRepository{
    override suspend fun logout(): Result<Unit> {
        return runCatching {
            sqlDelight.removeAllUsers()
        }
    }
}