package loop

import context.Context
import docker.ImageDigest
import sys.process._
import scala.language.postfixOps

object Loop {

  @scala.annotation.tailrec
  def loop(context: Context): Unit = {
    val newestImageDigestMap = for {
      registryImageNameList <- registry.getRegistryImageList(context)
      imageDigestList <- registry.getRegistryImageDigestList(
        context,
        registryImageNameList
      )
    } yield (registryImageNameList zip imageDigestList).toMap

    val localImageDigestMap = for {
      imageList <- docker.getImageList(context)
      digestList = docker.getDigestList(imageList)
      duplicatedImageNameList <- docker.getImageNameList(digestList)
      imageDigestList <- docker.getImageDigestList(digestList)
      mapOfDuplicatedImageNameToDigest = duplicatedImageNameList zip imageDigestList
    } yield
      mapOfDuplicatedImageNameToDigest.groupBy(_._1).map {
        case (key, value) => (key, value.map(_._2))
      }

    def isLocalImageNewest(localImageDigestList: List[ImageDigest],
                           remoteImageDigest: ImageDigest): Boolean = {
      localImageDigestList contains remoteImageDigest
    }

    val needingUpdateImageList = newestImageDigestMap map {
      newestImageDigestMap =>
        for {
          (remoteKey, remoteValue) <- newestImageDigestMap
          localImageDigestMap <- localImageDigestMap
          localValue <- localImageDigestMap.get(remoteKey)
          if !isLocalImageNewest(localValue, remoteValue)
        } yield remoteKey
    }

    val commands = needingUpdateImageList map { updatingImageList =>
      for {
        updatingImage <- updatingImageList
        runCommand <- context.runScripts.get(updatingImage)
      } yield runCommand
    }

    for {
      commandList <- commands
      command <- commandList
    } {
      command !
    }

    context.sleepFunction(context.pollingRatePerMinute)
    loop(context)
  }
}
