FROM gradle:8.7-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle clean build --no-daemon

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/build/libs/csca-5028-module3-1.0.0.jar /app/app.jar
COPY .env /app/.env

ENTRYPOINT ["/bin/sh", "-c", "\
    echo 'Starting CSCA 5028 Data Collector...' && \
    if [ -f .env ]; then \
        echo 'Found .env file — loading environment variables...' && \
        set -a && \
        . ./.env && \
        set +a && \
        echo 'Environment variables loaded successfully.'; \
    else \
        echo 'No .env file found — using defaults from Dockerfile (if any).'; \
    fi && \
    echo 'Launching application...' && \
    exec java -jar /app/app.jar \
"]

LABEL description="CSCA 5028 Module 3: Data Collector"