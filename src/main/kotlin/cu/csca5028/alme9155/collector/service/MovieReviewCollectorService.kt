package cu.csca5028.alme9155.collector.service

import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.ReplaceOptions
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.*
import org.bson.Document


data class RawMovieReview(
    val movieId: String,
    val raw: Document
)

class MovieReviewsCollectorService(
    database: MongoDatabase,
    apiDataCollectionName: String
) {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    private val collection: MongoCollection<Document> =
        database.getCollection(apiDataCollectionName)

    private val apiUrl: String = System.getenv("RAPIDAPI_URL")
        ?: error("RAPIDAPI_URL env var is required")

    private val rapidApiKey: String = System.getenv("RAPIDAPI_KEY")
        ?: error("RAPIDAPI_KEY env var is required")

    private val rapidApiHost: String = System.getenv("RAPIDAPI_HOST") ?: ""

    suspend fun collectOnce(): Int = withContext(Dispatchers.IO) {
        // 1) Call external API
        val responseText: String = client.get(apiUrl) {
            headers {
                append("X-RapidAPI-Key", rapidApiKey)
                if (rapidApiHost.isNotBlank()) {
                    append("X-RapidAPI-Host", rapidApiHost)
                }
            }
        }.body()

        // 2) Parse JSON; support { result: [...] } or plain array
        val jsonElement = Json.parseToJsonElement(responseText)
        val resultArray = jsonElement.jsonObject["result"]?.jsonArray
            ?: jsonElement.jsonArray

        // 3) Map to RawMovieReview with stable movieId
        val movies = resultArray.mapNotNull { elem ->
            val obj = elem.jsonObject

            val title = obj["title"]?.jsonPrimitive?.contentOrNull
            val year = obj["year"]?.jsonPrimitive?.contentOrNull
            val id = obj["id"]?.jsonPrimitive?.contentOrNull
                ?: obj["movie_id"]?.jsonPrimitive?.contentOrNull

            val movieId = id
                ?: listOfNotNull(title, year).joinToString("_")
                ?: return@mapNotNull null

            val doc = Document.parse(obj.toString())
            RawMovieReview(movieId = movieId, raw = doc)
        }

        // 4) Upsert into api_data (unique per movie_id)
        var upsertedCount = 0
        val opts = ReplaceOptions().upsert(true)

        for (m in movies) {
            collection.replaceOne(
                Document("movie_id", m.movieId),
                Document(m.raw).apply {
                    put("movie_id", m.movieId)
                },
                opts
            )
            upsertedCount++
        }

        upsertedCount
    }
}

