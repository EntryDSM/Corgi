package util

import java.nio.file.Paths

object URL {
  def join(urls: String*): String = {
    val joinedUrl = urls match {
      case first +: more             => Paths.get(first, more: _*)
      case first if urls.length == 1 => Paths.get(first.head)
    }
    joinedUrl.toString
  }
}
