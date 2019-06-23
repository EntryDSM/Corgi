import config.Config
import context.Context

object Main {
  def main(args: Array[String]): Unit = {
    val context = for {
      config <- Config.fromArgs(args)
    } yield Context.fromConfig(config)
  }
}
