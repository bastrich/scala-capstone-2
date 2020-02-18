package com.bastrich.utils

import com.bastrich.RequestHandler
import monix.eval.Task
import org.scalatest.Matchers._
import org.scalatest.matchers.Matcher

import scala.util.Random

object TestChain {
  def testChain[F[_]](steps: (Option[TestResponse] => (TestRequest, Matcher[TestResponse]))*)(implicit requestHandler: RequestHandler[F]): TestChain[F] = {
    new TestChain(
      requestHandler,
      steps,
      Random.nextInt(30)
    )
  }
}

class TestChain[F[_]](requestHandler: RequestHandler[F], chain: Seq[Option[TestResponse] => (TestRequest, Matcher[TestResponse])], periodMs: => Int) {
  def run() = {
    chain
      .foldLeft[Option[TestResponse]](None) { (previousTestResponse, step) =>
        val (testRequest, matcher) = step(previousTestResponse)
        requestHandler.handle(testRequest)
        Thread.sleep(1)
        val actualResponse = testRequest.response
        actualResponse.asInstanceOf[TestResponse] should matcher
        Thread.sleep(periodMs)
        Some(actualResponse)
      }
  }
}