#!/bin/bash
#
# Setup script to install git hooks for all developers
#
# Usage: .githooks/setup.sh
#

echo "🔧 Setting up git hooks..."

# Configure git to use the shared hooks directory
git config core.hooksPath .githooks

# Make all hooks executable
chmod +x .githooks/*

echo "✅ Git hooks installed successfully!"
echo ""
echo "The following hooks are now active:"
echo "  - pre-push: Runs tests before each push"
echo ""
echo "To skip hooks on push (not recommended): git push --no-verify"
