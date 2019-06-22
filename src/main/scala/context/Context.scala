package context

import com.typesafe.scalalogging.Logger
import config.Config

import scala.concurrent.duration._

case class Context(
                    registryURL: String,
                    userID: String,
                    password: String,
                    runScripts: Map[String, String],
                    sleepFunction: SleepFunction,
                    logger: Logger,
                  )

object Context {
  def fromConfig(config: Config): Context = {
    val sleepFunction = (minute: Int) => Thread.sleep(minute.minutes.toMillis)
    val logger = Logger("Corgi")
    val (registryURL, userID, password, _, runScripts) = Config.unapply(config).get

    Context(registryURL, userID, password, runScripts, sleepFunction, logger)
  }
}
