import com.spotify.docker.client.DefaultDockerClient
import com.spotify.docker.client.messages.Image
import context.Context
import scalaz.std.list.listInstance
import scalaz.std.option.optionInstance
import scalaz.syntax.traverse.ToTraverseOps

import scala.collection.JavaConverters._
import scala.util.{Success, Try}

package object docker {

  type ImageDigestWithName = String

  type ImageDigest = String

  type ImageName = String

  def getImageList(context: Context): Option[List[Image]] = {
    val docker = new DefaultDockerClient(context.dockerEngineSocket)
    val images = Try {
      docker.listImages().asScala.toList
    } match {
      case Success(imageList) => Some(imageList)
      case _ => None
    }

    images
  }

  def getDigestList(imageList: List[Image]): List[ImageDigestWithName] = imageList.flatMap(_.repoDigests.asScala.toList)

  def getImageNameList(digestList: List[String]): Option[List[ImageName]] = {
    val PatternForImageName = """([\w.-\/]+\/)?([\w-]+)@sha256:[0-9a-f]+""".r
    val imageNameList = digestList.map {
      case PatternForImageName(_, imageName) => Some(imageName)
      case _ => None
    }

    imageNameList.sequence
  }

  def getImageDigestList(digestList: List[String]): Option[List[ImageDigest]] = {
    val PatternForImageDigest = """([\w.-\/]+\/)?[\w-]+@sha256:([0-9a-f]+)""".r
    val imageDigestList = digestList.map {
      case PatternForImageDigest(_, imageDigest) => Some(imageDigest)
      case _ => None
    }

    imageDigestList.sequence
  }
}
