package loop

import context.Context
import io.shaka.http.Http.http
import io.shaka.http.{HttpHeader, Method}
import org.json4s._
import org.json4s.native.JsonMethods._
import scalaz.std.list.listInstance
import scalaz.std.option.optionInstance
import scalaz.syntax.traverse.ToTraverseOps
import util.Fetch

object Loop {

  @scala.annotation.tailrec
  def loop(context: Context): Unit = {
    val registryImageDigestList = for {
      registryImageList <- getRegistryImageList(context)
      imageDigestList <- getRegistryImageDigestList(context, registryImageList)
    } yield (registryImageList zip imageDigestList).toMap

    context.sleepFunction(context.pollingRatePerMinute)
    loop(context)
  }

  def getRegistryImageList(context: Context): Option[List[String]] = {
    val requestObject = Fetch.authenticatedRequest(context, Method.GET, pathForGetRegistryImageList)
    val response = http(requestObject)
    val body = for {
      entity <- response.entity
    } yield parse(entity.toString()).extract[RegistryImageList]

    body map { registryImageList => registryImageList.repositories }
  }

  def getRegistryImageDigestList(context: Context, imageList: List[String]): Option[List[String]] = {
    val digests = imageList map { imageName =>
      val requestObject = Fetch.authenticatedRequest(context, Method.HEAD, pathForGetRegistryDigest(imageName))
      val response = http(requestObject)
      val digest = response.header(HttpHeader.unknownHeader(dockerDigestHeaderFieldName))

      digest
    }
    digests.sequence
  }

  implicit val formats: DefaultFormats = DefaultFormats

  private def dockerDigestHeaderFieldName = "Docker-Content-Digest"

  private def pathForGetRegistryImageList = "/v2/_catalog"

  private def pathForGetRegistryDigest(imageName: String) = {
    "v2/" + imageName + "/manifests/latest"
  }

  case class RegistryImageList(repositories: List[String])
}
