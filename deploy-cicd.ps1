# 🚀 Quick Deploy Script - Push CI/CD to GitHub (PowerShell)

Write-Host "🚀 Prasunet LMS - CI/CD Deployment Script" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host ""

# Check if git is initialized
if (-not (Test-Path ".git")) {
    Write-Host "❌ Error: Not a git repository!" -ForegroundColor Red
    Write-Host "Run: git init" -ForegroundColor Yellow
    exit 1
}

Write-Host "📦 Adding CI/CD workflow files..." -ForegroundColor Yellow
git add .github/workflows/

Write-Host "📝 Adding documentation files..." -ForegroundColor Yellow
git add CICD_*.md QUICKSTART_CICD.md

Write-Host "✅ Staging complete!" -ForegroundColor Green
Write-Host ""
Write-Host "Files to be committed:" -ForegroundColor Cyan
git status --short

Write-Host ""
$confirm = Read-Host "🤔 Commit and push? (y/n)"

if ($confirm -eq "y" -or $confirm -eq "Y") {
    Write-Host "💾 Committing..." -ForegroundColor Yellow
    git commit -m "🚀 Add CI/CD pipeline for automatic APK releases

- Added GitHub Actions workflows for automatic builds
- Build & release APK on every push to main/master
- Added comprehensive documentation
- Supports version tags for production releases"

    Write-Host ""
    Write-Host "📤 Pushing to GitHub..." -ForegroundColor Yellow

    # Try main branch first, then master
    $pushed = $false
    try {
        git push origin main 2>&1 | Out-Null
        $pushed = $true
        Write-Host "✅ Pushed to main branch" -ForegroundColor Green
    } catch {
        try {
            git push origin master 2>&1 | Out-Null
            $pushed = $true
            Write-Host "✅ Pushed to master branch" -ForegroundColor Green
        } catch {
            Write-Host "❌ Failed to push. Check your remote configuration." -ForegroundColor Red
            exit 1
        }
    }

    if ($pushed) {
        Write-Host ""
        Write-Host "✅ Done! CI/CD is now active!" -ForegroundColor Green
        Write-Host ""
        Write-Host "🔗 Next steps:" -ForegroundColor Cyan
        Write-Host "   1. Go to your GitHub repository" -ForegroundColor White
        Write-Host "   2. Click 'Actions' tab to see build progress" -ForegroundColor White
        Write-Host "   3. After ~5-10 minutes, check 'Releases' tab" -ForegroundColor White
        Write-Host "   4. Download and install the APK!" -ForegroundColor White
        Write-Host ""
        Write-Host "⏱️  First build takes ~5-10 minutes" -ForegroundColor Yellow
        Write-Host "📱 APK will be available in Releases tab" -ForegroundColor Yellow
    }
} else {
    Write-Host "❌ Cancelled. No changes pushed." -ForegroundColor Red
}

Write-Host ""
Write-Host "Press any key to exit..."
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")

