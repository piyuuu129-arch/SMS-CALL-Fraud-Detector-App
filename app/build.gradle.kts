plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
}

android {
    namespace = "com.example.frauddetectorapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.frauddetectorapp"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
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

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    implementation("org.tensorflow:tensorflow-lite:2.14.0")
    implementation("org.tensorflow:tensorflow-lite-support:0.4.4")
    implementation("com.microsoft.onnxruntime:onnxruntime-android:1.20.0")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.cardview:cardview:1.0.0")


    dependencies {

        implementation("androidx.compose.ui:ui:1.6.0")
        implementation("androidx.compose.material3:material3:1.2.0")
        implementation("androidx.compose.ui:ui-tooling-preview:1.6.0")

        debugImplementation("androidx.compose.ui:ui-tooling:1.6.0")

        implementation("androidx.activity:activity-compose:1.8.2")

        implementation("androidx.compose.material:material-icons-extended")


        implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    }
}