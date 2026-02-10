# DFL Manager Deployment Guide

This guide covers building, testing, and deploying the DFL Manager application using Docker. The application is a Spring Boot monorepo with two main modules: **scheduler** (batch processing with Quartz) and **web** (Thymeleaf web application).

## Table of Contents

1. [Architecture Overview](#architecture-overview)
2. [Prerequisites](#prerequisites)
3. [Local Development with Docker Compose](#local-development-with-docker-compose)
4. [Building Docker Images](#building-docker-images)
5. [Running Individual Containers](#running-individual-containers)
6. [Environment Variables](#environment-variables)
7. [Production Deployment](#production-deployment)
8. [Database Migrations](#database-migrations)
9. [Troubleshooting](#troubleshooting)

## Architecture Overview

The DFL Manager uses a multi-stage Dockerfile to build both modules:

```
┌─────────────────────────────────────────┐
│         Build Stage (JDK 21)            │
│  Builds: common, scheduler, web         │
└─────────────────┬───────────────────────┘
                  │
         ┌────────┴────────┐
         │                 │
┌────────▼────────┐  ┌────▼──────────┐
│   Scheduler     │  │     Web       │
│   (JRE 21)      │  │   (JRE 21)    │
│  + Chrome       │  │   Lightweight │
│  + Selenium     │  │               │
└─────────────────┘  └───────────────┘
```

### Modules

- **common**: Shared entities, services, and repositories used by both scheduler and web
- **scheduler**: Batch processing, Quartz scheduling, CLI tools, web scraping with Selenium
- **web**: Spring Boot web application with Thymeleaf templates

## Prerequisites

### For Local Development

- Docker 24+ and Docker Compose 2+
- (Optional) Java 21 JDK for local builds without Docker

### For Production

- Docker 24+ on the deployment server
- PostgreSQL 16 database (managed separately or via docker-compose)
- At least 3GB RAM for scheduler (due to Chrome/Selenium)
- At least 1GB RAM for web

## Local Development with Docker Compose

### Quick Start

1. **Start all services** (PostgreSQL, scheduler, web):

   ```bash
   docker compose up -d
   ```

2. **View logs**:

   ```bash
   # All services
   docker compose logs -f

   # Specific service
   docker compose logs -f web
   docker compose logs -f scheduler
   ```

3. **Access the application**:

   - Web UI: http://localhost:8080
   - PostgreSQL: `localhost:5432`

4. **Stop all services**:

   ```bash
   docker compose down
   ```

5. **Stop and remove volumes** (clean slate):

   ```bash
   docker compose down -v
   ```

### Development Workflow

#### Rebuild after code changes:

```bash
# Rebuild and restart specific service
docker compose up -d --build web

# Rebuild all services
docker compose up -d --build
```

#### Run scheduler as CLI tool:

To run the scheduler in CLI mode instead of as a continuous scheduler, modify the `docker-compose.yml`:

```yaml
scheduler:
  # ... existing configuration ...
  command: ["--handler=afl-fixture-loader", "--round=1"]
```

Or run directly:

```bash
docker compose run --rm scheduler --handler=afl-fixture-loader --round=1
```

#### Access database:

```bash
docker compose exec postgres psql -U dflmngr -d dflmngrdb
```

## Building Docker Images

### Build All Images

```bash
# Build scheduler image
docker build --target scheduler -t dfl-manager:scheduler .

# Build web image
docker build --target web -t dfl-manager:web .

# Build both with specific tags
docker build --target scheduler -t dfl-manager:scheduler-v1.0 .
docker build --target web -t dfl-manager:web-v1.0 .
```

### Build Options

#### Skip tests for faster builds:

```bash
# Tests are already skipped in Dockerfile with -DskipTests
docker build --target scheduler -t dfl-manager:scheduler .
```

#### Use BuildKit for faster builds:

```bash
DOCKER_BUILDKIT=1 docker build --target scheduler -t dfl-manager:scheduler .
```

#### Multi-platform builds:

```bash
docker buildx build --platform linux/amd64,linux/arm64 \
  --target scheduler -t dfl-manager:scheduler .
```

### Verify Build

```bash
# Check image sizes
docker images | grep dfl-manager

# Verify Chrome is installed in scheduler
docker run --rm dfl-manager:scheduler which google-chrome

# Check JAR files
docker run --rm dfl-manager:scheduler ls -lh /app/*.jar
docker run --rm dfl-manager:web ls -lh /app/*.jar
```

## Running Individual Containers

### Scheduler

#### Run as continuous scheduler (default):

```bash
docker run -d \
  --name dfl-scheduler \
  -e DATABASE_URL=jdbc:postgresql://host.docker.internal:5432/dflmngrdb \
  -e DATABASE_USER=dflmngr \
  -e DATABASE_PASSWORD=dflmngr \
  -e JAVA_OPTS="-Xms512m -Xmx2g" \
  dfl-manager:scheduler
```

#### Run as CLI tool:

```bash
# Run a specific handler
docker run --rm \
  -e DATABASE_URL=jdbc:postgresql://host.docker.internal:5432/dflmngrdb \
  -e DATABASE_USER=dflmngr \
  -e DATABASE_PASSWORD=dflmngr \
  dfl-manager:scheduler \
  --handler=afl-fixture-loader --round=1

# Use shell scripts (mounted volume)
docker run --rm \
  -v $(pwd)/scheduler/bin:/app/bin:ro \
  -e DATABASE_URL=jdbc:postgresql://host.docker.internal:5432/dflmngrdb \
  -e DATABASE_USER=dflmngr \
  -e DATABASE_PASSWORD=dflmngr \
  dfl-manager:scheduler \
  /app/bin/run_afl_fixture_download.sh
```

### Web

```bash
docker run -d \
  --name dfl-web \
  -p 8080:8080 \
  -e JDBC_DATABASE_URL=jdbc:postgresql://host.docker.internal:5432/dflmngrdb \
  -e JDBC_DATABASE_USERNAME=dflmngr \
  -e JDBC_DATABASE_PASSWORD=dflmngr \
  -e JAVA_OPTS="-Xms256m -Xmx1g" \
  dfl-manager:web
```

## Environment Variables

### Common Variables (Both Modules)

| Variable | Description | Default |
|----------|-------------|---------|
| `JAVA_OPTS` | JVM options | `""` |
| `SPRING_PROFILES_ACTIVE` | Spring Boot profile | `default` |

### Scheduler Variables

| Variable | Description | Required | Default |
|----------|-------------|----------|---------|
| `DATABASE_URL` | JDBC connection string | Yes | `jdbc:postgresql://localhost:5432/dflmngrdb` |
| `DATABASE_USER` | Database username | Yes | `dflmngr` |
| `DATABASE_PASSWORD` | Database password | Yes | `dflmngr` |
| `APP_HOME` | Application home directory | No | `/app` |
| `CHROME_DRIVER_PATH` | Path to Chrome binary | No | `/usr/bin/google-chrome` |

### Web Variables

| Variable | Description | Required | Default |
|----------|-------------|----------|---------|
| `JDBC_DATABASE_URL` | JDBC connection string | Yes | - |
| `JDBC_DATABASE_USERNAME` | Database username | Yes | - |
| `JDBC_DATABASE_PASSWORD` | Database password | Yes | - |
| `PORT` | Web server port | No | `8080` |
| `DFLMNGR_LOG_LEVEL` | Application log level | No | `info` |

### Recommended JVM Options

#### Scheduler (memory-intensive due to Chrome/Selenium):
```bash
JAVA_OPTS="-Xms512m -Xmx2g -XX:+UseG1GC -XX:MaxGCPauseMillis=200"
```

#### Web (moderate load):
```bash
JAVA_OPTS="-Xms256m -Xmx1g -XX:+UseG1GC"
```

## Production Deployment

### Option 1: Docker Compose (Recommended for Single Server)

1. **Create production `docker-compose.prod.yml`**:

   ```yaml
   version: '3.8'

   services:
     scheduler:
       image: dfl-manager:scheduler-v1.0
       restart: always
       environment:
         DATABASE_URL: ${DATABASE_URL}
         DATABASE_USER: ${DATABASE_USER}
         DATABASE_PASSWORD: ${DATABASE_PASSWORD}
         JAVA_OPTS: "-Xms512m -Xmx2g -XX:+UseG1GC"
       env_file:
         - .env.production

     web:
       image: dfl-manager:web-v1.0
       restart: always
       ports:
         - "80:8080"
       environment:
         JDBC_DATABASE_URL: ${DATABASE_URL}
         JDBC_DATABASE_USERNAME: ${DATABASE_USER}
         JDBC_DATABASE_PASSWORD: ${DATABASE_PASSWORD}
         JAVA_OPTS: "-Xms256m -Xmx1g -XX:+UseG1GC"
       env_file:
         - .env.production
   ```

2. **Create `.env.production`**:

   ```bash
   DATABASE_URL=jdbc:postgresql://prod-db-host:5432/dflmngrdb
   DATABASE_USER=dflmngr
   DATABASE_PASSWORD=<secure-password>
   SPRING_PROFILES_ACTIVE=production
   ```

3. **Deploy**:

   ```bash
   docker compose -f docker-compose.prod.yml up -d
   ```

### Option 2: Individual Containers (Kubernetes, AWS ECS, etc.)

Deploy scheduler and web as separate containers with their respective configurations.

#### Example Kubernetes Deployment:

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: dfl-web
spec:
  replicas: 2
  selector:
    matchLabels:
      app: dfl-web
  template:
    metadata:
      labels:
        app: dfl-web
    spec:
      containers:
      - name: web
        image: dfl-manager:web-v1.0
        ports:
        - containerPort: 8080
        env:
        - name: JDBC_DATABASE_URL
          valueFrom:
            secretKeyRef:
              name: db-credentials
              key: url
        resources:
          requests:
            memory: "512Mi"
            cpu: "250m"
          limits:
            memory: "1Gi"
            cpu: "500m"
```

### Health Checks

#### Scheduler:
The scheduler doesn't expose HTTP endpoints by default. Monitor via:
- Container health status
- Log monitoring
- Database job status tables

#### Web:
Spring Boot Actuator health endpoint:
```bash
curl http://localhost:8080/actuator/health
```

Add to `application.yml` if not present:
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info
```

## Database Migrations

### Strategy

The DFL Manager uses JPA with `ddl-auto: validate` (does not auto-create/update schema).

#### For Production:

1. **Manual migrations**: Use tools like Flyway or Liquibase (not currently configured)
2. **Schema initialization**: Manually create schema using SQL scripts
3. **JPA validation**: Application validates that schema matches entities on startup

### Schema Setup

```bash
# Connect to database
psql -h localhost -U dflmngr -d dflmngrdb

# Import schema if you have schema.sql
\i /path/to/schema.sql
```

### Migration Workflow

1. **Test migrations locally**:
   ```bash
   docker compose up -d postgres
   # Apply schema changes
   docker compose up web
   # Check for JPA validation errors
   ```

2. **Apply to production**:
   ```bash
   # Backup database first
   pg_dump -h prod-host -U dflmngr dflmngrdb > backup.sql

   # Apply schema changes
   psql -h prod-host -U dflmngr -d dflmngrdb < migration.sql

   # Deploy new application version
   docker compose -f docker-compose.prod.yml up -d
   ```

## Troubleshooting

### Common Issues

#### 1. Build fails with "Cannot resolve dependency"

**Problem**: Maven can't download dependencies.

**Solution**:
```bash
# Clear Docker build cache
docker builder prune -a

# Rebuild with --no-cache
docker build --no-cache --target scheduler -t dfl-manager:scheduler .
```

#### 2. Scheduler fails with Chrome/Selenium errors

**Problem**: Chrome not installed or incompatible version.

**Solutions**:
```bash
# Verify Chrome is installed
docker run --rm dfl-manager:scheduler google-chrome --version

# Check Selenium logs
docker logs dfl-scheduler

# Run with verbose logging
docker run --rm \
  -e JAVA_OPTS="-Dselenium.LOGGER.level=FINE" \
  dfl-manager:scheduler
```

#### 3. Database connection refused

**Problem**: Container can't reach database.

**Solutions**:
```bash
# For Docker Desktop on Mac/Windows, use host.docker.internal
DATABASE_URL=jdbc:postgresql://host.docker.internal:5432/dflmngrdb

# For Linux, use host network or bridge IP
docker run --network host ...

# Check database is accessible
docker compose exec scheduler ping postgres
```

#### 4. Web application won't start

**Problem**: Missing environment variables or port conflict.

**Solutions**:
```bash
# Check logs
docker logs dfl-web

# Verify environment variables are set
docker inspect dfl-web | grep -A 20 Env

# Check port availability
lsof -i :8080

# Use different port
docker run -p 8081:8080 ... dfl-manager:web
```

#### 5. Out of memory errors

**Problem**: JVM running out of heap space.

**Solutions**:
```bash
# Increase heap size for scheduler
docker run -e JAVA_OPTS="-Xms1g -Xmx4g" ...

# Monitor memory usage
docker stats dfl-scheduler dfl-web

# Check container resource limits
docker inspect dfl-scheduler | grep -A 10 Memory
```

### Debugging Tips

#### Access container shell:
```bash
docker exec -it dfl-scheduler bash
docker exec -it dfl-web bash
```

#### View application logs:
```bash
docker logs -f dfl-scheduler
docker logs -f dfl-web --tail 100
```

#### Test database connection:
```bash
docker compose exec scheduler \
  java -cp /app/dfl-manager-scheduler.jar \
  org.springframework.boot.loader.JarLauncher \
  --spring.jpa.hibernate.ddl-auto=validate
```

#### Check network connectivity:
```bash
# From scheduler to database
docker compose exec scheduler ping postgres

# From host to web
curl -v http://localhost:8080
```

## CI/CD Integration

The project includes GitHub Actions workflow (`.github/workflows/docker.yml`) that:

1. Builds both scheduler and web images
2. Runs tests in the build container
3. Verifies Chrome installation in scheduler
4. Validates docker-compose configuration
5. Reports image sizes

### Extending for Deployment

Add deployment steps to workflow:

```yaml
- name: Push to registry
  run: |
    echo "${{ secrets.DOCKER_PASSWORD }}" | docker login -u "${{ secrets.DOCKER_USERNAME }}" --password-stdin
    docker tag dfl-manager:scheduler your-registry/dfl-manager:scheduler-${{ github.sha }}
    docker tag dfl-manager:web your-registry/dfl-manager:web-${{ github.sha }}
    docker push your-registry/dfl-manager:scheduler-${{ github.sha }}
    docker push your-registry/dfl-manager:web-${{ github.sha }}
```

## Support

For issues or questions:
1. Check container logs: `docker logs <container-name>`
2. Verify environment variables are set correctly
3. Ensure database is accessible
4. Review this deployment guide
5. Check application configuration in `application.yml`

---

**Last Updated**: 2026-02-10 (Phase 3 - Docker Configuration)
