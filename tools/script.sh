#!/bin/sh
./gradlew bootRun --args="--server.port=$ENDPOINT_PORT --spring.profiles.active=dev"