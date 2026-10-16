plugins {
  id("java-toolchain-convention")
  id(libs.plugins.android.library.get().pluginId)
  id("tgx-module")
}

dependencies {
  // Firebase: https://firebase.google.com/support/release-notes/android
  implementation(
    libs.google.firebase.messaging.latest
  ) {
    exclude(group = "com.google.firebase", module = "firebase-core")
    exclude(group = "com.google.firebase", module = "firebase-analytics")
    exclude(group = "com.google.firebase", module = "firebase-measurement-connector")
  }
  implementation(
    libs.google.play.services.base.latest
  )
  implementation(project(":tdlib"))
  api(project(":extension:bridge"))
}

android {
  namespace = "tgx.extension"
}
