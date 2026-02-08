# Git Hooks

This directory contains shared git hooks for the DFL Manager project.

## Installation

Run the setup script to install the hooks:

```bash
.githooks/setup.sh
```

Or manually:

```bash
git config core.hooksPath .githooks
chmod +x .githooks/*
```

## Available Hooks

### pre-commit

Runs Maven tests before allowing a commit. This ensures:
- All tests pass before code is committed
- No broken code enters the repository
- Consistent code quality

**What it does:**
1. Runs `mvn test -q`
2. If tests fail, blocks the commit
3. If tests pass, allows the commit to proceed

**Skipping the hook (not recommended):**
```bash
git commit --no-verify
```

## Testing the Hook

To test the pre-commit hook without making a commit:

```bash
.githooks/pre-commit
```

## Customization

You can modify the hooks in this directory. Changes will be automatically picked up by all developers who have run the setup script.

## Troubleshooting

**Hook not running:**
- Make sure you ran the setup script: `.githooks/setup.sh`
- Verify hooks are executable: `ls -la .githooks/`
- Check git config: `git config core.hooksPath`

**Tests taking too long:**
- Consider running only fast unit tests in pre-commit
- Move slower integration tests to pre-push hook
- Use `git commit --no-verify` for WIP commits (but run tests before pushing!)
