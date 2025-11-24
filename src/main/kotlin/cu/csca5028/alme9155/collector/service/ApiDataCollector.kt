package cu.csca5028.alme9155.collector.service

import cu.csca5028.alme9155.collector.database.RawMovieReview
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.*
import org.bson.Document

object ApiDataCollector {
    private val client = HttpClient {
        install(ContentNegotiation) { json() }
    }

    private val apiUrl: String = System.getenv("RAPID_API_URL")
        ?: error("RAPID_API_URL env var is required")

    private val apiKey: String = System.getenv("RAPID_API_KEY")
        ?: error("RAPID_API_KEY env var is required")

    private val apiHost: String = System.getenv("RAPID_API_HOST") 
        ?: error("RAPID_API_HOST env var is required")

    private var lastFetchedMovies: List<RawMovieReview> = emptyList()

    suspend fun fetchDataFromAPI(): Int = withContext(Dispatchers.IO) {
        println("Fetching API Data from: $apiUrl ...")

        val response: String = client.get(apiUrl) {
            headers {
                append("x-rapidapi-key", apiKey)
                append("x-rapidapi-host", apiHost)
            }
        }.body()

        val json = Json.parseToJsonElement(response)
        val array = json.jsonObject["result"]?.jsonArray ?: json.jsonArray

        val movies = array.mapNotNull { elem ->
            val obj = elem.jsonObject
            val title = obj["title"]?.jsonPrimitive?.contentOrNull
            val year = obj["year"]?.jsonPrimitive?.contentOrNull
            val id = obj["id"]?.jsonPrimitive?.contentOrNull
                ?: obj["movie_id"]?.jsonPrimitive?.contentOrNull

            val movieId = id ?: listOfNotNull(title, year).joinToString("_")
                .takeIf { it.isNotBlank() } ?: return@mapNotNull null

            val doc = Document.parse(obj.toString())
            RawMovieReview(movieId = movieId, raw = doc)
        }

        lastFetchedMovies = movies
        println("... Successfully fetched ${movies.size} records from API end point.")
        movies.size
    }

    fun getFetchedData(): List<RawMovieReview> = lastFetchedMovies.toList()

    fun close() {
        client.close()
    }
}