# Database Migration Strategy for Production

## Overview

The DFL Manager uses **Flyway pre-deploy hooks** on Render for safe, automated database migrations.

## How It Works

### Deployment Flow

```
1. Developer pushes code to GitHub
         ↓
2. Render detects change and starts build
         ↓
3. Render builds Docker images
         ↓
4. Render runs preDeployCommand: ./scripts/run-migrations.sh
         ↓
5. Script downloads Flyway CLI
         ↓
6. Script runs database migrations
         ↓
   ┌─────────────┬─────────────┐
   │  Success    │   Failure   │
   ├─────────────┼─────────────┤
   │ ✅ Continue │ ❌ ABORT    │
   │ Deploy new  │ Keep old    │
   │ containers  │ version     │
   └─────────────┴─────────────┘
         ↓
7. Application starts with validated schema
```

### Key Benefits

✅ **Safe Rollback**: If migrations fail, deployment stops and old version keeps running
✅ **Isolated Execution**: Migrations run once in build environment, not during traffic
✅ **Zero Downtime**: No race conditions with multiple containers
✅ **Automatic**: No manual steps required
✅ **Validated**: Hibernate confirms schema matches entities before serving traffic

## Configuration

### Render Blueprint (render.yaml)

```yaml
services:
  - type: worker
    name: dfl-scheduler
    runtime: docker
    preDeployCommand: ./scripts/run-migrations.sh  # ← Runs before deploy
    envVars:
      - key: DATABASE_HOST
        fromDatabase: ...
      # ... other vars
```

### Migration Script (scripts/run-migrations.sh)

The script:
1. Validates environment variables
2. Downloads Flyway CLI (if not present)
3. Constructs JDBC URL from Render's separate env vars
4. Runs `flyway migrate`
5. Shows migration history on success
6. Exits with error code to abort deployment on failure

### Application Configuration

Both scheduler and web have Flyway enabled but configured for different scenarios:

**During Pre-Deploy (Migration Script)**:
- Flyway CLI runs migrations
- Database updated to latest version

**During Application Startup**:
- Flyway checks `flyway_schema_history` table
- Sees all migrations already applied
- Skips running migrations again
- Hibernate validates schema matches entities
- Application starts

## Creating a New Migration

### Step 1: Create Migration File

```bash
# Naming: V{version}__{description}.sql
touch common/src/main/resources/db/migration/V2__Add_email_column.sql
```

```sql
-- V2__Add_email_column.sql
ALTER TABLE dfl_player ADD COLUMN email VARCHAR(255);
CREATE INDEX idx_dfl_player_email ON dfl_player(email);

-- Rollback instructions (for reference):
-- ALTER TABLE dfl_player DROP COLUMN email;
-- DROP INDEX idx_dfl_player_email;
```

### Step 2: Update Entity

```java
@Entity
@Table(name = "dfl_player")
public class DflPlayer {
    // ... existing fields

    @Column(name = "email")
    private String email;

    // ... getters/setters
}
```

### Step 3: Test Locally

```bash
# Run tests
mvn test

# Test with Docker Compose (optional)
docker-compose up
```

### Step 4: Commit Together

```bash
git add common/src/main/resources/db/migration/V2__Add_email_column.sql
git add common/src/main/java/net/dflmngr/model/entity/DflPlayer.java
git commit -m "feat: Add email column to dfl_player table"
git push
```

### Step 5: Deploy

Render automatically:
1. Detects the push
2. Builds new Docker images
3. Runs migration script (applies V2 migration)
4. Deploys new containers with updated entity
5. Validates schema matches entity

## Troubleshooting

### Migration Fails During Pre-Deploy

**Symptom**: Render deployment fails with migration error

**Resolution**:
1. Check Render logs for migration error details
2. Fix the migration SQL
3. Push fix to GitHub
4. Render will retry with corrected migration

**Old version keeps running** - your users are unaffected!

### Schema Validation Fails

**Symptom**: Application won't start, Hibernate validation error

**Cause**: Entity doesn't match database schema

**Resolution**:
1. Check if migration was actually applied:
   ```bash
   # Connect to Render database
   render shell dfl-postgres
   psql> SELECT * FROM flyway_schema_history;
   ```
2. Verify entity annotations match table structure
3. Create corrective migration if needed

### Migrations Applied Twice

**Symptom**: Duplicate key errors, constraint violations

**Cause**: Migration not idempotent

**Prevention**: Write idempotent migrations:
```sql
-- Good (idempotent)
ALTER TABLE dfl_player ADD COLUMN IF NOT EXISTS email VARCHAR(255);

-- Bad (fails on second run)
ALTER TABLE dfl_player ADD COLUMN email VARCHAR(255);
```

## First-Time Setup

Before your first deployment to Render with Flyway enabled:

1. **Generate Initial Migration**:
   ```bash
   ./scripts/generate-schema.sh
   ```
   This creates `V1__Initial_schema.sql` from your JPA entities.

2. **Review and Commit**:
   ```bash
   git add common/src/main/resources/db/migration/V1__Initial_schema.sql
   git commit -m "feat: Add initial database schema migration"
   ```

3. **Deploy**:
   ```bash
   git push origin monorepo
   ```

Render will:
- Run the pre-deploy hook
- Create all tables via V1 migration
- Start the application
- Hibernate validates schema
- ✅ Success!

## Alternative: Disable Pre-Deploy Hook

If you prefer migrations to run on container startup instead:

1. **Remove from render.yaml**:
   ```yaml
   services:
     - type: worker
       name: dfl-scheduler
       # preDeployCommand: ./scripts/run-migrations.sh  ← Remove this line
   ```

2. **Trade-offs**:
   - ✅ Simpler configuration
   - ❌ Each container runs migrations independently
   - ❌ Race conditions possible with multiple containers
   - ❌ Failed migrations break all containers
   - ❌ Migrations run during traffic

**Recommendation**: Keep the pre-deploy hook for production safety.

## Monitoring

### Check Migration Status

```bash
# View migration history
docker exec dfl-postgres psql -U dflmngr -d dflmngrdb \
  -c "SELECT installed_rank, version, description, installed_on, success FROM flyway_schema_history ORDER BY installed_rank;"
```

### View Render Logs

```bash
# Pre-deploy migration logs
render logs --service dfl-scheduler --tail 100 | grep -A 20 "Starting database migrations"

# Application startup logs
render logs --service dfl-scheduler --tail 100 | grep -i flyway
```

## Best Practices

### DO ✅
- Write idempotent migrations (use `IF NOT EXISTS`, `IF EXISTS`)
- Include rollback instructions in migration comments
- Test migrations on copy of production data
- Keep migrations small and focused
- Version control all migrations
- Never modify applied migrations

### DON'T ❌
- Edit migrations after they're applied
- Delete applied migrations
- Use `ddl-auto: update` in production
- Make breaking changes without data migration
- Skip version numbers
- Commit migrations without testing locally

## Further Reading

- [DATABASE_MIGRATIONS.md](DATABASE_MIGRATIONS.md) - Complete Flyway usage guide
- [README_SCHEMA_GENERATION.md](README_SCHEMA_GENERATION.md) - Generate initial migration
- [Flyway Documentation](https://flywaydb.org/documentation/)
- [Render Pre-Deploy Commands](https://render.com/docs/deploy-hooks)
