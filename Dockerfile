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

# Install Chrome in a single layer with cleanup
RUN apt-get update && \
    apt-get install -y wget gnupg && \
    wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add - && \
    echo "deb http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google.list && \
    apt-get update && \
    apt-get install -y google-chrome-stable && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Copy application artifacts
COPY --from=build /app/target/dflmngr.jar target/
COPY --from=build /app/target/dependency/*.jar target/dependency/
COPY bin/*.sh bin/

# Create SSH directory
RUN mkdir -p "$HOME"/.ssh && chmod 700 "$HOME"/.ssh

CMD ["java", "-classpath", "/app/target/dflmngr.jar:/app/target/dependency/*", "net.dflmngr.scheduler.JobScheduler"]