package tgx.extension

import android.app.Application
import android.content.Context
import tgx.bridge.DeviceTokenRetriever
import tgx.bridge.Extension
import tgx.extension.hms.FirebaseDeviceTokenRetriever
import tgx.extension.hms.obtainHuaweiAppId

object TelegramXExtension : Extension("google") {
  override fun createNewTokenRetriever(context: Context): DeviceTokenRetriever = FirebaseDeviceTokenRetriever()

  override fun configure(application: Application, deviceTokenRetriever: DeviceTokenRetriever) {
    if (deviceTokenRetriever !is FirebaseDeviceTokenRetriever) {
      FirebaseMessaging.getInstance().isAutoInitEnabled = false
    }
  }
}