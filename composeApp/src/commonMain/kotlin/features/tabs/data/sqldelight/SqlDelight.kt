package features.tabs.data.sqldelight

import app.cash.sqldelight.db.SqlDriver
import com.chatapp.firebaseauthentication.Database
import comchatappfirebaseauthentication.DatabaseQueries

class SqlDelight( sqlDriver: SqlDriver) {
    private val database: Database = Database(sqlDriver)
    private val databaseQuery: DatabaseQueries = database.databaseQueries

    internal fun removeAllUsers() {
        databaseQuery.transaction {
            databaseQuery.removeAllUsers()
        }
    }
}