package loop

import context.Context

object Loop {

  type ImageName = String

  type ImageDigest = String

  @scala.annotation.tailrec
  def loop(context: Context): Unit = {
    val registryImageDigestList = for {
      registryImageList <- registry.getRegistryImageList(context)
      imageDigestList <- registry.getRegistryImageDigestList(context, registryImageList)
    } yield (registryImageList zip imageDigestList).toMap

    context.sleepFunction(context.pollingRatePerMinute)
    loop(context)
  }
}
