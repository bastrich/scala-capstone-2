package com.bastrich.utils

import java.util

import io.vertx.core.buffer.Buffer
import io.vertx.core.http._
import io.vertx.core.net.NetSocket
import io.vertx.core.{Future, Handler, MultiMap}
import javax.security.cert.X509Certificate

class TestRequest(path: String, method: HttpMethod, urlParam: Option[String] = None) extends HttpServerRequest {
  override def exceptionHandler(handler: Handler[Throwable]): HttpServerRequest = ???

  override def handler(handler: Handler[Buffer]): HttpServerRequest = ???

  override def pause(): HttpServerRequest = ???

  override def resume(): HttpServerRequest = ???

  override def fetch(amount: Long): HttpServerRequest = ???

  override def endHandler(endHandler: Handler[Void]): HttpServerRequest = ???

  override def version(): HttpVersion = ???

  override def method(): HttpMethod = method

  override def rawMethod(): String = ???

  override def scheme(): String = ???

  override def uri(): String = ???

  override def path(): String = path

  override def query(): String = ???

  override def host(): String = ???

  override def bytesRead(): Long = ???

  override val response = new TestResponse

  override def headers(): MultiMap = ???

  override def params(): MultiMap = ???

  override def peerCertificateChain(): Array[X509Certificate] = ???

  override def absoluteURI(): String = ???

  override def body(): Future[Buffer] = ???

  override def netSocket(): NetSocket = ???

  override def setExpectMultipart(expect: Boolean): HttpServerRequest = ???

  override def isExpectMultipart: Boolean = ???

  override def uploadHandler(uploadHandler: Handler[HttpServerFileUpload]): HttpServerRequest = ???

  override def formAttributes(): MultiMap = ???

  override def getFormAttribute(attributeName: String): String = ???

  override def upgrade(): ServerWebSocket = ???

  override def isEnded: Boolean = ???

  override def customFrameHandler(handler: Handler[HttpFrame]): HttpServerRequest = ???

  override def connection(): HttpConnection = ???

  override def streamPriorityHandler(handler: Handler[StreamPriority]): HttpServerRequest = ???

  override def cookieMap(): util.Map[String, Cookie] = ???

  override def getParam(paramName: String): String = if (paramName == "url") urlParam.orNull else null
}