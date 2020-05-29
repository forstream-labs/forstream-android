### Project Source ###

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
-keepattributes InnerClasses
-keepattributes Exceptions
-keepattributes SourceFile
-keepattributes LineNumberTable
-renamesourcefileattribute SourceFile

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