# CSCA-5028 Applications of Software Architecture for Big Data - module

## *Goal*: 
* The goal for this peer review assignment is demonstrate that you can fetch data from an API endpoint and store that data in a database. 

## Build Procedure (execute at the project root)
* ./gradlew clean build
* docker build --no-cache
* docker compose up -d

## To execute the data collector
* docker compose run --rm data-collector
Or
* docker run --rm --network csca-5028-module3_default data-collector

## To examine the database, you can review the collection in mongodb while the instance is still running ( docker compose -d):
* use sentiment_db
* db.api_data.countDocuments()
* db.api_data.findOne().pretty()