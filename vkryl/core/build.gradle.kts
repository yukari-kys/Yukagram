plugins {
  id("java-toolchain-convention")
  id(libs.plugins.android.library.get().pluginId)
  id("tgx-module")
}

dependencies {
  implementation(
    libs.androidx.core.ktx.latest
  )
}

android {
  namespace = "me.vkryl.core"
}