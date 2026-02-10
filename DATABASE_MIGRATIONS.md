# Database Migrations with Flyway

This project uses [Flyway](https://flywaydb.org/) for database schema migrations.

## Overview

- **Migration files**: Located in `common/src/main/resources/db/migration/`
- **Naming convention**: `V{version}__{description}.sql` (e.g., `V1__Initial_schema.sql`, `V2__Add_user_table.sql`)
- **Execution**: Flyway runs automatically on application startup
- **Validation**: Hibernate validates schema matches entities (`ddl-auto: validate`)

## Configuration

### Scheduler (scheduler/src/main/resources/application.yml)
```yaml
spring:
  flyway:
    enabled: true
    baseline-on-migrate: true
    baseline-version: 0
    locations: classpath:db/migration
    validate-on-migrate: true
```

### Web (web/src/main/resources/application.yml)
```yaml
spring:
  flyway:
    enabled: true
    baseline-on-migrate: true
    baseline-version: 0
    locations: classpath:db/migration
    validate-on-migrate: true
```

## Initial Setup

### For Existing Databases (Production)

If you have an existing database with schema and data:

1. **Export the current schema**:
   ```bash
   pg_dump -h localhost -U dflmngr -d dflmngrdb \
     --schema-only \
     --no-owner \
     --no-privileges \
     --no-tablespaces \
     --no-security-labels \
     --no-comments \
     > common/src/main/resources/db/migration/V1__Initial_schema.sql
   ```

2. **Baseline the production database**:
   ```sql
   -- Run this on production database before deploying Flyway
   INSERT INTO flyway_schema_history (
     installed_rank, version, description, type, script,
     checksum, installed_by, installed_on, execution_time, success
   ) VALUES (
     1, '1', 'Initial schema', 'SQL', 'V1__Initial_schema.sql',
     NULL, 'manual', NOW(), 0, true
   );
   ```

   Or use Flyway CLI:
   ```bash
   flyway -url=jdbc:postgresql://localhost:5432/dflmngrdb \
          -user=dflmngr \
          -password=<password> \
          baseline -baselineVersion=1
   ```

### For New Databases (Development/Staging)

If starting fresh:

1. **Generate schema from entities**:
   The initial migration should create all tables. You can generate it using:

   ```bash
   # Temporarily change hibernate.ddl-auto to 'create' and capture SQL
   # Or use pg_dump from a dev database after Hibernate creates schema
   ```

2. **Let Flyway run**:
   Flyway will automatically create the schema on first startup.

## Creating New Migrations

When making schema changes:

1. **Create a new migration file**:
   ```bash
   # Format: V{next_version}__{description}.sql
   touch common/src/main/resources/db/migration/V2__Add_new_column.sql
   ```

2. **Write SQL**:
   ```sql
   -- V2__Add_new_column.sql
   ALTER TABLE dfl_player ADD COLUMN email VARCHAR(255);
   CREATE INDEX idx_dfl_player_email ON dfl_player(email);
   ```

3. **Update entities**:
   Add corresponding field to Java entity:
   ```java
   @Column(name = "email")
   private String email;
   ```

4. **Test locally**:
   ```bash
   mvn clean test
   docker-compose up
   ```

5. **Commit both migration and entity changes together**

## Migration Workflow

### Development
1. Create migration file
2. Update entity
3. Run tests (`mvn test`)
4. Commit together

### Staging/Production (Render Deployment)

The project uses **pre-deploy hooks** for safe migrations:

1. **Push code to GitHub**
   ```bash
   git push origin monorepo
   ```

2. **Render starts deployment**:
   - Builds Docker images
   - Runs `preDeployCommand: ./scripts/run-migrations.sh`
   - Migrations execute **before** new code deploys
   - If migrations fail → deployment aborted (safe!)
   - If migrations succeed → new containers start

3. **Application startup**:
   - Flyway skips (migrations already done)
   - Hibernate validates schema matches entities
   - Application serves traffic

**Key benefit**: Migrations run once, in isolation, before any code changes. If they fail, old code keeps running.

## Best Practices

### DO
- ✅ Use descriptive migration names: `V2__Add_email_to_player.sql`
- ✅ Make migrations idempotent when possible
- ✅ Test migrations on a copy of production data
- ✅ Include rollback instructions in comments
- ✅ Keep migrations small and focused
- ✅ Version control all migrations
- ✅ Never modify applied migrations

### DON'T
- ❌ Edit migrations after they've been applied
- ❌ Delete applied migrations
- ❌ Use `ddl-auto: update` in production
- ❌ Make breaking changes without data migration
- ❌ Skip version numbers

## Production Migration Strategy

### Render Pre-Deploy Hook (Configured)

The [render.yaml](render.yaml) includes a `preDeployCommand` that runs migrations:

```yaml
services:
  - type: worker
    name: dfl-scheduler
    preDeployCommand: ./scripts/run-migrations.sh
```

**How it works**:
1. Render builds new Docker image
2. Runs [scripts/run-migrations.sh](scripts/run-migrations.sh) in build environment
3. Script downloads Flyway CLI
4. Runs migrations against production database
5. If migrations fail → deployment stops (old version keeps running)
6. If migrations succeed → new containers deploy

**Safety features**:
- ✅ Migrations run in isolation (not during traffic)
- ✅ Failed migrations abort deployment
- ✅ Automatic Flyway CLI installation
- ✅ Migration history logged
- ✅ No manual steps required

### Alternative: Disable Pre-Deploy Hook

If you prefer automatic migrations on startup, remove the `preDeployCommand` from render.yaml:

```yaml
# Remove this line:
preDeployCommand: ./scripts/run-migrations.sh
```

Flyway will run when each container starts instead.

## Common Operations

### Check Migration Status
```bash
# Using Flyway Maven plugin
mvn flyway:info -pl common

# Or query database directly
SELECT * FROM flyway_schema_history ORDER BY installed_rank;
```

### Repair Failed Migration
```bash
# If a migration fails and needs to be fixed
mvn flyway:repair -pl common

# Or manually:
DELETE FROM flyway_schema_history WHERE version = '<failed_version>';
```

### Baseline Existing Database
```bash
mvn flyway:baseline -pl common \
  -Dflyway.baselineVersion=1 \
  -Dflyway.baselineDescription="Existing schema"
```

## Troubleshooting

### "Checksum mismatch"
- Never edit applied migrations
- If absolutely necessary: `mvn flyway:repair`

### "Schema already exists"
- Set `baseline-on-migrate: true` for existing databases
- Or manually baseline first

### "Validation failed"
- Entity doesn't match database schema
- Check Hibernate logs for details
- Ensure migration was applied

## Disabling Flyway (Development Only)

For local development experiments:

```yaml
spring:
  flyway:
    enabled: false
  jpa:
    hibernate:
      ddl-auto: create-drop  # or 'update'
```

**⚠️ Never use in production!**

## Further Reading

- [Flyway Documentation](https://flywaydb.org/documentation/)
- [Flyway SQL Migrations](https://flywaydb.org/documentation/concepts/migrations#sql-based-migrations)
- [Spring Boot Flyway Integration](https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto.data-initialization.migration-tool)
