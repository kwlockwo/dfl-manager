# Generating Initial Schema Migration

Since this is a greenfield deployment (no existing database), you need to create the initial Flyway migration from the JPA entities.

## Option 1: Using Hibernate Schema Export (Recommended)

1. **Build the project**:
   ```bash
   mvn clean package -DskipTests
   ```

2. **Temporarily enable schema creation**:
   Edit `scheduler/src/main/resources/application.yml`:
   ```yaml
   spring:
     jpa:
       hibernate:
         ddl-auto: create  # Change from 'validate'
     flyway:
       enabled: false  # Temporarily disable Flyway
   ```

3. **Start application against empty database**:
   ```bash
   docker run --name temp-postgres -e POSTGRES_PASSWORD=temp \
     -e POSTGRES_USER=temp -e POSTGRES_DB=tempdb \
     -p 5433:5432 -d postgres:16-alpine

   # Wait for startup
   sleep 5

   # Run with temporary config
   DATABASE_HOST=localhost \
   DATABASE_PORT=5433 \
   DATABASE_NAME=tempdb \
   DATABASE_USER=temp \
   DATABASE_PASSWORD=temp \
   ENV=production \
   java -jar scheduler/target/dfl-manager-scheduler.jar
   ```

4. **Export the schema**:
   ```bash
   docker exec temp-postgres pg_dump -U temp -d tempdb \
     --schema-only \
     --no-owner \
     --no-privileges \
     > common/src/main/resources/db/migration/V1__Initial_schema.sql
   ```

5. **Cleanup**:
   ```bash
   docker stop temp-postgres && docker rm temp-postgres
   ```

6. **Restore configuration**:
   Revert changes to `application.yml`:
   ```yaml
   spring:
     jpa:
       hibernate:
         ddl-auto: validate
     flyway:
       enabled: true
   ```

7. **Commit the migration**:
   ```bash
   git add common/src/main/resources/db/migration/V1__Initial_schema.sql
   git commit -m "feat: Add initial schema migration"
   ```

## Option 2: Skip Initial Migration (Development Only)

For development/testing, you can skip Flyway entirely and let Hibernate manage the schema:

```yaml
spring:
  flyway:
    enabled: false
  jpa:
    hibernate:
      ddl-auto: update  # or 'create-drop'
```

**⚠️ NEVER use this in production!**

## Option 3: Manual Migration Creation

If you have an existing database schema (from old backend), export it directly:

```bash
pg_dump -h <host> -U <user> -d <database> \
  --schema-only \
  --no-owner \
  --no-privileges \
  > common/src/main/resources/db/migration/V1__Initial_schema.sql
```

## After Creating Migration

1. Test locally:
   ```bash
   docker-compose up
   ```

2. Verify Flyway runs successfully:
   ```bash
   docker logs dfl-scheduler | grep Flyway
   docker logs dfl-web | grep Flyway
   ```

3. Check migration was applied:
   ```bash
   docker exec dfl-postgres psql -U dflmngr -d dflmngrdb \
     -c "SELECT * FROM flyway_schema_history;"
   ```

See `DATABASE_MIGRATIONS.md` for ongoing migration workflow.
