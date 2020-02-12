package com.bastrich.entities

import io.vertx.core.http.HttpServerResponse

case class RedirectRequest(response: HttpServerResponse, path: String) extends Request
