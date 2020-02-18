package com.bastrich

import cats.MonadError
import com.bastrich.entities._
import com.bastrich.storage.Storage
import io.vertx.core.Handler
import io.vertx.core.http.{HttpMethod, HttpServerRequest, HttpServerResponse}
import monix.eval.Task
import monix.execution.Scheduler

import cats.syntax.applicative._
import cats.syntax.applicativeError._
import cats.syntax.flatMap._
import cats.syntax.functor._
import cats.syntax.monadError._

class RequestHandler[F[_]](val config: Config, storage: Storage[F])(implicit monadError: MonadError[F, Throwable]) extends Handler[HttpServerRequest] {
  private val DefaultResponseText = s"send POST ${config.host}:${config.port}}/shorten?url=<taget_url>"

  private implicit val executor = Scheduler.fixedPool("asd", config.parallelism)

  override def handle(event: HttpServerRequest): Unit = {
    toRequest(event) match {
      case ShortenUrlRequest(response, urlParam) => {

        urlParam
          .pure
          .ensure(new Exception("Not valid URL")) {
            !_.isEmpty
          }
          .flatMap { a => storage.put(a) }
          .map { shortUrl =>
            response.end(s"http://${config.host}:${config.port}/$shortUrl")
          }
          .recover { case exception => response.end(exception.getMessage) } match {
          case task: Task[String] => task.runToFuture
          case _ =>
        }
      }


      case HomePageRequest(response) => response.end(DefaultResponseText)

      case RedirectRequest(response, path) =>
        storage.get(path.tail)
          .map { shortUrl => redirect(response, shortUrl) }
          .recover { case exception => response.end(s"Error: ${exception.getMessage}") }
        match {
          case task: Task[String] => task.runToFuture
          case _ =>
        }

      case NotValidRequest(response) => response.end(DefaultResponseText)
    }
  }

  def validUrlTry(url: String) =
    if (url.isEmpty) new Exception("Not valid URL") else url

  def validUrlTask(url: String): Task[String] =
    if (url.isEmpty) Task.raiseError(new Exception("Not valid URL")) else Task(url)


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
