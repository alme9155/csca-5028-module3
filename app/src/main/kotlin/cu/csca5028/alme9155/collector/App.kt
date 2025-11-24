package cu.csca5028.alme9155.collector

import cu.csca5028.alme9155.collector.database.MongoDBProvider
import cu.csca5028.alme9155.collector.service.MovieReviewsCollectorService
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val apiDataCollectionName =
        System.getenv("API_DATA_COLLECTION") ?: "api_data"

    val db = MongoProvider.database
    val service = MovieReviewsCollectorService(db, apiDataCollectionName)

    println("Starting data collection run...")
    val count = try {
        service.collectOnce()
    } catch (e: Exception) {
        e.printStackTrace()
        -1
    }

    if (count >= 0) {
        println("Collected/updated $count API movie records in '$apiDataCollectionName'")
    } else {
        println("Data collection failed.")
    }
}

