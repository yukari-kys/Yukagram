package tgx.extension.google

import android.content.Context
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.ConnectionResult
import tgx.bridge.TokenRetrieverListener
import com.google.firebase.FirebaseApp
import tgx.bridge.DeviceTokenRetriever
import tgx.bridge.PushManagerBridge
import org.drinkless.tdlib.TdApi.DeviceTokenFirebaseCloudMessaging
import java.util.regex.Pattern

abstract class DefaultFirebaseTokenRetriever : DeviceTokenRetriever("firebase") {
  override fun isAvailable(context: Context): Boolean {
    try {
      if (context != null) {
        return GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS
      }
    } catch (e: Throwable) {
    }
    return false
  }


  override fun performInitialization(context: Context): Boolean {
    try {
      PushManagerBridge.log("FirebaseApp is initializing...")
      if (FirebaseApp.initializeApp(context) != null) {
        PushManagerBridge.log("FirebaseApp initialization finished successfully")
        return true
      } else {
        PushManagerBridge.log("FirebaseApp initialization failed")
      }
    } catch (e: Throwable) {
      PushManagerBridge.error("FirebaseApp initialization failed with error", e)
    }
    return false
  }

  companion object {
    fun extractFirebaseErrorName(e: Throwable): String {
      val message: String = e.message!!
      if (!message.isNullOrEmpty()) {
        val matcher = Pattern.compile("(?<=: )[A-Z_]+$").matcher(message)
        if (matcher.find()) {
          return matcher.group()
        }
        return message
      }
      return e.javaClass.getSimpleName()
    }
  }
}

class FirebaseDeviceTokenRetriever : DefaultFirebaseTokenRetriever() {
  override fun fetchDeviceToken(context: Context, listener: TokenRetrieverListener) {
    try {
      PushManagerBridge.log("FirebaseMessaging: registering...")
      FirebaseMessaging.getInstance().register().addOnSuccessListener(OnSuccessListener { _ ->
        PushManagerBridge.log("FirebaseMessaging: successfully registered, obtaining installation ID...")
        FirebaseInstallations.getInstance().id
          .addOnSuccessListener { installationId ->
            PushManagerBridge.log("FirebaseMessaging: successfully fetched installation ID: \"%s\"", installationId)
            listener.onTokenRetrievalSuccess(DeviceTokenFirebaseCloudMessaging(installationId, true))
          }
          .addOnFailureListener { e: Exception? ->
            val errorName = extractFirebaseErrorName(e!!)
            PushManagerBridge.error(
              "FirebaseMessaging: installation ID fetch failed ($errorName)",
              e
            )
            listener.onTokenRetrievalError(errorName, e)
          }
      }).addOnFailureListener { e: Exception? ->
        val errorName = extractFirebaseErrorName(e!!)
        PushManagerBridge.error(
          "FirebaseMessaging: registration failed ($errorName)",
          e
        )
        listener.onTokenRetrievalError(errorName, e)
      }
    } catch (e: Throwable) {
      PushManagerBridge.error("FirebaseMessaging: token fetch failed with error", e)
      listener.onTokenRetrievalError("FIREBASE_REQUEST_ERROR", e)
    }
  }
}