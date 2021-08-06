-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.SerializationKt
-keep,includedescriptorclasses class io.dotanuki.norris.rest.**$$serializer { *; }
-keepclassmembers class io.dotanuki.norris.rest.** {
    *** Companion;
}
-keepclasseswithmembers class io.dotanuki.norris.rest.** {
    kotlinx.serialization.KSerializer serializer(...);
}