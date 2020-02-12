package com.bastrich.entities

import io.vertx.core.http.HttpServerResponse

case class ShortenUrlRequest(response: HttpServerResponse, urlParam: String) extends Request
