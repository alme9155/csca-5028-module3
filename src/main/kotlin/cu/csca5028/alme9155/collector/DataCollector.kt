package cu.csca5028.alme9155.collector

import cu.csca5028.alme9155.collector.service.ApiDataCollector
import cu.csca5028.alme9155.collector.database.MongoDBAdapter
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    println("CSCA 5028 Module 3 – Standalone Data Collector\n")
    println("Starting data collection pipeline ...\n")

    var count = 0
    
    try {
        val fetchedCount = ApiDataCollector.fetchDataFromAPI()
        //val reviews = ApiDataCollector.getFetchedData()
        //count = MongoDBAdapter.upsertMoviesReviews(reviews)
    } catch (ex: Exception) {
        println("Exception found during data collection pipeline: ${ex.message}")
        ex.printStackTrace()
    }

    println("... Data collection pipeline complete.")
    println("Total record persisted into database: $count")
}

