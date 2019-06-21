import config.Config

object Main {
  def main(args: Array[String]): Unit = {
    val config = Config.fromArgs(args)
  }
}
