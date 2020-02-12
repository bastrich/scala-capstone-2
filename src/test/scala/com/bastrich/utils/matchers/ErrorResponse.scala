package com.bastrich.utils.matchers

import com.bastrich.utils.TestResponse
import org.scalatest.matchers.{MatchResult, Matcher}

class ErrorResponse extends Matcher[TestResponse] {

  def apply(left: TestResponse) = {
    MatchResult(
      left.endMessage.startsWith("Error"),
      s"Unexpected",
      s"Good"
    )
  }
}
