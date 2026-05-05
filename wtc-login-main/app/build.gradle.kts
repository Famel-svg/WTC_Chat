import java.io.File

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.gms.google-services")
}

fun loadRepoDotEnv(repoRoot: File): Map<String, String> {
    val map = mutableMapOf<String, String>()
    val envFile = repoRoot.resolve("../.env")
    if (!envFile.isFile) return map
    envFile.readLines().forEach { line ->
        val trimmed = line.trim()
        if (trimmed.isEmpty() || trimmed.startsWith("#")) return@forEach
        val eq = trimmed.indexOf('=')
        if (eq <= 0) return@forEach
        val key = trimmed.substring(0, eq).trim()
        var value = trimmed.substring(eq + 1).trim()
        if (value.length >= 2
            && ((value.startsWith("\"") && value.endsWith("\"")) || (value.startsWith("'") && value.endsWith("'")))
        ) {
            value = value.substring(1, value.length - 1)
        }
        map[key] = value
    }
    return map
}

android {
    namespace = "br.com.fiap.wtchat"
    compileSdk = 36

    defaultConfig {
        applicationId = "br.com.fiap.wtchat"
        minSdk = 27
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val env = loadRepoDotEnv(rootProject.projectDir)
        buildConfigField("String", "FIREBASE_PROJECT_DISPLAY_NAME", "\"${env["FIREBASE_PROJECT_DISPLAY_NAME"] ?: ""}\"")
        buildConfigField("String", "GOOGLE_CLOUD_PROJECT_ID", "\"${env["GOOGLE_CLOUD_PROJECT_ID"] ?: ""}\"")
        buildConfigField("String", "GOOGLE_CLOUD_PROJECT_NUMBER", "\"${env["GOOGLE_CLOUD_PROJECT_NUMBER"] ?: ""}\"")
        buildConfigField("String", "FIREBASE_WEB_API_KEY", "\"${env["FIREBASE_WEB_API_KEY"] ?: ""}\"")
        buildConfigField("String", "API_BASE_URL", "\"${env["API_BASE_URL"] ?: "http://10.0.2.2:8080/"}\"") // Adicionado
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation(platform("androidx.compose:compose-bom:2024.09.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.09.00"))
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.google.code.gson:gson:2.11.0")

    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("com.google.firebase:firebase-database-ktx") // Adicionado

    // As dependências STOMP/RxJava podem ser removidas se não forem mais usadas
    // implementation("com.github.NaikSoftware:StompProtocolAndroid:1.6.6")
    // implementation("io.reactivex.rxjava2:rxjava:2.2.21")
    // implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
}