# Build Guide / 构建指南

[English](#english) | [中文](#中文)

---

## English

### Automated Build (GitHub Actions)

The project uses GitHub Actions for automated APK building with intelligent version management.

#### Version Management

**Non-tag builds** (branches):
- Version format: `{base_version}.git.{short_sha}`
- Example: `1.0.0.git.a1b2c3d`
- Version code: `{base_code} + {commit_count}`

**Tag builds** (releases):
- Version format: Extracted from tag (e.g., `v1.0.0` → `1.0.0`)
- Automatically updates `gradle.properties` with the new version
- Commits and pushes the version update to main branch

#### Build Behavior

- **Push to any branch**: Builds debug + signed release APKs with git version
- **Pull Requests**: Builds debug + unsigned release APKs (for security)
- **Tags (v*)**: 
  - Updates version in `gradle.properties`
  - Builds signed APKs with release version
  - Creates GitHub release
  - Pushes version update to main branch

#### Creating a Release

To create a release, push a tag starting with `v`:

```bash
# Update version and create tag
git tag v1.0.0
git push origin v1.0.0
```

This will:
1. Extract version from tag (v1.0.0 → 1.0.0)
2. Build debug and signed release APKs
3. Create a GitHub release with the tag name
4. Update `gradle.properties` with the new version
5. Push version update to main branch (with `[skip ci]` to avoid triggering another build)

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

项目使用 GitHub Actions 进行自动化 APK 构建，具有智能版本管理功能。

#### 版本管理

**非标签构建**（分支）：
- 版本格式：`{基础版本}.git.{短哈希}`
- 示例：`1.0.0.git.a1b2c3d`
- 版本代码：`{基础代码} + {提交数}`

**标签构建**（发行版）：
- 版本格式：从标签提取（例如：`v1.0.0` → `1.0.0`）
- 自动更新 `gradle.properties` 中的版本号
- 提交并推送版本更新到 main 分支

#### 构建行为

- **推送到任何分支**：构建 debug + 已签名的 release APK（带 git 版本）
- **Pull Requests**：构建 debug + 未签名的 release APK（出于安全考虑）
- **标签 (v*)**：
  - 更新 `gradle.properties` 中的版本号
  - 构建已签名的 APK（带发行版本号）
  - 创建 GitHub 发行版
  - 推送版本更新到 main 分支

#### 创建发行版

要创建发行版，请推送以 `v` 开头的标签：

```bash
# 更新版本并创建标签
git tag v1.0.0
git push origin v1.0.0
```

这将会：
1. 从标签提取版本号（v1.0.0 → 1.0.0）
2. 构建 debug 和已签名的 release APK
3. 使用标签名称创建 GitHub 发行版
4. 更新 `gradle.properties` 中的版本号
5. 推送版本更新到 main 分支（带 `[skip ci]` 标记以避免触发新的构建）

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
