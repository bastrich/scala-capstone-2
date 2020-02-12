package com.bastrich.utils

import com.bastrich.utils.matchers.{ErrorResponse, UrlIsShortened}
import io.vertx.core.http.HttpMethod.{GET, POST}
import org.scalatest.Matchers.equal

class Steps(host: String, port: Int) {
  def shorten(targetUrl: String) = { _: Option[TestResponse] =>
    (new TestRequest("/shorten", POST, Some(targetUrl)), new UrlIsShortened(host, port))
  }

  def redirectSuccess(expectedTargetUrl: String) = { previousTestResponse: Option[TestResponse] =>
    (
      new TestRequest(previousTestResponse.get.endMessage.substring(8 + host.length + port.toString.length), GET),
      equal(TestResponse(301, Map("Location" -> (if (expectedTargetUrl.startsWith("http://")) expectedTargetUrl else s"http://$expectedTargetUrl")), "", true)).matcher[TestResponse]
    )
  }

  def redirectError(shortPath: String) = { _: Option[TestResponse] =>
    (
      new TestRequest(shortPath, GET),
      new ErrorResponse
    )
  }

  def defaultPage() = { _: Option[TestResponse] =>
    (
      new TestRequest("/", GET),
      equal(TestResponse(200, Map(), s"send POST $host:$port}/shorten?url=<taget_url>", true)).matcher[TestResponse]
    )
  }


}
