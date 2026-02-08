# Contributing to DFL Manager

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/kwlockwo/dfl-manager.git
cd dfl-manager/code
```

### 2. Install Git Hooks

**Important:** Run this setup script to install automated testing hooks:

```bash
.githooks/setup.sh
```

This will configure git to run tests automatically before each commit, ensuring code quality.

## Development Workflow

### Before You Start

1. Make sure you're on the latest main branch:
   ```bash
   git checkout main
   git pull origin main
   ```

2. Create a feature branch:
   ```bash
   git checkout -b feature/your-feature-name
   ```

### Making Changes

1. Make your code changes
2. Run tests manually to verify:
   ```bash
   mvn test
   ```
3. Commit your changes:
   ```bash
   git add .
   git commit -m "Your commit message"
   ```

   **Note:** The pre-commit hook will automatically run tests. If they fail, your commit will be blocked.

### Pre-Commit Hook

The pre-commit hook automatically runs `BaseHandlerTest` before each commit:
- ✅ If tests pass → commit proceeds
- ❌ If tests fail → commit is blocked

**To skip the hook** (not recommended):
```bash
git commit --no-verify -m "Your message"
```

## Testing

### Run All Tests
```bash
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=BaseHandlerTest
```

### Run Tests in Quiet Mode
```bash
mvn test -q
```

## Code Standards

### Handlers
- All handlers must extend `BaseHandler`
- Use `ServiceFactory` for service instantiation
- Call `ensureLoggingConfigured()` at the start of `execute()` methods

### Services
- Create services via `ServiceFactory.getInstance().createXxxService()`
- Never use `new XxxServiceImpl()` directly

### Transactions
- Use `TransactionHelper.executeInTransaction()` for database operations
- This provides automatic rollback on exceptions

## Pull Requests

1. Push your feature branch:
   ```bash
   git push origin feature/your-feature-name
   ```

2. Create a pull request on GitHub
3. Ensure all tests pass in CI
4. Request review from maintainers
5. Address any feedback
6. Once approved, it will be merged to main

## Build

### Compile
```bash
mvn clean compile
```

### Package
```bash
mvn clean package
```

## Troubleshooting

### Git Hook Not Running

If the pre-commit hook isn't running:

1. Check if hooks are configured:
   ```bash
   git config core.hooksPath
   ```
   Should output: `.githooks`

2. Re-run the setup script:
   ```bash
   .githooks/setup.sh
   ```

3. Verify hook is executable:
   ```bash
   ls -la .githooks/pre-commit
   ```

### Tests Failing Locally

1. Make sure you're on the latest main:
   ```bash
   git checkout main
   git pull
   ```

2. Clean and rebuild:
   ```bash
   mvn clean compile
   ```

3. Run tests:
   ```bash
   mvn test
   ```

## Questions?

- Check the [TEST_SUMMARY.md](TEST_SUMMARY.md) for testing documentation
- Review [.githooks/README.md](.githooks/README.md) for git hooks documentation
- Open an issue on GitHub for help

## License

[Add your license information here]
