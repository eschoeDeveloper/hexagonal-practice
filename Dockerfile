FROM eclipse-temurin:23-jdk AS builder
WORKDIR /build
COPY gradlew settings.gradle.kts build.gradle.kts ./
COPY gradle ./gradle
RUN ./gradlew --no-daemon dependencies || true
COPY src ./src
RUN ./gradlew --no-daemon bootWar -x test

FROM eclipse-temurin:23-jre
WORKDIR /app
COPY --from=builder /build/build/libs/*.war app.war
EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=prod
ENTRYPOINT ["java", "-jar", "/app/app.war"]
