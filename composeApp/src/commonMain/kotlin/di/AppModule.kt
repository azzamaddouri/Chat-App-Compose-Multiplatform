package di

import app.cash.sqldelight.db.SqlDriver
import features.authentication.data.firebase.FirebaseAuth
import features.authentication.data.repository.AuthRepositoryImp
import features.authentication.data.sqldelight.SqlDelight
import features.authentication.domain.AuthRepository

object AppModule{
    private lateinit var sqlDriver: SqlDriver

    fun setSqlDriver(driver: SqlDriver) {
        sqlDriver = driver
    }

    private val sqlDelight: SqlDelight by lazy { SqlDelight(sqlDriver) }

    val AuthRepository: AuthRepository by lazy {
        AuthRepositoryImp(firebaseAuth = FirebaseAuth(), sqlDelight)
    }
}