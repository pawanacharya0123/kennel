# Step 1: Build the app using Maven and Java 17
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Step 2: Run the packaged JAR using Java 17 runtime
# FROM eclipse-temurin:17-jdk
FROM openjdk:17-jdk-slim
WORKDIR /app

RUN apt-get update && apt-get install -y netcat && rm -rf /var/lib/apt/lists/*

COPY --from=build /app/target/*.jar app.jar
# EXPOSE 8080
COPY wait-for-it.sh .
RUN chmod +x wait-for-it.sh
ENTRYPOINT ["./wait-for-it.sh", "mysql", "3306", "java", "-jar", "app.jar"]

# ENTRYPOINT ["java", "-jar", "app.jar"]

