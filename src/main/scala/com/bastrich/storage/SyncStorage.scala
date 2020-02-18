package com.bastrich.storage

import java.util.concurrent.atomic.AtomicReferenceArray

import cats.MonadError
import monix.eval.Task
import org.apache.commons.lang3.RandomStringUtils

import scala.collection.concurrent.TrieMap
import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

import scala.collection.mutable


class SyncStorage extends Storage[Try] {
  private val shortUrls = mutable.Map[String, String]()

  def put(url: String) =
    Try {
      val shortUrl = shortenUrl(url)
      shortUrls(shortUrl) = url
      shortUrl
    }


  def get(shortUrl: String) =
    Try {
      shortUrls(shortUrl)
    }

  private def shortenUrl(url: String): String = {
    LazyList
      .continually(RandomStringUtils.randomAlphanumeric(8))
      .filterNot(shortUrls.keySet.contains)
      .head
  }
}
