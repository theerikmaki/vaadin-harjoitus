# --- Build stage ---
FROM eclipse-temurin:23-jdk AS build

WORKDIR /app

# Copy all project files
COPY . .

# Build the application (skip tests if desired)
RUN ./mvnw clean package -Pproduction -DskipTests

# --- Runtime stage ---
FROM eclipse-temurin:23-jdk

RUN apt-get update && apt-get install -y default-mysql-client && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy only the built jar
COPY --from=build /app/target/*.jar app.jar

COPY wait-for-db.sh .

RUN chmod +x wait-for-db.sh

# Expose port (your app uses 8080 by default)
EXPOSE 8080

# Run the app
ENTRYPOINT ["./wait-for-db.sh", "db", "java", "-jar", "app.jar"]
