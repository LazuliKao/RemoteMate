# RemoteMate

[English](#english) | [中文](README_zh.md)

---

## English

An Xposed module that enhances remote desktop applications with advanced keyboard support and better user experience.

### ✨ Features

#### 🎹 Enhanced Keyboard Support

RemoteMate intercepts system-level keyboard events to provide seamless keyboard input in remote desktop applications, solving common keyboard compatibility issues.

**Key Features:**
- **System-level Keyboard Interception**: Bypass Android system shortcuts and deliver all key events directly to remote desktop apps
- **External Keyboard Only Mode**: Option to intercept only external keyboard events, preserving built-in keyboard functionality
- **Target App Filtering**: Selectively enable keyboard interception only when specific remote desktop apps are in foreground
- **Customizable App List**: Choose which apps should trigger the keyboard enhancement

**Default Supported Apps:**
- Microsoft Remote Desktop (androidx & android versions)
- RealVNC Viewer
- TeamViewer

#### 🪟 Microsoft Remote Desktop Enhancements

Specialized optimizations for Microsoft Remote Desktop app:

- **Hide Soft Keyboard**: Automatically prevents the soft keyboard from appearing, providing a cleaner desktop experience
- **Force Scancode Mode**: Enables scancode keyboard mode for better key mapping compatibility with Windows systems

### 📋 Requirements

- Android device with root access
- Xposed Framework (LSPosed, EdXposed, or compatible)
- Supported remote desktop application (see list above)

### 🚀 Installation

1. Install an Xposed framework (LSPosed recommended)
2. Download and install RemoteMate APK
3. Enable the module in your Xposed manager
4. Reboot your device
5. Open RemoteMate to configure settings

### ⚙️ Configuration

Launch RemoteMate and configure:

1. **Keyboard Hook Settings**:
   - Enable/disable keyboard interception
   - Toggle external keyboard only mode
   - Enable/disable target app filtering
   - Select which apps should trigger interception

2. **Windows App Hook** (Microsoft Remote Desktop):
   - Toggle soft keyboard hiding
   - Toggle force scancode mode

### 📖 Documentation

- [Build Guide](docs/BUILD.md) - Instructions for building from source
- [APK Signing Guide](docs/APK_SIGNING.md) - APK signing configuration

### 🛠️ Technology Stack

- **YukiHookAPI**: Modern Xposed development framework
- **KavaRef**: Advanced Java reflection utilities
- **Kotlin**: Primary development language

### 📄 License

See [LICENSE](LICENSE) file for details.

### 🤝 Contributing

Issues and pull requests are welcome!

---

**Note**: This module requires Xposed framework and root access. Use at your own risk.
