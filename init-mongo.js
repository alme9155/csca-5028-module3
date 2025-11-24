db = db.getSiblingDB("sentiment_db");

db.createCollection("api_data");

db.api_data.createIndex(
  { movie_id: 1 },
  { unique: true, name: "uniq_raw_per_movie" }
);
