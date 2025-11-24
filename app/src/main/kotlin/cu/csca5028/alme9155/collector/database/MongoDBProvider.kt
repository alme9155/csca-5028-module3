package cu.csca5028.alme9155.collector.database

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.KMongo

object MongoDBProvider {
    private val uri: String = System.getenv("MONGODB_URI")
        ?: "mongodb://root:example_password_123@mongodb:27017/sentiment_db?authSource=admin"

    private val dbName: String = System.getenv("MONGODB_DB_NAME") ?: "sentiment_db"

    val client: MongoClient by lazy {
        KMongo.createClient(uri)
    }

    val database: MongoDatabase by lazy {
        client.getDatabase(dbName)
    }
}

