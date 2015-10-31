# To enable ProGuard in your project, edit project.properties
# to define the proguard.config property as described in that file.
#
# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in ${sdk.dir}/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the ProGuard
# include property in project.properties.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-dontoptimize
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
#-dontpreverify
-verbose
-dontwarn
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-keepattributes InnerClasses,LineNumberTable

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.app.View
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference

-keep class android.net.http.SslError
-keep class android.webkit.**{*;}

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class **.R$* {   
    *;   
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#shuzhi proguard rules
-keepclassmembers class * {
    public <methods>;
}

-dontwarn android.support.**
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
    public <fields>;
}
-keepattributes Signature#过滤泛型

-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** { *; }

-dontwarn com.umeng.**
-keep class com.umeng.**{*;}
-keep public class co.lujun.shuzhi.R$*{
    public static final int *;
}
-keep class com.umeng.message.* {
        public <fields>;
        public <methods>;
}
-keep class com.umeng.message.protobuffer.* {
        public <fields>;
        public <methods>;
}
-keep class com.squareup.wire.* {
        public <fields>;
        public <methods>;
}
-keep class org.android.agoo.impl.*{
        public <fields>;
        public <methods>;
}
-keep class org.android.agoo.service.* {*;}
-keep class org.android.spdy.**{*;}
-keep public class co.lujun.shuzhi.R$*{
    public static final int *;
}
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

# For RxJava:
-dontwarn org.mockito.**
-dontwarn org.junit.**
-dontwarn org.robolectric.**

-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }

-dontwarn rx.**
-dontwarn retrofit.**
-keep class rx.** { *; }
-keep class retrofit.** { *; }
#-keepclasseswithmembers class * {
#    @retrofit.http.* <methods>;
#}
-dontwarn com.loopj.android.http.**
-keep class com.loopj.android.http.** { *; }

-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class co.lujun.shuzhi.bean.** { *; }

-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}

#-keep class org.apache.http.**
-dontwarn org.android.agoo.net.**
-dontwarn u.upd.**
-dontwarn u.aly.**
-dontwarn com.ta.**
-dontwarn com.android.volley.**
-keep class com.android.volley.** {*;}
-dontwarn com.tencent.**
-keep class com.tencent.mm.sdk.** {*;}
-dontwarn com.sina.weibo.sdk.**
-keep class com.sina.**{*;}
-keep class * extends android.app.Dialog
-keepattributes Exceptions
