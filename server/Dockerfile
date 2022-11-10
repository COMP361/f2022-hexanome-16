FROM maven:3.8-eclipse-temurin-17-focal AS build
WORKDIR /app

# Copy the source code
COPY src src
COPY pom.xml .
COPY google_checks.xml .

# Build the application
RUN mvn install -DskipTests

FROM eclipse-temurin:17-jre-focal AS runtime
EXPOSE 4243
WORKDIR /app
COPY --from=build /app/target/server-0.0.1-SNAPSHOT.jar .
ENTRYPOINT ["java","-jar","server-0.0.1-SNAPSHOT.jar"]
