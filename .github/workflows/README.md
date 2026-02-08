# GitHub Actions CI/CD

## Test Workflow

The `test.yml` workflow automatically runs tests on every push and pull request.

### What It Does

1. **Checks out code** - Gets the latest code from the repository
2. **Sets up JDK 25** - Installs Java 25 (Temurin distribution)
3. **Runs tests** - Executes BaseHandlerTest (6 unit tests)
4. **Uploads results** - Saves test reports as artifacts
5. **Publishes report** - Creates a test report in the Actions UI

### Test Configuration

**Currently Running:**
- BaseHandlerTest (6 tests)
- Pure unit tests, no database required
- Fast execution (< 1 second)

**Excluded (need database configuration):**
- ServiceFactoryTest
- EntityManagerFactoryProviderTest
- TransactionHelperTest

### Viewing Test Results

1. Go to the **Actions** tab in GitHub
2. Click on the latest workflow run
3. View the **Test Results** section for detailed report
4. Download artifacts for detailed Surefire reports

### Triggers

The workflow runs on:
- **Push** to `main`, `develop`, or `feature/**` branches
- **Pull requests** to `main` or `develop`

### Command Executed

The workflow runs:
```bash
mvn test --batch-mode --fail-at-end -Dtest=BaseHandlerTest
```

This ensures only stable unit tests run in CI.

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
- Verify same tests are running (check `-Dtest=` parameter)
- Ensure Java 25 is being used locally
- Check test isolation (no shared state)

**JDK version mismatch:**
- Workflow uses JDK 25 (Temurin)
- Update your local Java version if needed

**Adding more tests to CI:**
- Database tests need proper configuration first
- Update the `-Dtest=` parameter to include more test classes
- Ensure tests can run without external dependencies
