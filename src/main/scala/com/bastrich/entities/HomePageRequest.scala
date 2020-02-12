package com.bastrich.entities

import io.vertx.core.http.HttpServerResponse

case class HomePageRequest(response: HttpServerResponse) extends Request
