FROM maven:3.9-eclipse-temurin-17-alpine
WORKDIR /app
COPY ./docker-entrypoint.sh /docker-entrypoint.sh
RUN apk add --no-cache inotify-tools && \
    chmod +x /docker-entrypoint.sh

COPY pom.xml .
COPY src ./src

ENTRYPOINT ["/docker-entrypoint.sh"]
