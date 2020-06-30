### Forstream ###
-keep class io.forstream.api.model.** { *; }
-keep class io.forstream.api.enums.** { *; }
-keep class io.forstream.api.exception.ApiException { *; }
-keep public class * extends java.lang.Exception

-keepclassmembers class io.forstream.api.model.** implements android.os.Parcelable {
  static ** CREATOR;
}
-keepclassmembers class io.forstream.api.model.** implements java.io.Serializable {
  static final long serialVersionUID;
  private static final java.io.ObjectStreamField[] serialPersistentFields;
  !static !transient <fields>;
  !private <fields>;
  !private <methods>;
  private void writeObject(java.io.ObjectOutputStream);
  private void readObject(java.io.ObjectInputStream);
  java.lang.Object writeReplace();
  java.lang.Object readResolve();
}

-keepattributes Signature
-keepattributes Annotation
-keepattributes RuntimeVisibleAnnotations
-keepattributes RuntimeVisibleParameterAnnotations
-keepattributes InnerClasses
-keepattributes EnclosingMethod
-keepattributes Exceptions
-keepattributes SourceFile
-keepattributes LineNumberTable
-renamesourcefileattribute SourceFile

-dontwarn javax.annotation.**

### Iconics ###
-keep class .R
-keep class **.R$* {
    <fields>;
}

### Ucrop ###
-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }

### Crashlytics ###
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**

### Retrofit ###
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn kotlin.Unit
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

### OkHttp ###
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
-dontwarn org.codehaus.mojo.animal_sniffer.*
-dontwarn okhttp3.internal.platform.ConscryptPlatform
