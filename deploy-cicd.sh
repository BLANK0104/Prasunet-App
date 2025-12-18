#!/bin/bash

# 🚀 Quick Deploy Script - Push CI/CD to GitHub
# This script will add and push all CI/CD files to GitHub

echo "🚀 Prasunet LMS - CI/CD Deployment Script"
echo "=========================================="
echo ""

# Check if git is initialized
if [ ! -d .git ]; then
    echo "❌ Error: Not a git repository!"
    echo "Run: git init"
    exit 1
fi

echo "📦 Adding CI/CD workflow files..."
git add .github/workflows/

echo "📝 Adding documentation files..."
git add CICD_*.md QUICKSTART_CICD.md

echo "✅ Staging complete!"
echo ""
echo "Files to be committed:"
git status --short

echo ""
read -p "🤔 Commit and push? (y/n) " -n 1 -r
echo ""

if [[ $REPLY =~ ^[Yy]$ ]]
then
    echo "💾 Committing..."
    git commit -m "🚀 Add CI/CD pipeline for automatic APK releases

- Added GitHub Actions workflows for automatic builds
- Build & release APK on every push to main/master
- Added comprehensive documentation
- Supports version tags for production releases"

    echo ""
    echo "📤 Pushing to GitHub..."
    git push origin main || git push origin master

    echo ""
    echo "✅ Done! CI/CD is now active!"
    echo ""
    echo "🔗 Check your build status:"
    echo "   Actions: https://github.com/YOUR_USERNAME/YOUR_REPO/actions"
    echo "   Releases: https://github.com/YOUR_USERNAME/YOUR_REPO/releases"
    echo ""
    echo "⏱️  First build will take ~5-10 minutes"
    echo "📱 APK will be available in Releases tab"
else
    echo "❌ Cancelled. No changes pushed."
fi

