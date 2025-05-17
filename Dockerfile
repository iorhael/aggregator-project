FROM maven:3-eclipse-temurin-24-alpine AS builder

WORKDIR /app

COPY pom.xml ./pom.xml
COPY src ./src

RUN mvn clean package --no-transfer-progress


FROM eclipse-temurin:21-jre-alpine AS final

COPY --from=builder /app/target/*.jar app.jar

CMD ["java", "-jar", "/app.jar"]
