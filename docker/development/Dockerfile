FROM openjdk:11-slim AS builder

WORKDIR /app

COPY mvnw pom.xml docker ./
COPY .mvn .mvn
COPY src src

RUN ./mvnw package -DskipTests

RUN mkdir -p target/dependency && \
    cd target/dependency && \
    jar -xf ../*.jar

FROM openjdk:11-slim

ENV APP_ENV "development"

ARG DEPENDENCY=/app/target/dependency

COPY --from=builder /app/development /app/docker/development
COPY --from=builder ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=builder ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=builder ${DEPENDENCY}/BOOT-INF/classes /app

ENTRYPOINT ["java", "-cp", "app:app/lib/*", "me.avmnusng.counter.CounterApplication"]
