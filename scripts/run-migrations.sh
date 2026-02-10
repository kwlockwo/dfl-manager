#!/bin/bash
# Flyway database migrations for Render pre-deploy hook
#
# This script runs database migrations before deploying new application code.
# If migrations fail, the deployment is aborted (safe rollback).
#
# Environment variables required:
# - DATABASE_HOST
# - DATABASE_PORT
# - DATABASE_NAME
# - DATABASE_USER or JDBC_DATABASE_USERNAME
# - DATABASE_PASSWORD or JDBC_DATABASE_PASSWORD

set -e  # Exit on any error

echo "🔄 Starting database migrations..."

# Determine which credentials to use
DB_USER="${DATABASE_USER:-${JDBC_DATABASE_USERNAME}}"
DB_PASS="${DATABASE_PASSWORD:-${JDBC_DATABASE_PASSWORD}}"

# Validate required environment variables
if [ -z "$DATABASE_HOST" ] || [ -z "$DATABASE_PORT" ] || [ -z "$DATABASE_NAME" ]; then
  echo "❌ Error: Missing required database environment variables"
  echo "   Required: DATABASE_HOST, DATABASE_PORT, DATABASE_NAME"
  exit 1
fi

if [ -z "$DB_USER" ] || [ -z "$DB_PASS" ]; then
  echo "❌ Error: Missing database credentials"
  echo "   Required: DATABASE_USER/JDBC_DATABASE_USERNAME and DATABASE_PASSWORD/JDBC_DATABASE_PASSWORD"
  exit 1
fi

# Construct JDBC URL
JDBC_URL="jdbc:postgresql://${DATABASE_HOST}:${DATABASE_PORT}/${DATABASE_NAME}"

echo "📊 Database: ${DATABASE_HOST}:${DATABASE_PORT}/${DATABASE_NAME}"
echo "👤 User: ${DB_USER}"

# Check if Flyway CLI is available
if ! command -v flyway &> /dev/null; then
  echo "⚠️  Flyway CLI not found, installing..."

  # Download and install Flyway CLI
  FLYWAY_VERSION="10.20.0"
  FLYWAY_TAR="flyway-commandline-${FLYWAY_VERSION}-linux-x64.tar.gz"

  curl -L "https://repo1.maven.org/maven2/org/flywaydb/flyway-commandline/${FLYWAY_VERSION}/${FLYWAY_TAR}" -o /tmp/flyway.tar.gz
  tar -xzf /tmp/flyway.tar.gz -C /tmp
  export PATH="/tmp/flyway-${FLYWAY_VERSION}:$PATH"
  rm /tmp/flyway.tar.gz

  echo "✅ Flyway CLI installed"
fi

# Run Flyway migrations
echo "🚀 Running Flyway migrations..."

flyway \
  -url="${JDBC_URL}" \
  -user="${DB_USER}" \
  -password="${DB_PASS}" \
  -locations="filesystem:./common/src/main/resources/db/migration" \
  -baselineOnMigrate=true \
  -baselineVersion=0 \
  -validateMigrationNaming=true \
  migrate

MIGRATION_EXIT_CODE=$?

if [ $MIGRATION_EXIT_CODE -eq 0 ]; then
  echo "✅ Database migrations completed successfully"

  # Show migration status
  echo ""
  echo "📋 Migration history:"
  flyway \
    -url="${JDBC_URL}" \
    -user="${DB_USER}" \
    -password="${DB_PASS}" \
    info || true

  exit 0
else
  echo "❌ Database migrations failed with exit code: $MIGRATION_EXIT_CODE"
  echo "🛑 Deployment aborted - fix migrations and try again"
  exit $MIGRATION_EXIT_CODE
fi
