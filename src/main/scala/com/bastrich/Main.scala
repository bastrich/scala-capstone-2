package com.bastrich

import io.vertx.core.Vertx
import pureconfig.ConfigSource
import pureconfig.generic.auto._

object Main extends App {
  val config = ConfigSource.default.load[Config] match {
    case Left(configReaderFailures) => throw new Exception(configReaderFailures.prettyPrint())
    case Right(config) => config
  }

  Vertx
    .vertx()
    .createHttpServer()
    .requestHandler(new RequestHandler(config))
    .listen(config.port)
}
