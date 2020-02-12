package com.bastrich.utils

import java.lang

import io.vertx.core.buffer.Buffer
import io.vertx.core.http.{Cookie, HttpMethod, HttpServerResponse}
import io.vertx.core.{AsyncResult, Future, Handler, MultiMap}

object TestResponse {
  def apply(): TestResponse = TestResponse()
}

case class TestResponse(
                         @volatile var statusCode: Int = 0,
                         @volatile var responseHeaders: Map[String, String] = Map(),
                         @volatile var endMessage: String = "",
                         @volatile var isEnded: Boolean = false) extends HttpServerResponse {

  override def exceptionHandler(handler: Handler[Throwable]): HttpServerResponse = ???

  override def setWriteQueueMaxSize(maxSize: Int): HttpServerResponse = ???

  override def drainHandler(handler: Handler[Void]): HttpServerResponse = ???

  override def getStatusCode: Int = ???

  override def setStatusCode(statusCode: Int): HttpServerResponse = {
    this.statusCode = statusCode
    this
  }

  override def getStatusMessage: String = ???

  override def setStatusMessage(statusMessage: String): HttpServerResponse = ???

  override def setChunked(chunked: Boolean): HttpServerResponse = ???

  override def isChunked: Boolean = ???

  override def headers(): MultiMap = ???

  override def putHeader(name: String, value: String): HttpServerResponse = {
    responseHeaders = responseHeaders + (name -> value)
    this
  }

  override def putHeader(name: CharSequence, value: CharSequence): HttpServerResponse = ???

  override def putHeader(name: String, values: lang.Iterable[String]): HttpServerResponse = ???

  override def putHeader(name: CharSequence, values: lang.Iterable[CharSequence]): HttpServerResponse = ???

  override def trailers(): MultiMap = ???

  override def putTrailer(name: String, value: String): HttpServerResponse = ???

  override def putTrailer(name: CharSequence, value: CharSequence): HttpServerResponse = ???

  override def putTrailer(name: String, values: lang.Iterable[String]): HttpServerResponse = ???

  override def putTrailer(name: CharSequence, value: lang.Iterable[CharSequence]): HttpServerResponse = ???

  override def closeHandler(handler: Handler[Void]): HttpServerResponse = ???

  override def endHandler(handler: Handler[Void]): HttpServerResponse = ???

  override def write(chunk: String, enc: String): Future[Void] = ???

  override def write(chunk: String, enc: String, handler: Handler[AsyncResult[Void]]): Unit = ???

  override def write(chunk: String): Future[Void] = ???

  override def write(chunk: String, handler: Handler[AsyncResult[Void]]): Unit = ???

  override def writeContinue(): HttpServerResponse = ???

  override def end(chunk: String): Future[Void] = {
    isEnded = true
    endMessage = chunk
    statusCode = 200
    Future.succeededFuture()
  }

  override def end(chunk: String, handler: Handler[AsyncResult[Void]]): Unit = ???

  override def end(chunk: String, enc: String): Future[Void] = ???

  override def end(chunk: String, enc: String, handler: Handler[AsyncResult[Void]]): Unit = ???

  override def sendFile(filename: String, offset: Long, length: Long): Future[Void] = ???

  override def sendFile(filename: String, offset: Long, length: Long, resultHandler: Handler[AsyncResult[Void]]): HttpServerResponse = ???

  override def close(): Unit = ???

  override def ended(): Boolean = ???

  override def closed(): Boolean = ???

  override def headWritten(): Boolean = ???

  override def headersEndHandler(handler: Handler[Void]): HttpServerResponse = ???

  override def bodyEndHandler(handler: Handler[Void]): HttpServerResponse = ???

  override def bytesWritten(): Long = ???

  override def streamId(): Int = ???

  override def push(method: HttpMethod, host: String, path: String, headers: MultiMap): Future[HttpServerResponse] = ???

  override def reset(code: Long): Unit = ???

  override def writeCustomFrame(`type`: Int, flags: Int, payload: Buffer): HttpServerResponse = ???

  override def addCookie(cookie: Cookie): HttpServerResponse = ???

  override def removeCookie(name: String, invalidate: Boolean): Cookie = ???

  override def write(data: Buffer): Future[Void] = ???

  override def write(data: Buffer, handler: Handler[AsyncResult[Void]]): Unit = ???

  override def end(): Future[Void] = {
    isEnded = true
    Future.succeededFuture()
  }

  override def writeQueueFull(): Boolean = ???

  override def end(handler: Handler[AsyncResult[Void]]): Unit = ???
}
