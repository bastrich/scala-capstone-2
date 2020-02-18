package com.bastrich

import com.bastrich.storage.ConcurrentAsyncStorage
import io.vertx.core.Vertx
import pureconfig.ConfigSource
import pureconfig.generic.auto._

object Main extends App {
  val config = ConfigSource.default.load[Config] match {
    case Left(configReaderFailures) => throw new Exception(configReaderFailures.prettyPrint())
    case Right(config) => config
  }

  implicit val monadErrorTask = new monix.eval.instances.CatsBaseForTask

  Vertx
    .vertx()
    .createHttpServer()
    .requestHandler(new RequestHandler(config, new ConcurrentAsyncStorage))
    .listen(config.port)
}
