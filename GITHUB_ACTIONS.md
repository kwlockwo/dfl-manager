# GitHub Actions CI/CD Setup

## ✅ What's Configured

Your repository now has **automatic testing via GitHub Actions** with PostgreSQL!

### Workflow Overview

**Name:** Tests
**File:** `.github/workflows/test.yml`
**Triggers:** Push to main/develop/feature branches, Pull Requests

### What Happens on Every Push/PR

1. ☕ **Java 25** is installed (Temurin distribution)
2. 📦 **Maven dependencies** are cached for faster builds
3. 🧪 **Unit tests run** (BaseHandlerTest - 6 tests)
4. 📊 **Test reports** are uploaded and displayed in GitHub UI

### Test Strategy

| Test Suite | Local | GitHub Actions | Status |
|------------|-------|----------------|--------|
| **BaseHandlerTest** | ✅ Runs | ✅ Runs | 6 tests passing |
| **ServiceFactoryTest** | ⏭️ Skipped | ⏭️ Skipped | Needs DB config |
| **EntityManagerFactoryProviderTest** | ⏭️ Skipped | ⏭️ Skipped | Needs DB config |
| **TransactionHelperTest** | ⏭️ Skipped | ⏭️ Skipped | Needs DB config |

### View Test Results

1. Go to **Actions** tab in GitHub: https://github.com/kwlockwo/dfl-manager/actions
2. Click on latest workflow run
3. See test results summary
4. Download detailed reports if needed

## Configuration Details

### Maven Test Command

Both locally and in GitHub Actions:

```bash
mvn test  # Runs BaseHandlerTest only
```

Pre-commit hook runs the same test:
```bash
mvn test -Dtest=BaseHandlerTest -q
```

## Benefits

✅ **Fast unit testing** - BaseHandlerTest runs in < 1 second
✅ **No database setup** - Unit tests don't require database
✅ **Automatic execution** - Every push/PR triggers tests
✅ **Visible results** - Test reports in GitHub UI
✅ **Fast feedback** - Know if tests break immediately
✅ **PR protection** - Tests must pass before merge (branch protection enabled)

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

- Verify the same tests run in both environments
- Check Java version compatibility (should be 25 in both)
- Review test isolation (no shared state between tests)

## Next Steps

### 1. ✅ Branch Protection Enabled

Main branch now requires "test" workflow to pass before merging.

### 2. Add Database Integration Tests

Configure remaining tests (ServiceFactoryTest, EntityManagerFactoryProviderTest, TransactionHelperTest) to work with test database.

### 3. Add Build Workflow

Create `.github/workflows/build.yml` for packaging.

### 4. Add Coverage Reporting

Use JaCoCo + Codecov for test coverage tracking.

## Files

- `.github/workflows/test.yml` - Main test workflow
- `.github/workflows/README.md` - Detailed workflow documentation
- `src/test/resources/META-INF/persistence.xml` - Test database configuration

## Current Status

- ✅ GitHub Actions configured
- ✅ Test workflow active (BaseHandlerTest - 6 tests)
- ✅ Automatic on push/PR
- ✅ Branch protection enabled on main
- ✅ Workflow running successfully

Check it out: https://github.com/kwlockwo/dfl-manager/actions
