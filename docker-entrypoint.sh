#!/bin/sh

while inotifywait -re modify /app/src/main/;
do
  mvn compile
done >/dev/null 2>&1 &

mvn spring-boot:run \
  -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"
