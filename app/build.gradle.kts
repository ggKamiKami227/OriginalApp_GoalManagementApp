plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.ver2"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.ver2"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    // ★ 修正: sourceSets の位置を android ブロックの中に
    sourceSets.named("main") {
        res.srcDirs(
            "src/main/res",               // 標準の res ディレクトリ
            "src/main/res/layouts",       // カスタムの layouts ディレクトリ
            "src/main/res/layouts/will_can_must", // サブディレクトリ
            "src/main/res/layouts/benchMarking",
            "src/main/res/layouts/smart",
            "src/main/res/layouts/memo_goal",
            "src/main/res/layouts/CreateGoal",
            "src/main/res/layouts/confirmGoal",
            "src/main/res/layouts/task",
            "src/main/res/layouts/MandalaChart",
            "src/main/res/layouts/Purpose"
        )
    }

}

dependencies {
    implementation(libs.room.runtime)
    implementation(libs.gridlayout) // 最新バージョンを確認してください
    annotationProcessor(libs.room.compiler) // 最新バージョンを確認してください
    //implementation(libs.gson)
    implementation ("com.google.code.gson:gson:2.8.9")  // ✅ 手動で追加
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.room.common)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}