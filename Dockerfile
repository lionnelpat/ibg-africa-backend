# syntax=docker/dockerfile:1

# ---- Build stage ---------------------------------------------------------
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /workspace

COPY . .
# skip.installnodenpm/skip.npm: this backend image doesn't need the legacy
# JHipster-generated Angular app under src/main/webapp — the real frontend
# is a separate deployable built and served on its own.
RUN --mount=type=cache,target=/root/.m2 \
    chmod +x mvnw && ./mvnw -ntp package -Pprod -DskipTests -Dmaven.test.skip=true -Dskip.installnodenpm=true -Dskip.npm=true

# ---- Runtime stage --------------------------------------------------------
FROM eclipse-temurin:17-jre-jammy AS runtime
RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*

RUN groupadd -r spring && useradd -r -g spring spring
WORKDIR /app

COPY --from=build /workspace/target/*.jar app.jar
RUN chown spring:spring app.jar
USER spring

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JHIPSTER_SLEEP=0 \
    JAVA_OPTS=""

EXPOSE 8080

HEALTHCHECK --interval=10s --timeout=5s --start-period=60s --retries=10 \
    CMD curl -f http://localhost:8080/management/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar"]
