print("Running custom init script for sentiment_db...");

db = db.getSiblingDB("sentiment_db");  // switch to sentiment_db

db.api_data.createIndex({ "movie_id": 1 }, { unique: true });

db.api_data.insertOne({
  _id: "init",
  message: "sentiment_db initialized successfully",
  initializedAt: new Date()
});

print("Custom init complete. Ready for data collection.");