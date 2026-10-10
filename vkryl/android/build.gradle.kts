plugins {
  id("java-toolchain-convention")
  id(libs.plugins.android.library.get().pluginId)
  id("tgx-module")
}

dependencies {
  implementation(
    libs.androidx.core.ktx.latest
  )
  implementation(project(":vkryl:core"))
}

android {
  namespace = "me.vkryl.android"

  lint {
    disable += "ObsoleteSdkInt"
  }
}