#!/bin/bash
# Generate initial Flyway migration from JPA entities
#
# This script:
# 1. Starts a temporary PostgreSQL database
# 2. Configures Hibernate to create the schema
# 3. Starts the scheduler application
# 4. Exports the generated schema to a Flyway migration
# 5. Cleans up

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
MIGRATION_FILE="$PROJECT_ROOT/common/src/main/resources/db/migration/V1__Initial_schema.sql"

echo "🚀 Generating initial schema migration..."

# Step 1: Start temporary database
echo "📦 Starting temporary PostgreSQL..."
docker run --name dfl-schema-gen -e POSTGRES_PASSWORD=temp -e POSTGRES_USER=temp -e POSTGRES_DB=tempdb -p 5433:5432 -d postgres:16-alpine
sleep 5

# Step 2: Temporarily modify application.yml to use create mode
TEMP_CONFIG=$(mktemp)
cat > "$TEMP_CONFIG" <<EOF
spring:
  datasource:
    url: jdbc:postgresql://localhost:5433/tempdb
    username: temp
    password: temp
  jpa:
    hibernate:
      ddl-auto: create
    properties:
      hibernate:
        format_sql: true
  flyway:
    enabled: false
EOF

echo "🏗️  Running Hibernate schema generation..."
cd "$PROJECT_ROOT"
java -jar "$PROJECT_ROOT/scheduler/target/dfl-manager-scheduler.jar" \
  --spring.config.location="$TEMP_CONFIG" \
  --spring.main.web-application-type=none &
APP_PID=$!

# Wait for schema creation
sleep 10
kill $APP_PID 2>/dev/null || true

# Step 3: Export schema
echo "💾 Exporting schema to Flyway migration..."
docker exec dfl-schema-gen pg_dump -U temp -d tempdb \
  --schema-only \
  --no-owner \
  --no-privileges \
  --no-tablespaces \
  --no-security-labels \
  > "$MIGRATION_FILE"

# Add header
{
  echo "-- Initial schema migration for DFL Manager"
  echo "-- Generated from JPA entities on $(date)"
  echo "-- "
  echo ""
  cat "$MIGRATION_FILE"
} > "${MIGRATION_FILE}.tmp"
mv "${MIGRATION_FILE}.tmp" "$MIGRATION_FILE"

# Step 4: Cleanup
echo "🧹 Cleaning up..."
docker stop dfl-schema-gen
docker rm dfl-schema-gen
rm "$TEMP_CONFIG"

echo "✅ Migration created: $MIGRATION_FILE"
echo ""
echo "Next steps:"
echo "1. Review the migration file"
echo "2. Commit it to version control"
echo "3. Deploy with Flyway enabled"
