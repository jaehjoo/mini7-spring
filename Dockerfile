FROM openjdk:17-jdk-slim

WORKDIR /app/src/mini7

COPY ./mini7/ /app/src/mini7/
COPY ./tools/script.sh /usr/local/bin/script.sh

RUN apt-get update && apt-get install dumb-init

RUN chmod +x ./gradlew
RUN chmod +x /usr/local/bin/script.sh

EXPOSE $ENDPOINT_PORT

ENTRYPOINT ["/usr/bin/dumb-init", "--", "sh", "/usr/local/bin/script.sh"]