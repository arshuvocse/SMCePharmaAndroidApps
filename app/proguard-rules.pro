# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:-keepclassmembers class com.creatrix.salessolution.Model {
  #public *;
#}
-keep class com.creatrix.salessolution.Model.*
-keep class com.creatrix.salessolution.Customer.*
-keep class com.creatrix.salessolution.ResposeModel.*

-keep class com.creatrix.salessolution.Activity.Attendance.Model.*
-keep class com.creatrix.salessolution.Activity.Approval.TourPlan.Model.*
-keep class com.creatrix.salessolution.Activity.Approval.VisitPlan.Model.*
-keep class com.creatrix.salessolution.Activity.Doctor.Approval.Model.*
-keep class com.creatrix.salessolution.Activity.Doctor.TourePlan.Model.*
-keep class com.creatrix.salessolution.Activity.Doctor.VisitPlan.Model.*
-keep class com.creatrix.salessolution.Activity.DWSP.Model.*
-keep class com.creatrix.salessolution.Activity.Expense.Model.*
-keep class com.creatrix.salessolution.Activity.MileageClaim.Model.*
-keep class com.creatrix.salessolution.Activity.OrderProcess.Model.*
-keep class com.creatrix.salessolution.Activity.SelfReports.ExpenseSummery.Model.*
-keep class com.creatrix.salessolution.Activity.SelfReports.SalesReport.Model.*
-keep class com.creatrix.salessolution.Activity.Team.Model.*


#Retrofit
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepattributes AnnotationDefault
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn javax.annotation.**
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response
#Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
  *** rewind();
}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile