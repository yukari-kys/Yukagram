package tgx.bridge

import android.app.Application
import android.content.Context
import org.drinkless.tdlib.TdApi

class DummyTokenRetriever : DeviceTokenRetriever("dummy") {
  override fun isAvailable(context: Context): Boolean = true
  override fun performInitialization(context: Context): Boolean = true
  override fun fetchDeviceToken(context: Context, listener: TokenRetrieverListener) {}
}

open class Extension(@JvmField val name: String) {
  fun isNotEmpty(): Boolean =
    name != "none"

  open fun createNewTokenRetriever(context: Context): DeviceTokenRetriever = DummyTokenRetriever()
  open fun configure(application: Application, deviceTokenRetriever: DeviceTokenRetriever) { }
}