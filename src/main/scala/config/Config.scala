package config

import org.json4s._
import org.json4s.native.JsonMethods._

import scala.util.{Success, Try}

case class Config(registryProtocol: String,
                  registryURL: String,
                  userID: String,
                  password: String,
                  pollingRatePerMinute: Int = 5,
                  runScripts: Map[String, String],
                  dockerEngineSocket: String)

object Config {
  def fromArgs(args: Array[String]): Option[Config] = {
    implicit val formats: DefaultFormats = DefaultFormats
    args match {
      case Array("--config", configFilePath) =>
        val configFile = {
          val configFileBuffer = scala.io.Source.fromFile(configFilePath)
          configFileBuffer.mkString
        }
        val parseResult = Try(parse(configFile).extract[Config])
        parseResult match {
          case Success(config) => Some(config)
          case _               => None
        }
      case _ => None
    }
  }
}
