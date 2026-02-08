# GitHub Actions CI/CD Setup

## ✅ What's Configured

Your repository now has **automatic testing via GitHub Actions** with PostgreSQL!

### Workflow Overview

**Name:** Tests
**File:** `.github/workflows/test.yml`
**Triggers:** Push to main/develop/feature branches, Pull Requests

### What Happens on Every Push/PR

1. 🐘 **PostgreSQL 16 starts** in a service container
2. ☕ **Java 25** is installed (Temurin distribution)
3. 📦 **Maven dependencies** are cached for faster builds
4. 🧪 **All tests run** against real PostgreSQL database
5. 📊 **Test reports** are uploaded and displayed in GitHub UI

### Database Strategy

| Environment | Database | Setup Required |
|-------------|----------|----------------|
| **Local (developers)** | H2 in-memory | None ✅ |
| **GitHub Actions (CI)** | PostgreSQL 16 | Automatic ✅ |

### View Test Results

1. Go to **Actions** tab in GitHub: https://github.com/kwlockwo/dfl-manager/actions
2. Click on latest workflow run
3. See test results summary
4. Download detailed reports if needed

## Configuration Details

### PostgreSQL Service

```yaml
Database: dflmngr_test
User: dflmngr
Password: dflmngr_test_password
Port: 5432
Health checks: Automatic
```

### Maven Test Command

In GitHub Actions, tests run with:

```bash
mvn test --batch-mode --fail-at-end \
  -Djakarta.persistence.jdbc.url="jdbc:postgresql://localhost:5432/dflmngr_test" \
  -Djakarta.persistence.jdbc.user="dflmngr" \
  -Djakarta.persistence.jdbc.password="dflmngr_test_password" \
  -Djakarta.persistence.jdbc.driver="org.postgresql.Driver" \
  -Declipselink.target-database="org.eclipse.persistence.platform.database.PostgreSQLPlatform"
```

Locally, just run:
```bash
mvn test  # Uses H2 automatically
```

## Benefits

✅ **Proper integration testing** - Tests run against real PostgreSQL
✅ **No local setup** - Developers use H2, CI uses PostgreSQL
✅ **Automatic execution** - Every push/PR triggers tests
✅ **Visible results** - Test reports in GitHub UI
✅ **Fast feedback** - Know if tests break immediately
✅ **PR protection** - Can require passing tests before merge

## Adding Branch Protection

To require tests to pass before merging:

1. Go to **Settings** → **Branches**
2. Add rule for `main` branch
3. Check "Require status checks to pass"
4. Select "Tests" workflow
5. Save

Now PRs can't be merged until tests pass!

## Troubleshooting

### Workflow doesn't run

- Check `.github/workflows/test.yml` is in main branch
- Verify Actions are enabled in repo settings

### Tests fail in CI but pass locally

- PostgreSQL vs H2 differences (SQL syntax, features)
- Check H2 PostgreSQL compatibility mode
- Test isolation issues (order dependency)

### PostgreSQL connection fails

- Service health checks ensure PostgreSQL is ready
- Check logs in Actions output
- Verify connection string in workflow

## Next Steps

### 1. Enable Required Status Checks

Protect your main branch by requiring tests to pass.

### 2. Add Build Workflow

Create `.github/workflows/build.yml` for packaging.

### 3. Add Coverage Reporting

Use JaCoCo + Codecov for test coverage tracking.

### 4. Add Deployment Workflow

Automate deployment when tests pass on main.

## Files

- `.github/workflows/test.yml` - Main test workflow
- `.github/workflows/README.md` - Detailed workflow documentation
- `src/test/resources/META-INF/persistence.xml` - Test database configuration

## Current Status

- ✅ GitHub Actions configured
- ✅ PostgreSQL service container running
- ✅ Test workflow active
- ✅ Automatic on push/PR
- ⏳ Waiting for first workflow run...

Check it out: https://github.com/kwlockwo/dfl-manager/actions
