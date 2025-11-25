# Build Guide / 构建指南

[English](#english) | [中文](#中文)

---

## English

### Automated Build (GitHub Actions)

The project uses GitHub Actions for automated APK building. Every push to any branch will automatically build both debug and signed release APKs (except for Pull Requests, which build unsigned releases for security).

#### Build Behavior

- **Push to any branch**: Builds debug + signed release APKs
- **Pull Requests**: Builds debug + unsigned release APKs (for security)
- **Tags**: Builds signed APKs + creates GitHub release

#### Creating a Release

To create a release, push a tag starting with `v`:

```bash
git tag v1.0.0
git push origin v1.0.0
```

This will:
1. Build debug and signed release APKs
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

Release APKs are automatically signed in CI/CD for all builds except Pull Requests. For detailed instructions on:
- Setting up local signing for development
- Configuring CI/CD signing with GitHub Secrets  
- Verifying APK signatures
- Troubleshooting signing issues

Please refer to the [APK Signing Configuration Guide](APK_SIGNING.md).

**Note**: Pull Request builds produce unsigned release APKs to protect signing credentials.

---

## 中文

### 自动构建 (GitHub Actions)

项目使用 GitHub Actions 进行自动化 APK 构建。推送到任何分支都会自动构建 debug 和已签名的 release APK（Pull Request 除外，出于安全考虑会构建未签名的 release 版本）。

#### 构建行为

- **推送到任何分支**：构建 debug + 已签名的 release APK
- **Pull Requests**：构建 debug + 未签名的 release APK（出于安全考虑）
- **标签**：构建已签名的 APK + 创建 GitHub 发行版

#### 创建发行版

要创建发行版，请推送以 `v` 开头的标签：

```bash
git tag v1.0.0
git push origin v1.0.0
```

这将会：
1. 构建 debug 和已签名的 release APK
2. 使用标签名称创建 GitHub 发行版
3. 将 APK 上传到发行版

### 手动构建

本地构建方法：

```bash
# Debug APK
./gradlew assembleDebug

# Release APK
./gradlew assembleRelease
```

构建的 APK 文件将位于 `app/build/outputs/apk/` 目录。

### APK 签名

Release 版本 APK 在 CI/CD 中会自动签名（Pull Request 除外）。有关以下内容的详细说明：
- 配置开发环境的本地签名
- 配置 GitHub Secrets 用于 CI/CD 签名
- 验证 APK 签名
- 故障排除

请参阅 [APK 签名配置指南](APK_SIGNING.md)。

**注意**：Pull Request 构建会生成未签名的 release APK，以保护签名凭据。
