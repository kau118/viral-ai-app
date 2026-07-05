# ViralAI Android Project

This is a WebView wrapper for the ViralAI React application.

## How to build the APK

1. Open Android Studio.
2. Select **Open** and choose the `android` folder in this project.
3. Wait for Gradle to sync.
4. Ensure your React app is running (e.g., `npm run dev`) if you want to test in the emulator using `http://10.0.2.2:5173`.
5. To generate a production APK:
   - Build the React app: `npm run build`.
   - Copy the contents of the `dist` folder to `android/app/src/main/assets` (create the folder if it doesn't exist).
   - Change `webView.loadUrl("http://10.0.2.2:5173")` to `webView.loadUrl("file:///android_asset/index.html")` in `MainActivity.kt`.
   - In Android Studio, go to **Build > Build Bundle(s) / APK(s) > Build APK(s)**.

## Prerequisites
- Android SDK
- Gradle
- Android Studio
