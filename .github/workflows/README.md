# GitHub Actions CI/CD

## Test Workflow

The `test.yml` workflow automatically runs tests on every push and pull request.

### What It Does

1. **Sets up PostgreSQL 16** - Runs a PostgreSQL service container
2. **Checks out code** - Gets the latest code from the repository
3. **Sets up JDK 25** - Installs Java 25 (Temurin distribution)
4. **Runs tests** - Executes `mvn test` with PostgreSQL connection
5. **Uploads results** - Saves test reports as artifacts
6. **Publishes report** - Creates a test report in the Actions UI

### Database Configuration

**Locally (developers):**
- Uses H2 in-memory database
- No setup required
- Fast test execution

**GitHub Actions (CI):**
- Uses PostgreSQL 16 service container
- Database: `dflmngr_test`
- User: `dflmngr`
- Password: `dflmngr_test_password`
- Configured via environment variables

### Viewing Test Results

1. Go to the **Actions** tab in GitHub
2. Click on the latest workflow run
3. View the **Test Results** section for detailed report
4. Download artifacts for detailed Surefire reports

### Triggers

The workflow runs on:
- **Push** to `main`, `develop`, or `feature/**` branches
- **Pull requests** to `main` or `develop`

### Environment Variables

The workflow sets these environment variables for PostgreSQL:

```yaml
DB_URL: jdbc:postgresql://localhost:5432/dflmngr_test
DB_USER: dflmngr
DB_PASSWORD: dflmngr_test_password
DB_DRIVER: org.postgresql.Driver
DB_PLATFORM: org.eclipse.persistence.platform.database.PostgreSQLPlatform
```

These override the default H2 configuration in `persistence.xml`.

### Adding More Workflows

To add new workflows, create `.yml` files in this directory:

**Build workflow:**
```yaml
name: Build
on: [push, pull_request]
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
      - run: mvn package
```

**Deploy workflow:**
```yaml
name: Deploy
on:
  push:
    branches: [main]
# Add deployment steps
```

### Troubleshooting

**Tests fail in CI but pass locally:**
- Check PostgreSQL-specific SQL syntax
- Verify H2 compatibility mode is correct
- Check test isolation (tests may depend on order locally)

**PostgreSQL service not ready:**
- The workflow includes health checks
- Waits for `pg_isready` before running tests
- Check PostgreSQL logs in Actions output

**JDK version mismatch:**
- Workflow uses JDK 25 (Temurin)
- Update your local Java version if needed
