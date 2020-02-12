package com.bastrich

import java.util.concurrent.ForkJoinPool

import com.bastrich.utils.TestChain.testChain
import com.bastrich.utils.{Steps, TestChain}
import org.apache.commons.lang3.RandomStringUtils
import org.scalatest.FunSpec

import scala.collection.parallel.CollectionConverters._
import scala.collection.parallel.ForkJoinTaskSupport
import scala.util.Random

class Test extends FunSpec {

  private val config = Config("test_host", 111, 4)
  private implicit val requestHandler: RequestHandler = new RequestHandler(config)
  private val steps = new Steps(config.host, config.port)

  it("test logic in parallel") {
    val testChains = List
      .fill(30)(generateTestChain(42 + Random.nextInt(100) / 3))
      .par

    testChains.tasksupport = new ForkJoinTaskSupport(new ForkJoinPool(4))

    testChains
      .foreach(_.run())
  }

  private def generateTestChain(approximateLength: Int): TestChain = {
    import steps._

    testChain(
      List
        .fill(approximateLength / 3) {
          val targetUrl = RandomStringUtils.randomAlphanumeric(5 + Random.nextInt(10))

          List(shorten(targetUrl), redirectSuccess(targetUrl))
            .appendedAll(if (Random.nextBoolean()) List(defaultPage()) else Nil)
            .appendedAll(if (Random.nextBoolean()) List(redirectError(RandomStringUtils.randomAlphanumeric(15 + Random.nextInt(10)))) else Nil)
        }
        .flatten: _*
    )
  }
}

