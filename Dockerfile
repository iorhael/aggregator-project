FROM maven:3.9-eclipse-temurin-17-alpine AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
COPY --from=builder /app/target/*.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
