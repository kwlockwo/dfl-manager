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

# Install Chrome 143 to match selenium-devtools-v143
RUN apt-get update && \
    apt-get install -y wget gnupg && \
    wget -q "https://dl.google.com/linux/chrome/deb/pool/main/g/google-chrome-stable/google-chrome-stable_143.0.7499.192-1_amd64.deb" -O /tmp/chrome.deb && \
    apt-get install -y /tmp/chrome.deb && \
    rm /tmp/chrome.deb && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Copy application artifacts
COPY --from=build /app/target/dflmngr.jar target/
COPY --from=build /app/target/dependency/*.jar target/dependency/
COPY bin/*.sh bin/

# Create SSH directory
RUN mkdir -p "$HOME"/.ssh && chmod 700 "$HOME"/.ssh

CMD ["java", "-classpath", "/app/target/dflmngr.jar:/app/target/dependency/*", "net.dflmngr.scheduler.JobScheduler"]