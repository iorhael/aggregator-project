FROM maven:3.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

COPY pom.xml ./pom.xml
COPY src ./src

RUN mvn clean package


FROM eclipse-temurin:21-jre-alpine AS final

COPY --from=builder /app/target/*.jar app.jar

CMD ["java", "-jar", "/app.jar"]
