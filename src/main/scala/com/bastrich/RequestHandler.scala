package com.bastrich

import java.util.concurrent.ForkJoinPool

import com.bastrich.entities.{HomePageRequest, NotValidRequest, RedirectRequest, Request, ShortenUrlRequest}
import io.vertx.core.Handler
import io.vertx.core.http.{HttpMethod, HttpServerRequest, HttpServerResponse}

import scala.language.implicitConversions

import scala.concurrent.{ExecutionContext, Future}

class RequestHandler(val config: Config) extends Handler[HttpServerRequest] {
  private val DefaultResponseText = s"send POST ${config.host}:${config.port}}/shorten?url=<taget_url>"

  private implicit val executor: ExecutionContext = ExecutionContext.fromExecutor(new ForkJoinPool(config.parallelism))

  private val storage = new Storage

  override def handle(event: HttpServerRequest): Unit = {
    toRequest(event) match {
      case ShortenUrlRequest(response, urlParam) =>
        (
          for {
            _ <- if (validUrl(urlParam)) Future.unit else Future.failed(new Exception("Not valid URL"))
            shortUrl <- storage.put(urlParam)
          } yield shortUrl
          )
          .map(shortUrl => response.end(s"http://${config.host}:${config.port}/$shortUrl"))
          .recover { case exception => response.end(exception.getMessage) }

      case HomePageRequest(response) => response.end(DefaultResponseText)

      case RedirectRequest(response, path) =>
        storage.get(path.tail)
          .map { shortUrl => redirect(response, shortUrl) }
          .recover { case exception => response.end(s"Error: ${exception.getMessage}") }

      case NotValidRequest(response) => response.end(DefaultResponseText)
    }
  }

  def validUrl(url: String) = !url.isEmpty

  def redirect(response: HttpServerResponse, targetUrl: String): Unit =
    response
      .setStatusCode(301)
      .putHeader("Location", if (targetUrl.startsWith("http://")) targetUrl else s"http://$targetUrl")
      .end()

  def toRequest(request: HttpServerRequest): Request = request match {
    case _ if request.path() == "/" && request.method() == HttpMethod.GET => HomePageRequest(request.response())
    case _ if request.path.startsWith("/shorten") && request.method() == HttpMethod.POST && request.getParam("url") != null
    => ShortenUrlRequest(request.response(), request.getParam("url"))
    case _ if request.method() == HttpMethod.GET => RedirectRequest(request.response(), request.path)
    case _ => NotValidRequest(request.response())
  }
}
