package com.bastrich.storage

import java.util.concurrent.atomic.AtomicReferenceArray

import cats.MonadError
import monix.eval.Task
import org.apache.commons.lang3.RandomStringUtils

import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try


/**
 * Thread-safe (with some nuances)
 */
class ConcurrentAsyncStorage extends Storage[Task] {
  private val threadNumberToHash = Array.tabulate(30) { i => (61 + i).toChar }
  private val hashToThreadNumber = threadNumberToHash.zipWithIndex.toMap
  private val threadNumberToMap = new AtomicReferenceArray(Array.fill(30)(Map[String, String]()))
  private val existingShortUrls = ThreadLocal.withInitial[mutable.Set[String]](() => mutable.Set())

  def put(url: String) =
    Task {
      val threadNumber = Thread.currentThread().getId.toInt
      val shortUrl = threadNumberToHash(threadNumber) + shortenUrl(url)
      threadNumberToMap.getAndUpdate(threadNumber, { currentMap => currentMap + (shortUrl -> url) })
      shortUrl
    }


  def get(shortUrl: String) =
    Task {
      threadNumberToMap.get(hashToThreadNumber(shortUrl.head))(shortUrl)
    }

  private def shortenUrl(url: String): String = {
    LazyList
      .continually(RandomStringUtils.randomAlphanumeric(8))
      .find(existingShortUrls.get().add)
      .get
  }
}
