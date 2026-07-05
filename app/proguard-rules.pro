# ProGuard rules for Paavani AI
-keepattributes SourceFile,LineNumberTable,JavascriptInterface

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider

# Keep JS Bridge
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

-keep class com.viral.ai.WebAppInterface { *; }
