package loop

import context.Context

object Loop {

  @scala.annotation.tailrec
  def loop(context: Context): Unit = {
    val newestImageDigestMap = for {
      registryImageNameList <- registry.getRegistryImageList(context)
      imageDigestList <- registry.getRegistryImageDigestList(context, registryImageNameList)
    } yield (registryImageNameList zip imageDigestList).toMap

    val localImageDigestMap = for {
      imageList <- docker.getImageList(context)
      digestList = docker.getDigestList(imageList)
      duplicatedImageNameList <- docker.getImageNameList(digestList)
      imageDigestList <- docker.getImageDigestList(digestList)
      mapOfDuplicatedImageNameToDigest = duplicatedImageNameList zip imageDigestList
    } yield mapOfDuplicatedImageNameToDigest.groupBy(_._1).map { case (key, value) => (key, value.map(_._2)) }

    context.sleepFunction(context.pollingRatePerMinute)
    loop(context)
  }
}
