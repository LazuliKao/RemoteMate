# APK签名配置指南 / APK Signing Configuration Guide

## 概述 / Overview

本项目已配置APK签名功能，支持在CI/CD和本地环境中对Release版本APK进行签名。

This project has been configured with APK signing support for both CI/CD and local builds.

## 本地签名配置 / Local Signing Setup

### 1. 创建密钥库 / Create Keystore

如果您还没有密钥库文件，请使用以下命令创建：

If you don't have a keystore file yet, create one with:

```bash
keytool -genkey -v -keystore remotemate.jks -keyalg RSA -keysize 2048 -validity 10000 -alias remotemate
```

按照提示输入密码和相关信息。请妥善保管密钥库文件和密码！

Follow the prompts to enter passwords and information. Keep your keystore file and passwords safe!

### 2. 配置签名属性 / Configure Signing Properties

1. 复制模板文件 / Copy the template file (from project root):
   ```bash
   cp keystore.properties.template keystore.properties
   ```

2. 编辑 `keystore.properties` 文件，填入您的签名信息 / Edit `keystore.properties` with your signing details:
   ```properties
   storeFile=path/to/your/remotemate.jks
   storePassword=your_keystore_password
   keyAlias=remotemate
   keyPassword=your_key_password
   ```

**重要提示 / Important**: `keystore.properties` 文件已被添加到 `.gitignore`，不会被提交到版本控制系统。请勿将此文件或密钥库文件提交到仓库！

The `keystore.properties` file is in `.gitignore` and will not be committed. Never commit this file or keystore files to the repository!

### 3. 构建签名的APK / Build Signed APK

配置完成后，构建Release APK时会自动应用签名：

Once configured, Release APKs will be automatically signed:

```bash
./gradlew assembleRelease
```

签名的APK将位于：`app/build/outputs/apk/release/`

The signed APK will be in: `app/build/outputs/apk/release/`

## CI/CD签名配置 / CI/CD Signing Setup

### 配置GitHub Secrets / Configure GitHub Secrets

在GitHub仓库设置中添加以下Secrets：

Add the following secrets in your GitHub repository settings:

1. **KEYSTORE_BASE64**: Base64编码的密钥库文件
   ```bash
   # macOS
   base64 -i your-keystore.jks | pbcopy
   
   # Linux
   base64 -w 0 your-keystore.jks
   ```

2. **KEYSTORE_PASSWORD**: 密钥库密码

3. **KEY_ALIAS**: 密钥别名

4. **KEY_PASSWORD**: 密钥密码

### 自动签名 / Automatic Signing

配置完成后，以下情况将自动对APK进行签名：

Once configured, APKs will be automatically signed in:

- 推送到 `main` 分支 / Pushes to `main` branch
- 创建标签 / Creating tags (for releases)

Pull Request中不会进行签名，以保护密钥安全。

Pull requests will not sign APKs to protect the signing keys.

## 验证签名 / Verify Signature

可以使用以下命令验证APK签名：

Verify APK signature with:

```bash
# 查看签名信息 / View signature info (replace with actual APK filename)
jarsigner -verify -verbose -certs app/build/outputs/apk/release/*.apk

# 或使用apksigner / Or use apksigner
apksigner verify --print-certs app/build/outputs/apk/release/*.apk
```

## 故障排除 / Troubleshooting

### 本地构建未签名 / Local Build Not Signed

如果Release APK没有被签名：

If Release APK is not signed:

1. 确认 `keystore.properties` 文件存在且配置正确
   Verify `keystore.properties` file exists and is configured correctly

2. 确认密钥库文件路径正确
   Verify keystore file path is correct

3. 检查密钥库密码和别名是否正确
   Check if keystore passwords and alias are correct

### CI/CD构建失败 / CI/CD Build Fails

1. 确认所有GitHub Secrets都已正确配置
   Verify all GitHub Secrets are properly configured

2. 确认KEYSTORE_BASE64是完整的base64编码（无换行符）
   Verify KEYSTORE_BASE64 is complete base64 encoding (no line breaks)

3. 检查GitHub Actions日志中的详细错误信息
   Check GitHub Actions logs for detailed error messages

## 安全建议 / Security Recommendations

1. **永远不要**将密钥库文件或密码提交到版本控制
   **Never** commit keystore files or passwords to version control

2. 定期更换签名密码
   Regularly update signing passwords

3. 为不同的应用使用不同的密钥库
   Use different keystores for different applications

4. 备份密钥库文件到安全的地方
   Backup keystore files to a secure location

5. 限制对GitHub Secrets的访问权限
   Restrict access to GitHub Secrets
