# Argon2 cruza a codigo nativo por JNI: la parte en C busca las clases y los
# metodos por su nombre exacto, asi que si R8 los renombra deja de encontrarlos.
# Esta regla no se puede tocar.
-keep class com.lambdapioneer.argon2kt.** { *; }

# Mantener la clase Application para que R8 no la ofusque en AGP 9
-keep class com.jlnavas3.bovedalocal.BovedaApp

# kotlinx.serialization trae sus propias reglas dentro del .jar, y aqui los
# serializadores se piden a mano (ContenidoBoveda.serializer()), no por reflexion,
# asi que R8 los rastrea solo. Lo unico que hay que sujetar son los serializadores
# generados de los modelos, que si se pierden se lleva por delante la boveda.
-keepattributes *Annotation*, InnerClasses
-keep,includedescriptorclasses class com.jlnavas3.bovedalocal.data.**$$serializer { *; }
-keepclassmembers class com.jlnavas3.bovedalocal.data.** {
    *** Companion;
}
-keepclasseswithmembers class com.jlnavas3.bovedalocal.data.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Credential Transfer / Provider Events (androidx.credentials.providerevents)
# Las clases de providerevents y play-services se instancian por reflexión mediante ProviderFactory
# a través de nombres de clase definidos en el AndroidManifest.
-keep class androidx.credentials.providerevents.** { *; }
-keep interface androidx.credentials.providerevents.** { *; }
-keep class com.google.android.gms.identitycredentials.** { *; }
-keep interface com.google.android.gms.identitycredentials.** { *; }

# Modelos CXF (FIDO Credential Exchange Format) serializados con kotlinx.serialization
-keep,includedescriptorclasses class com.jlnavas3.bovedalocal.cxf.**$$serializer { *; }
-keepclassmembers class com.jlnavas3.bovedalocal.cxf.** {
    *** Companion;
}
-keepclasseswithmembers class com.jlnavas3.bovedalocal.cxf.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep class com.jlnavas3.bovedalocal.cxf.** { *; }

# Numeros de linea en las trazas, para que un informe de fallo siga sirviendo de
# algo. El nombre del fichero se sustituye por uno falso: guardarlo de verdad
# seria regalar la estructura del codigo, que es justo lo que se quiere esconder.
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
