#!/bin/sh

echo "RUN_NOTHING_ON_LOCAL=$RUN_NOTHING_ON_LOCAL"

run_nothing="${RUN_NOTHING_ON_LOCAL}"

if [ "$run_nothing" = "1" ]; then
    echo "Container will stay alive and do nothing"
    tail -f /dev/null
else
    echo "Running Maven build"
    mvn clean package || exit 1

    echo "Starting Spring Boot"
    mvn spring-boot:run || exit 1
fi