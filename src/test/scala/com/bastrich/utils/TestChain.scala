package com.bastrich.utils

import com.bastrich.RequestHandler
import org.scalatest.Matchers._
import org.scalatest.matchers.Matcher

import scala.util.Random

object TestChain {
  def testChain(steps: (Option[TestResponse] => (TestRequest, Matcher[TestResponse]))*)(implicit requestHandler: RequestHandler): TestChain = {
    new TestChain(
      requestHandler,
      steps,
      Random.nextInt(30)
    )
  }
}

class TestChain(requestHandler: RequestHandler, chain: Seq[Option[TestResponse] => (TestRequest, Matcher[TestResponse])], periodMs: => Int) {
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