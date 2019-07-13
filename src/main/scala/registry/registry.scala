import context.Context
import docker.{ImageDigest, ImageName}
import io.shaka.http.Http.http
import io.shaka.http.{HttpHeader, Method}
import org.json4s.DefaultFormats
import org.json4s.native.JsonMethods.parse
import scalaz.std.list.listInstance
import scalaz.std.option.optionInstance
import scalaz.syntax.traverse.ToTraverseOps
import util.Fetch

package object registry {

  def getRegistryImageList(context: Context): Option[List[ImageName]] = {
    val requestObject = Fetch.authenticatedRequest(
      context,
      Method.GET,
      pathForGetRegistryImageList
    )
    val response = http(requestObject)
    val body = for {
      entity <- response.entity
    } yield parse(entity.toString()).extract[RegistryImageList]

    body map { registryImageList =>
      registryImageList.repositories
    }
  }

  private def pathForGetRegistryImageList = "/v2/_catalog"

  implicit val formats: DefaultFormats = DefaultFormats

  def getRegistryImageDigestList(
    context: Context,
    imageList: List[String]
  ): Option[List[ImageDigest]] = {
    val digests = imageList map { imageName =>
      val requestObject = Fetch.authenticatedRequest(
        context,
        Method.HEAD,
        pathForGetRegistryDigest(imageName)
      )
      val response = http(requestObject)
      val digest =
        response.header(HttpHeader.unknownHeader(dockerDigestHeaderFieldName))

      digest
    }
    digests.sequence
  }

  private def dockerDigestHeaderFieldName = "Docker-Content-Digest"

  private def pathForGetRegistryDigest(imageName: String) = {
    "v2/" + imageName + "/manifests/latest"
  }

  case class RegistryImageList(repositories: List[String])

}
