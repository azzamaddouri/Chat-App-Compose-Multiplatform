package features.chat.data.firebaseDB

import features.authentication.data.firebase.Firebase
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.serialization.kotlinx.json.json

class FirebaseDatabase() {
    private val DATABASE_URL = Firebase.getDatabaseURL()
    private val currentUser = Firebase.getCurrentUser()
    private val httpClient = HttpClient() {
        install(ContentNegotiation) {
            json()
        }
        install(WebSockets)
    }

    suspend fun updateFirebaseDatabase(
        child: List<String>,
        parameter: HashMap<String,Any>) : String {
        val idToken = currentUser.idToken
        val childPath = child.joinToString("/")
        println("Path: ${childPath}")
        val responseBody = httpClient
            .put("${DATABASE_URL}/${childPath}.json?auth=${idToken}") {
                header("Content-Type", "application/json")
                setBody(parameter)
            }
        println("Response ${responseBody.bodyAsText()}")
        if (responseBody.status.value in 200..299) {
           return  responseBody.bodyAsText()
        } else {
           throw Exception("${responseBody.request.url} ${responseBody.bodyAsText()}")
        }
    }

    suspend fun readFirebaseDatabase(
        child: List<String>,
        query: String) : String {
        val idToken = currentUser.idToken
        val childPath = child.joinToString("/")
        val responseBody = httpClient
            .get("${DATABASE_URL}/${childPath}.json?auth=${idToken}&${query}") {
                header("Content-Type", "application/json")
            }
        if (responseBody.status.value in 200..299) {
           return responseBody.bodyAsText()
        } else {
           throw Exception(
                "${responseBody.request.url} ${responseBody.bodyAsText()}")
        }
    }
}