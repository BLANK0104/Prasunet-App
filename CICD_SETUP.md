# CI/CD Pipeline Setup for Prasunet LMS Android App

This repository includes automated CI/CD pipelines using GitHub Actions that automatically build and release APK files.

## 🚀 Workflows

### 1. **Android CI/CD** (`android-build.yml`)
**Triggers:** On every push to `main`, `master`, or `develop` branches

**What it does:**
- ✅ Checks out the code
- ✅ Sets up JDK 17
- ✅ Builds debug APK
- ✅ Uploads APK as artifact
- ✅ Creates automatic release with timestamp
- ✅ Attaches APK to release

**Result:** Every commit automatically generates a new release with downloadable APK

### 2. **Release APK** (`release.yml`)
**Triggers:** When you push a tag like `v1.0.0`

**What it does:**
- ✅ Builds release APK (optimized)
- ✅ Signs the APK (if secrets configured)
- ✅ Creates a GitHub release
- ✅ Attaches signed APK to release

## 📦 How to Use

### Automatic Builds (Every Commit)
Simply push to main/master/develop:
```bash
git add .
git commit -m "Your commit message"
git push origin main
```

The pipeline will automatically:
1. Build the APK
2. Create a release named `v2025-12-18-14-30`
3. Upload the APK to the release

### Manual Release (Version Tags)
To create a versioned release:
```bash
git tag -a v1.0.0 -m "Version 1.0.0 - Initial release"
git push origin v1.0.0
```

This will create a release called "Prasunet LMS v1.0.0"

## 🔐 Optional: APK Signing (Recommended for Production)

To sign your APKs automatically, add these secrets to your GitHub repository:

### Step 1: Generate Keystore
```bash
keytool -genkey -v -keystore prasunet.keystore -alias prasunet -keyalg RSA -keysize 2048 -validity 10000
```

### Step 2: Convert to Base64
```bash
# Linux/Mac
base64 -i prasunet.keystore -o keystore.txt

# Windows (PowerShell)
[Convert]::ToBase64String([IO.File]::ReadAllBytes("prasunet.keystore")) | Out-File keystore.txt
```

### Step 3: Add GitHub Secrets

Go to: **Settings → Secrets and variables → Actions → New repository secret**

Add these secrets:
- `SIGNING_KEY`: Content of `keystore.txt` (the base64 string)
- `ALIAS`: Your keystore alias (e.g., `prasunet`)
- `KEY_STORE_PASSWORD`: Your keystore password
- `KEY_PASSWORD`: Your key password

## 📱 Download APKs

After any commit, you can download APKs from:
- **Releases page**: `https://github.com/YOUR_USERNAME/YOUR_REPO/releases`
- **Actions artifacts**: `https://github.com/YOUR_USERNAME/YOUR_REPO/actions`

## 🎨 Release Features

Each automatic release includes:
- 📦 APK file ready to install
- 📝 Commit information
- 🕐 Timestamp
- 🌿 Branch name
- 💬 Commit message

## 🔧 Customization

### Change Build Type
Edit `.github/workflows/android-build.yml`:
```yaml
# For debug builds
run: ./gradlew assembleDebug

# For release builds
run: ./gradlew assembleRelease
```

### Change Release Frequency
Edit the `on:` section:
```yaml
on:
  push:
    branches: [ main ]  # Only trigger on main branch
```

### Customize Release Notes
Edit the `body:` section in the workflow files.

## 🐛 Troubleshooting

### Build Fails
1. Check the Actions tab for error logs
2. Verify `gradlew` has execute permissions
3. Ensure all dependencies are in `build.gradle.kts`

### APK Not Generated
1. Check if `assembleDebug` task succeeded
2. Verify path: `app/build/outputs/apk/debug/app-debug.apk`

### Signing Fails
1. Verify all 4 secrets are added correctly
2. Check that base64 conversion was done correctly
3. Ensure passwords match your keystore

## 📊 Build Status

Add this badge to your README:
```markdown
![Android CI](https://github.com/YOUR_USERNAME/YOUR_REPO/workflows/Android%20CI%2FCD/badge.svg)
```

## 🎯 Next Steps

1. ✅ Commit and push to trigger first build
2. ✅ Check Actions tab for build progress
3. ✅ Download APK from Releases page
4. ✅ (Optional) Configure APK signing for production
5. ✅ Share release link with testers/users

## 📚 Learn More

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Android Build Documentation](https://developer.android.com/studio/build)
- [APK Signing Guide](https://developer.android.com/studio/publish/app-signing)

---

## Current Workflow Status

Once you push this to GitHub, every commit will automatically:
1. **Build** the app (takes ~5-10 minutes)
2. **Create** a new release
3. **Upload** the APK file
4. **Notify** you when complete

**No manual intervention needed!** 🎉

