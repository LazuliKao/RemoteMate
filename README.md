# RemoteMate

An Xposed module for enhanced remote desktop keyboard support.

## Build

The project uses GitHub Actions for automated APK building. Every push to `main` or `develop` branch will automatically build both debug and release APKs.

### Creating a Release

To create a release, push a tag starting with `v`:

```bash
git tag v1.0.0
git push origin v1.0.0
```

This will:
1. Build debug and release APKs
2. Create a GitHub release with the tag name
3. Upload the APKs to the release

### Manual Build

To build locally:

```bash
# Debug APK
./gradlew assembleDebug

# Release APK
./gradlew assembleRelease
```

The built APKs will be in `app/build/outputs/apk/`.

