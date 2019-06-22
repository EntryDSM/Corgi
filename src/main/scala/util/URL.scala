package util

import java.nio.file.Paths

object URL {
  def join(urls: String*): String = {
    val joinedUrl = urls match {
      case first :: more => Some(Paths.get(first, more: _*))
      case first :: Nil => Some(Paths.get(first))
    }
    joinedUrl.toString
  }
}
