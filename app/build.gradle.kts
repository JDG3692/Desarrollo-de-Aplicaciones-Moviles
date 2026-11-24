import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

// La clave de firma y su contraseña viven fuera del repositorio, que es público.
// Si no existen en este equipo, la variante release se compila sin firmar.
val archivoFirma = File(
    System.getProperty("user.home"),
    ".agenda-personal-firma/keystore.properties"
)
val datosFirma = Properties().apply {
    if (archivoFirma.exists()) {
        archivoFirma.inputStream().use { load(it) }
    }
}

android {
    namespace = "com.example.agendapersonal"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.agendapersonal"
        minSdk = 24
        targetSdk = 37
        versionCode = 3
        versionName = "1.1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        if (archivoFirma.exists()) {
            create("release") {
                storeFile = File(archivoFirma.parentFile, "release.p12")
                storeType = "PKCS12"
                storePassword = datosFirma.getProperty("password")
                keyAlias = "agenda-personal"
                keyPassword = datosFirma.getProperty("password")
            }
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.findByName("release")
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    testOptions {
        unitTests {
            // Robolectric necesita los recursos y el manifiesto de la app.
            isIncludeAndroidResources = true
        }
    }
    buildFeatures {
        compose = true
    }
}

// Guarda el esquema de cada versión de Room para poder revisar sus cambios.
ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.junit)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.test.core)
    testImplementation(platform(libs.androidx.compose.bom))
    testImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)

    implementation("androidx.compose.material:material-icons-core")
    implementation("androidx.compose.material:material-icons-extended")

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
}