package tgx.gradle.source

import ApplicationConfig
import BuildVersions
import PullRequest
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.logging.Logging
import org.gradle.api.provider.ValueSource
import org.gradle.api.provider.ValueSourceParameters
import tgx.gradle.fatal
import tgx.gradle.getIntOrThrow
import tgx.gradle.getOrThrow
import tgx.gradle.loadProperties
import tgx.gradle.task.isWindowsHost
import tgx.gradle.task.validateMsys2Dir
import java.io.File
import java.util.*

private fun getOrDefault(properties: Properties, key: String, defaults: Properties): String {
  return properties.getProperty(key, null) ?: defaults.getOrThrow(key)
}

abstract class AppConfigurationSource : ValueSource<ApplicationConfig, AppConfigurationSource.Params> {
  interface Params : ValueSourceParameters {
    val version: RegularFileProperty
    val properties: RegularFileProperty
    val defaults: RegularFileProperty
  }

  override fun obtain(): ApplicationConfig {
    val version = loadProperties(parameters.version.get().asFile)
    val properties = loadProperties(parameters.properties.get().asFile)
    val defaults = loadProperties(parameters.defaults.get().asFile)

    val keystoreFilePath = properties.getProperty("keystore.file", "").takeIf {
      !(version.getProperty("app.disable_signing")?.toBoolean() ?: false)
    }

    val applicationName = version.getOrThrow("app.name")
    val applicationId = version.getOrThrow("app.id")
    val isExampleBuild = applicationId.matches(Regex(
      "^(?:com|org)\\.example(?:\\.[a-z]+)+$"
    ))
    val isExperimentalBuild =
      isExampleBuild ||
      keystoreFilePath == null ||
        version.getProperty("app.experimental", "false") == "true"
    val applicationExtension = getOrDefault(properties, "tgx.extension", defaults).also {
      require(it == "none" || it == "google")
    }

    val sampleApiId = defaults.getIntOrThrow("telegram.api_id")
    val apiId = properties.getProperty("telegram.api_id").takeIf { !it.isNullOrEmpty() }?.toInt() ?: sampleApiId
    val apiHash = getOrDefault(properties, "telegram.api_hash", defaults)

    if (apiId == sampleApiId) {
      Logging.getLogger(AppConfigurationSource::class.java).apply {
        warn("""
          Telegram API credentials are missing.
          
          Set them in your local.properties file:
          telegram.api_id=YOUR_API_ID_HERE
          telegram.api_hash=YOUR_API_HASH_HERE
          
          Obtain them at https://core.telegram.org/api/obtaining_api_id
        """.trimIndent())
      }
    }

    val sdkDir: String = properties.getProperty("sdk.dir")
      ?.takeIf { it.isNotEmpty() && it.none(Char::isWhitespace) }
      ?: run {
        System.err.println("Warning: sdk.dir is not set")
        System.getenv("ANDROID_HOME").orEmpty()
      }

    val msys2Dir = if (isWindowsHost()) {
      properties.getOrThrow("msys2.dir").also {
        if (it.isEmpty()) {
          fatal("msys2.dir is not set")
        }
        if (it.any(Char::isWhitespace)) {
          fatal("msys2.dir contains whitespace: $it")
        }
        validateMsys2Dir(File(it))
      }
    } else {
      ""
    }

    return ApplicationConfig(
      // local.properties & local.properties.sample
      sdkDir =
        sdkDir,
      msys2Dir =
        msys2Dir,
      applicationName =
        applicationName,
      applicationId =
        applicationId,
      extension =
        applicationExtension,
      sourceCodeUrl =
        version.getProperty("app.sources_url", ""),
      isExperimentalBuild =
        isExperimentalBuild,
      isHuaweiBuild =
        false,
      forceOptimize =
        version.getProperty("app.forceoptimize")?.toBoolean() ?: false,
      doNotObfuscate =
        isExampleBuild ||
          version.getProperty("app.dontobfuscate")?.toBoolean() ?: false,
      telegramApiId =
        apiId,
      telegramApiHash =
        apiHash,
      safetyNetToken =
        properties.getProperty("safetynet.api_key", "").takeIf {
          keystoreFilePath != null
        },
      appDownloadUrl =
        version.getOrThrow("app.download_url"),
      googlePlayUrl =
        version.getProperty("app.google_download_url", null),
      galaxyStoreUrl =
        version.getProperty("app.galaxy_download_url", null),
      huaweiAppGalleryUrl =
        version.getProperty("app.huawei_download_url", null),
      amazonAppStoreUrl =
        version.getProperty("app.amazon_download_url", null),
      pullRequests =
        properties.getProperty("pr.ids", "").split(',').filter { it.matches(Regex("^[0-9]+$")) }.map {
          PullRequest(it.toLong(), properties)
        }.sortedBy { it.id },
      outputFileNamePrefix =
        properties.getProperty("app.file", null) ?:
        applicationName.replace(" ", "-").replace("#", ""),
      keystorePropertiesPath =
        keystoreFilePath,

      // version.properties
      applicationVersion =
        version.getProperty("app.version", "0").toInt().takeIf { versionOverride ->
          versionOverride > 0
        } ?:
        version.getOrThrow("version.app").toInt(),
      build = BuildVersions(version),
      majorVersion =
        version.getOrThrow("version.major").toInt(),
      nativeLibraryVersion =
        version.getProperty("version.jni"),
      leveldbVersion =
        version.getProperty("version.leveldb"),
      emojiVersion =
        version.getIntOrThrow("version.emoji"),
      creationDateMillis =
        version.getOrThrow("version.creation").toLong(),
    )
  }
}
