# DFL Manager Monorepo

Modern monorepo structure for the DFL Manager project, organized into three Maven modules with Spring Boot 3.4.3 and Spring Data JPA.

## 🏗️ Project Structure

```
dfl-manager/
├── pom.xml                 # Parent POM with Spring Boot 3.4.3, Java 21
├── common/                 # Shared entities, repositories, services, utilities
├── scheduler/              # Batch processing and Quartz jobs
└── web/                    # Spring Boot web application with Thymeleaf
```

## 📦 Modules

### Common Module
Shared code used by both scheduler and web applications.

**Contents**:
- 28 JPA entities + 13 composite key classes + 1 converter
- 24 Spring Data JPA repositories
- 24 Spring services
- Utility classes (DflmngrUtils, EmailUtils, AmazonS3Utils, OAuth2)
- 11 custom exception classes

**Tests**: 32 integration tests for repositories

### Scheduler Module
Batch processing, Quartz job scheduling, and data collection.

**Contents**:
- Spring Boot application with CommandLineRunner
- 26 handler classes (BaseHandler pattern)
- 7 Quartz jobs + 7 job generators
- Report generators (Excel, email)
- Validation logic
- Log4j2 with syslog support

**Tests**: 60 tests (handlers, jobs, validation)

### Web Module
Spring Boot web application for displaying DFL data.

**Contents**:
- 6 controllers (HTML + REST)
- 3 services
- Thymeleaf templates and static resources
- Spring Data JPA repositories (shared from common)

**Tests**: None currently

## 🧪 Testing

**Total**: 92 tests passing

```bash
# Run all tests (uses H2 in-memory database)
./mvnw test

# Run tests for specific module
./mvnw test -pl common
./mvnw test -pl scheduler

# Run tests in CI (uses PostgreSQL 16)
# Automatically configured via GitHub Actions
```

## 🔨 Building

```bash
# Build all modules
./mvnw clean install

# Build specific module
./mvnw clean install -pl common
./mvnw clean install -pl scheduler,common
./mvnw clean install -pl web,common

# Skip tests
./mvnw clean install -DskipTests
```

## 🚀 Running Locally

### Scheduler (CLI)
```bash
# Build the JAR
./mvnw clean package -pl scheduler,common

# Run a specific handler
java -jar scheduler/target/dfl-manager-scheduler.jar --handler=afl-fixture-loader

# Available handlers (see scheduler/bin/*.sh for full list)
```

### Web Application
```bash
# Build the JAR
./mvnw clean package -pl web,common

# Run the web server
java -jar web/target/dfl-manager-web.jar

# Access at http://localhost:8080
```

## 🐳 Docker

### Build Images
```bash
# Scheduler (includes Chrome for Selenium)
docker build -f Dockerfile.scheduler -t dfl-manager:scheduler .

# Web application
docker build -f Dockerfile.web -t dfl-manager:web .
```

### Run with Docker Compose
```bash
# Start all services (PostgreSQL, scheduler, web)
docker-compose up

# Run specific service
docker-compose up web
docker-compose up scheduler
```

## 🔧 Tech Stack

- **Java**: 21 (LTS)
- **Spring Boot**: 3.4.3
- **Spring Data JPA**: Repository abstraction over Hibernate
- **Database**: PostgreSQL 16 (production), H2 (local/test)
- **Logging**: Log4j2 with syslog support
- **Job Scheduling**: Quartz 2.5.2
- **Web Scraping**: Selenium 4.40.0 with Chrome/Chromium
- **Template Engine**: Thymeleaf
- **Testing**: JUnit 5, Mockito, AssertJ
- **Build Tool**: Maven 3.9.9 (via wrapper)

## 📊 CI/CD

GitHub Actions workflows run on every push and pull request:

- **Tests**: 92 tests with PostgreSQL 16 and JaCoCo coverage
- **Docker**: Build both scheduler and web images, verify Chrome installation
- **Branch Protection**: Requires both workflows to pass before merge

See [GITHUB_ACTIONS.md](GITHUB_ACTIONS.md) for details.

## 🗄️ Database

### Local Development
- Uses **H2 in-memory database** (PostgreSQL compatibility mode)
- No setup required, runs automatically with tests
- Schema auto-generated from JPA entities

### CI/CD
- Uses **PostgreSQL 16** in GitHub Actions
- Database migrations managed by Flyway
- See [DATABASE_MIGRATIONS.md](DATABASE_MIGRATIONS.md)

### Production
- PostgreSQL 16 on Render
- Automated migrations via pre-deploy hook
- See [DEPLOYMENT.md](DEPLOYMENT.md)

## 🚢 Deployment

Deployed to Render (Singapore region) with automated migrations:

1. Push code → triggers deployment
2. Pre-deploy hook runs Flyway migrations
3. New containers deploy if migrations succeed
4. Hibernate validates schema on startup

See [DEPLOYMENT.md](DEPLOYMENT.md) for full details.

## 📝 Key Documentation

- [DATABASE_MIGRATIONS.md](DATABASE_MIGRATIONS.md) - Flyway migration guide
- [DEPLOYMENT.md](DEPLOYMENT.md) - Docker and Render deployment
- [MIGRATION_STRATEGY.md](MIGRATION_STRATEGY.md) - Production workflow
- [GITHUB_ACTIONS.md](GITHUB_ACTIONS.md) - CI/CD workflows
- [CONTRIBUTING.md](CONTRIBUTING.md) - Development guidelines

### Repository Guides
- [common/src/main/java/net/dflmngr/repositories/REPOSITORIES_SUMMARY.md](common/src/main/java/net/dflmngr/repositories/REPOSITORIES_SUMMARY.md) - Spring Data repositories
- [common/src/main/java/net/dflmngr/repositories/MIGRATION_GUIDE.md](common/src/main/java/net/dflmngr/repositories/MIGRATION_GUIDE.md) - DAO to repository migration

## 🌿 Branch Strategy

- **`monorepo`**: Production-ready branch with Spring Boot + Spring Data JPA
- **`main`**: Legacy EclipseLink-based backend (will be deprecated)
- **Feature branches**: Create from `monorepo`, merge via PR

### Branch Protection
The `monorepo` branch is protected:
- ✅ Requires PR reviews
- ✅ Requires status checks to pass (test + docker workflows)
- ✅ No direct pushes
- ✅ No force pushes
- ✅ Enforced for admins

## 🤝 Contributing

1. Create a feature branch from `monorepo`
2. Make your changes
3. Ensure tests pass locally: `./mvnw test`
4. Create a pull request to `monorepo`
5. Wait for CI/CD checks to pass
6. Request review

See [CONTRIBUTING.md](CONTRIBUTING.md) for detailed guidelines.

## 📜 License

Proprietary - DFL Manager Project

## 🆘 Support

For questions or issues:
1. Check existing documentation in this README and linked docs
2. Review [GITHUB_ACTIONS.md](GITHUB_ACTIONS.md) for CI/CD issues
3. Check [DATABASE_MIGRATIONS.md](DATABASE_MIGRATIONS.md) for migration issues
