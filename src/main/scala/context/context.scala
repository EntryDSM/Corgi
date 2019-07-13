import com.typesafe.scalalogging.Logger
import config.Config

import scala.concurrent.duration._

package object context {
  type SleepFunction = Int => Unit

  case class Context(registryProtocol: String,
                     registryURL: String,
                     userID: String,
                     password: String,
                     pollingRatePerMinute: Int,
                     runScripts: Map[String, String],
                     sleepFunction: context.SleepFunction,
                     logger: Logger,
                     dockerEngineSocket: String)

  object Context {
    def fromConfig(config: Config): Context = {
      val sleepFunction = (minute: Int) => Thread.sleep(minute.minutes.toMillis)
      val logger = Logger("Corgi")
      val (
        registryProtocol,
        registryURL,
        userID,
        password,
        pollingRatePerMinute,
        runScripts,
        dockerEngineSocket
      ) = Config.unapply(config).get

      Context(
        registryProtocol,
        registryURL,
        userID,
        password,
        pollingRatePerMinute,
        runScripts,
        sleepFunction,
        logger,
        dockerEngineSocket
      )
    }
  }
}
