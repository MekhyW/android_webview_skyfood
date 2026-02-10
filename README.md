# Android WebView SkyFood

An Android application that provides a WebView wrapper for integrating with the Autoxing Robot SDK. This app demonstrates how to create a bridge between native Android code and JavaScript for robot control and interaction.

## Project Structure

```
android_webview_skyfood/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/autoxing/delivery/
│   │       │   └── MainActivity.java          # Main activity with WebView
│   │       ├── assets/dist/
│   │       │   ├── index.html                 # Web interface
│   │       │   ├── robot-js-sdk.js            # Autoxing Robot SDK
│   │       │   └── axios.min.js               # HTTP client
│   │       ├── res/                           # Android resources
│   │       └── AndroidManifest.xml
│   └── build.gradle                           # App-level build config
├── build.gradle                               # Project-level build config
├── settings.gradle
└── README.md
```

## Getting Started

### Prerequisites

- Android Studio Arctic Fox or later
- JDK 11 or higher
- Android SDK with API level 32

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/MekhyW/android_webview_skyfood.git
   cd android_webview_skyfood
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an Existing Project"
   - Navigate to the cloned directory

3. **Sync Gradle**
   - Android Studio should automatically sync Gradle files
   - If not, click "Sync Project with Gradle Files" in the toolbar

4. **Run the app**
   - Connect an Android device or start an emulator
   - Click the "Run" button or press `Shift + F10`

## Configuration

### Autoxing SDK Setup

To use the Autoxing Robot SDK functionality, you need to configure the credentials in `app/src/main/assets/dist/index.html`:

```javascript
const appId = "your-app-id"
const appSecret = "your-app-secret"
const robotId = "your-robot-id"
```

### Changing the Web Content

The app loads `file:///android_asset/dist/index.html` by default. To use a remote URL:

1. Open `MainActivity.java`
2. Locate the `loadWebView()` method (line 100)
3. Replace the URL:
   ```java
   webView.loadUrl("https://your-url-here");
   ```

## Usage

### JavaScript to Android Communication

The app exposes Android methods to JavaScript through the `app` interface:

```javascript
// Call Android method and get return value
let result = app.actionFromJsHello("Hello from JS");

// Refresh the WebView
app.actionFromJsWebRefresh();
```

### Android to JavaScript Communication

Android can call JavaScript functions in the WebView:

```java
webView.loadUrl("javascript:callJSHello(" + Math.random() + ")");
```

## Testing

The bundled demo (`index.html`) includes:

- Android ↔ JavaScript communication test buttons
- Autoxing Robot SDK initialization example
- POI (Point of Interest) list retrieval demo

## Building for Production

To create a release build:

1. **Generate a signing key** (if you don't have one):
   ```bash
   keytool -genkey -v -keystore my-release-key.keystore -alias my-key-alias -keyalg RSA -keysize 2048 -validity 10000
   ```

2. **Configure signing** in `app/build.gradle`

3. **Build the APK**:
   ```bash
   ./gradlew assembleRelease
   ```

The APK will be generated at `app/build/outputs/apk/release/app-release.apk`

---

**Note**: This application is configured as a launcher app and can be set as the default home screen on Android devices, making it suitable for dedicated car pad or kiosk deployments.
