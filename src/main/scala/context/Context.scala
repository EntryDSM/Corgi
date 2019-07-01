package context

import com.typesafe.scalalogging.Logger
import config.Config

import scala.concurrent.duration._

case class Context(
                    registryURL: String,
                    userID: String,
                    password: String,
                    pollingRatePerMinute: Int,
                    runScripts: Map[String, String],
                    sleepFunction: SleepFunction,
                    logger: Logger,
                    dockerEngineSocket: String
                  )

object Context {
  def fromConfig(config: Config): Context = {
    val sleepFunction = (minute: Int) => Thread.sleep(minute.minutes.toMillis)
    val logger = Logger("Corgi")
    val (
      registryURL,
      userID,
      password,
      pollingRatePerMinute,
      runScripts,
      dockerEngineSocket
      ) = Config.unapply(config).get

    Context(registryURL, userID, password, pollingRatePerMinute, runScripts, sleepFunction, logger, dockerEngineSocket)
  }
}
