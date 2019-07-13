import config.Config
import context.Context
import loop.Loop

object Main {
  def main(args: Array[String]): Unit = {
    val context = for {
      config <- Config.fromArgs(args)
    } yield Context.fromConfig(config)

    context match {
      case Some(ctx) => Loop.loop(ctx)
      case None =>
        println("Cannot execute Corgi. please check configuration file.")
    }
  }
}
