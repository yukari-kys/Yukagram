package tgx.extension

import android.app.Application
import android.content.Context
import tgx.bridge.DeviceTokenRetriever
import tgx.bridge.Extension
import com.google.firebase.messaging.FirebaseMessaging
import tgx.extension.google.FirebaseDeviceTokenRetriever

object TelegramXExtension : Extension("google") {
  override fun createNewTokenRetriever(context: Context): DeviceTokenRetriever = FirebaseDeviceTokenRetriever()

  override fun configure(application: Application, deviceTokenRetriever: DeviceTokenRetriever) {
    if (deviceTokenRetriever !is FirebaseDeviceTokenRetriever) {
      FirebaseMessaging.getInstance().isAutoInitEnabled = false
    }
  }
}