package features.authentication.data.sqldelight

import app.cash.sqldelight.db.SqlDriver
import com.chatapp.firebaseauthentication.Database
import comchatappfirebaseauthentication.DatabaseQueries
import comchatappfirebaseauthentication.User
import features.authentication.domain.AuthResponse

class SqlDelight( sqlDriver: SqlDriver) {
    private val database: Database = Database(sqlDriver)
    private val databaseQuery: DatabaseQueries = database.databaseQueries
    internal fun storeUserDetails(response: AuthResponse) {
        databaseQuery.insertUser(
            response.idToken, response.email, response.refreshToken,
            response.email, response.localId
        )
    }
    internal fun getAllUsers(): List<User> {
        return databaseQuery.selectAllUsers().executeAsList()
    }

}