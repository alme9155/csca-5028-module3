FROM gradle:8.7-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle clean build --no-daemon

FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/build/libs/mvp-data-collector-1.0.jar /app/app.jar

ENV MONGODB_DB_NAME=sentiment_db
ENV MONGODB_URI="mongodb://root:password@mongodb:27017/sentiment_db?authSource=admin"
ENV API_DATA_COLLECTION=api_data

# RAPIDAPI_URL, RAPIDAPI_KEY, RAPIDAPI_HOST must be provided at runtime

ENTRYPOINT ["java", "-jar", "/app/app.jar"]

