package com.bastrich

import org.apache.commons.lang3.RandomStringUtils

import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}

/**
 * Thread-safe (with some nuances)
 */
class Storage(private implicit val executor: ExecutionContext) {
  private val threadNumberToHash = Array.tabulate(30) { i => (61 + i).toChar }
  private val hashToThreadNumber = threadNumberToHash.zipWithIndex.toMap
  private val threadNumberToMap = Array.fill(30)(Map[String, String]())
  private val existingShortUrls = ThreadLocal.withInitial[mutable.Set[String]](() => mutable.Set())

  def put(url: String): Future[String] = {
    Future {
      val threadNumber = Thread.currentThread().getId.toInt
      val shortUrl = threadNumberToHash(threadNumber) + shortenUrl(url)
      threadNumberToMap(threadNumber) = threadNumberToMap(threadNumber) + (shortUrl -> url)
      shortUrl
    }
  }

  def get(shortUrl: String): Future[String] = {
    Future {
      threadNumberToMap(hashToThreadNumber(shortUrl.head))(shortUrl)
    }
  }

  private def shortenUrl(url: String): String = {
    LazyList
      .continually(RandomStringUtils.randomAlphanumeric(8))
      .takeWhile(existingShortUrls.get().add(_))
      .head
  }
}
