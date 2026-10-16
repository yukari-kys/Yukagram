plugins {
    id("java-toolchain-convention")
    id(libs.plugins.android.library.get().pluginId)
    id("tgx-module")
}

dependencies {
    implementation(libs.androidx.annotation)
}

android {
  namespace = "org.drinkless.tdlib"

  defaultConfig {
    consumerProguardFiles("consumer-rules.pro")
  }

  externalNativeBuild {
    cmake {
      path = file("CMakeLists.txt")
    }
  }

  lint {
    disable += "ScopedStorage"
  }
}
