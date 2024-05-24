package features.authentication.data.firebase

class Firebase {

    companion object {
        private lateinit var API_KEY: String
        private lateinit var DATABASE_URL: String
        private lateinit var _currentUser: FirebaseUser

        fun getAPIKey() = API_KEY
        fun getDatabaseURL() = DATABASE_URL
        fun getCurrentUser() = _currentUser
    }

    fun initialize(apiKey: String, databaseUrl: String) {
        API_KEY = apiKey
        DATABASE_URL = databaseUrl
    }

    fun setCurrentUser(currentUser: FirebaseUser) {
        _currentUser = currentUser
    }
}