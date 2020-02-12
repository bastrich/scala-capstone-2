package com.bastrich.entities

import io.vertx.core.http.HttpServerResponse

case class NotValidRequest(response: HttpServerResponse) extends Request
