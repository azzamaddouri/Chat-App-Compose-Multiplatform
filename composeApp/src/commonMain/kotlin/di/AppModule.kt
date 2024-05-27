package di

import app.cash.sqldelight.db.SqlDriver
import features.authentication.data.firebase.FirebaseAuth
import features.authentication.data.repository.AuthRepositoryImp
import features.authentication.domain.AuthRepository
import features.tabs.data.repository.UserRepositoryImp
import features.tabs.domain.UserRepository

object AppModule{
    private lateinit var sqlDriver: SqlDriver

    fun setSqlDriver(driver: SqlDriver) {
        sqlDriver = driver
    }

    private val sqlDelightAuth: features.authentication.data.sqldelight.SqlDelight
    by lazy { features.authentication.data.sqldelight.SqlDelight(sqlDriver) }

    val AuthRepository: AuthRepository by lazy {
        AuthRepositoryImp(firebaseAuth = FirebaseAuth(), sqlDelightAuth)
    }

    private val sqlDelightUser: features.tabs.data.sqldelight.SqlDelight
    by lazy { features.tabs.data.sqldelight.SqlDelight(sqlDriver) }

    val UserRepository: UserRepository by lazy {
        UserRepositoryImp(sqlDelightUser)
    }
}