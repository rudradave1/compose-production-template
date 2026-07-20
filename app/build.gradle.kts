plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.compose.compiler)
    id("io.github.rudradave1.proguardlint")
}

proguardLint {
    // Exclude root package entry points from audit
    dangerZones.set(setOf("com.rudradave.composeproductiontemplate.features", "com.rudradave.composeproductiontemplate.core"))
    failOnError.set(true)
}

tasks.withType<io.github.rudradave1.proguardlint.ProguardLintTask>().configureEach {
    val buildDir = layout.buildDirectory
    mappingFile.set(buildDir.file("outputs/mapping/release/mapping.txt"))
    seedsFile.set(buildDir.file("outputs/mapping/release/seeds.txt"))
    // Change: dependsOn explicitly links the task as an input provider
    dependsOn("minifyReleaseWithR8")
}

android {
    namespace = "com.rudradave.composeproductiontemplate"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.rudradave.composeproductiontemplate"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:network"))
    implementation(project(":core:database"))
    implementation(project(":features:samplefeature"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.google.material)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(libs.work.runtime.ktx)

    debugImplementation(libs.androidx.compose.ui.tooling)
}

kapt {
    correctErrorTypes = true
}
