# RemoteMate

[English](README.md) | [中文](#中文)

---

## 中文

一个增强远程桌面应用的 Xposed 模块，提供高级键盘支持和更好的用户体验。

### ✨ 功能特性

#### 🎹 增强键盘支持

RemoteMate 通过拦截系统级键盘事件，为远程桌面应用提供无缝的键盘输入体验，解决常见的键盘兼容性问题。

**核心功能：**
- **系统级键盘拦截**：绕过 Android 系统快捷键，将所有按键事件直接传递给远程桌面应用
- **仅外接键盘模式**：可选择仅拦截外接键盘事件，保留内置键盘的原有功能
- **目标应用过滤**：仅在特定远程桌面应用处于前台时启用键盘拦截
- **可自定义应用列表**：自由选择哪些应用应该触发键盘增强功能

**默认支持的应用：**
- Microsoft Remote Desktop（androidx 和 android 版本）
- RealVNC Viewer
- TeamViewer

#### 🪟 Microsoft Remote Desktop 专项优化

针对 Microsoft Remote Desktop 应用的特殊优化：

- **隐藏软键盘**：自动阻止软键盘弹出，提供更清爽的桌面体验
- **强制 Scancode 模式**：启用扫描码键盘模式，提供与 Windows 系统更好的按键映射兼容性

### 📋 系统要求

- 已 Root 的 Android 设备
- Xposed 框架（推荐 LSPosed、EdXposed 或兼容框架）
- 支持的远程桌面应用程序（见上述列表）

### 🚀 安装步骤

1. 安装 Xposed 框架（推荐 LSPosed）
2. 下载并安装 RemoteMate APK
3. 在 Xposed 管理器中启用本模块
4. 重启设备
5. 打开 RemoteMate 进行设置配置

### ⚙️ 配置说明

启动 RemoteMate 进行配置：

1. **键盘钩子设置**：
   - 启用/禁用键盘拦截功能
   - 切换仅外接键盘模式
   - 启用/禁用目标应用过滤
   - 选择哪些应用应该触发拦截

2. **Windows 应用钩子**（Microsoft Remote Desktop）：
   - 切换软键盘隐藏功能
   - 切换强制 Scancode 模式

### 📖 文档

- [构建指南](docs/BUILD.md) - 从源代码构建的说明
- [APK 签名指南](docs/APK_SIGNING.md) - APK 签名配置说明

### 🛠️ 技术栈

- **YukiHookAPI**：现代化的 Xposed 开发框架
- **KavaRef**：高级 Java 反射工具库
- **Kotlin**：主要开发语言

### 📄 许可证

详见 [LICENSE](LICENSE) 文件。

### 🤝 贡献

欢迎提交 Issue 和 Pull Request！

---

**注意**：本模块需要 Xposed 框架和 Root 权限。使用风险自负。
