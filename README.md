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

### APK Signing

Release APKs are signed automatically in GitHub Actions using repository secrets. For local signed builds, create a `keystore.properties` file in the project root:

```properties
storeFile=path/to/your/keystore.jks
storePassword=your_keystore_password
keyAlias=your_key_alias
keyPassword=your_key_password
```

**Note:** The `keystore.properties` file and keystore files are excluded from version control for security.

#### Setting up GitHub Actions Secrets

To enable APK signing in CI/CD, configure the following secrets in your repository settings:

1. `KEYSTORE_BASE64`: Base64-encoded keystore file
   ```bash
   base64 -i your-keystore.jks | pbcopy  # macOS
   base64 -w 0 your-keystore.jks         # Linux
   ```
2. `KEYSTORE_PASSWORD`: Keystore password
3. `KEY_ALIAS`: Key alias name
4. `KEY_PASSWORD`: Key password

#### Creating a Keystore

If you don't have a keystore yet, create one with:

```bash
keytool -genkey -v -keystore remotemate.jks -keyalg RSA -keysize 2048 -validity 10000 -alias remotemate
```

