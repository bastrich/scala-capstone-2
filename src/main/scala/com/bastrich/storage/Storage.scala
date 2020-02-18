package com.bastrich.storage

import cats.{Applicative, Monad, MonadError}
import monix.eval.instances

trait Storage[F[_]] {
  def put(url: String): F[String]
  def get(url: String): F[String]
}
