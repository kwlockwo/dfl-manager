# Multi-stage Dockerfile for DFL Manager Monorepo
# Supports building and running both scheduler and web modules

# =============================================================================
# Build stage - Builds all modules (common, scheduler, web)
# =============================================================================
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app

# Copy Maven wrapper and root POM first for better layer caching
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Copy module POMs
COPY common/pom.xml common/
COPY scheduler/pom.xml scheduler/
COPY web/pom.xml web/

# Download dependencies (cached unless POM changes)
RUN ./mvnw dependency:go-offline -B

# Copy source code for all modules
COPY common/src common/src
COPY scheduler/src scheduler/src
COPY scheduler/bin scheduler/bin
COPY web/src web/src

# Build all modules (common is built first due to dependency order)
# Skip tests in Docker build for faster builds
RUN ./mvnw clean package -DskipTests -B

# Verify JARs were created
RUN ls -la scheduler/target/dfl-manager-scheduler.jar && \
    ls -la web/target/dfl-manager-web.jar

# =============================================================================
# Scheduler runtime - Includes Chrome/Chromium for Selenium web scraping
# =============================================================================
FROM eclipse-temurin:21-jre-jammy AS scheduler
WORKDIR /app

# Install Chrome/Chromium based on architecture
# amd64: Google Chrome (official)
# arm64: Chromium (open source)
RUN apt-get update && \
    apt-get install -y wget gnupg ca-certificates && \
    ARCH=$(dpkg --print-architecture) && \
    if [ "$ARCH" = "amd64" ]; then \
        wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | gpg --dearmor -o /usr/share/keyrings/google-chrome.gpg && \
        echo "deb [signed-by=/usr/share/keyrings/google-chrome.gpg] http://dl.google.com/linux/chrome/deb/ stable main" > /etc/apt/sources.list.d/google-chrome.list && \
        apt-get update && \
        apt-get install -y google-chrome-stable; \
    else \
        apt-get install -y chromium-browser chromium-chromedriver; \
    fi && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Copy scheduler JAR from build stage
COPY --from=build /app/scheduler/target/dfl-manager-scheduler.jar /app/dfl-manager-scheduler.jar

# Copy shell scripts for CLI execution
COPY --from=build /app/scheduler/bin/*.sh /app/bin/
RUN chmod +x /app/bin/*.sh

# Create SSH directory for deployment scripts
RUN mkdir -p "$HOME"/.ssh && chmod 700 "$HOME"/.ssh

# Environment variables
ENV APP_HOME=/app
ENV JAVA_OPTS=""

# Scheduler can run on port 8080 for health checks
EXPOSE 8080

# Default entrypoint - runs the Spring Boot application
# To run as CLI tool, override with: --handler=<handler-name>
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar dfl-manager-scheduler.jar \"$@\"", "--"]

# =============================================================================
# Web runtime - Lightweight image for web application
# =============================================================================
FROM eclipse-temurin:21-jre-jammy AS web
WORKDIR /app

# Copy web JAR from build stage
COPY --from=build /app/web/target/dfl-manager-web.jar /app/dfl-manager-web.jar

# Environment variables
ENV JAVA_OPTS=""
ENV PORT=8080

# Web application port
EXPOSE 8080

# Entrypoint for web application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar dfl-manager-web.jar"]
