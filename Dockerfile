# Build stage
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app

# Copy Maven wrapper and POM first for better layer caching
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Download dependencies (cached unless POM changes)
RUN ./mvnw -T 2 dependency:go-offline

# Copy source and build
COPY src src
RUN ./mvnw -T 2 clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Install Chrome 143 to match selenium-devtools-v143 and download OTel Java agent
RUN apt-get update && \
    apt-get install -y wget gnupg && \
    wget -q "https://dl.google.com/linux/chrome/deb/pool/main/g/google-chrome-stable/google-chrome-stable_143.0.7499.192-1_amd64.deb" -O /tmp/chrome.deb && \
    apt-get install -y /tmp/chrome.deb && \
    rm /tmp/chrome.deb && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/* && \
    wget -q "https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v2.25.0/opentelemetry-javaagent.jar" \
        -O /app/opentelemetry-javaagent.jar

# Copy application artifacts
COPY --from=build /app/target/dflmngr.jar target/
COPY --from=build /app/target/dependency/*.jar target/dependency/
COPY bin/*.sh bin/

# Create SSH directory
RUN mkdir -p "$HOME"/.ssh && chmod 700 "$HOME"/.ssh

ENV OTEL_SERVICE_NAME=dfl-manager-worker
ENV OTEL_LOGS_EXPORTER=none
ENV OTEL_METRICS_EXPORTER=otlp
ENV OTEL_EXPORTER_OTLP_ENDPOINT=http://localhost:4317

CMD ["java", \
     "-javaagent:/app/opentelemetry-javaagent.jar", \
     "-classpath", "/app/target/dflmngr.jar:/app/target/dependency/*", \
     "net.dflmngr.scheduler.JobScheduler"]