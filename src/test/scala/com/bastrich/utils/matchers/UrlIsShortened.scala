package com.bastrich.utils.matchers

import com.bastrich.utils.TestResponse
import org.scalatest.matchers.{MatchResult, Matcher}

class UrlIsShortened(host: String, port: Int) extends Matcher[TestResponse] {

  def apply(left: TestResponse) = {
    MatchResult(
      left.statusCode == 200 && left.isEnded && left.endMessage.startsWith(s"http://$host:$port/"),
      s"Unexpected",
      s"Good"
    )
  }
}
