package util

import context.Context
import io.shaka.http.Https.TrustAnyServer
import io.shaka.http.{Method, Request}

object Fetch {
  def authenticatedRequest(context: Context,
                           method: Method,
                           urls: String*): Request = {
    import io.shaka.http.Https.HttpsConfig
    implicit val https: Option[HttpsConfig] = Some(HttpsConfig(TrustAnyServer))

    val registryURL = context.registryURL
    val requestingURL = context.registryProtocol + "://" + URL.join(
      registryURL +: urls: _*
    )
    val userID = context.userID
    val password = context.password
    val requestObject =
      Request(method, requestingURL).basicAuth(userID, password)

    requestObject
  }
}
