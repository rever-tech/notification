package com.rever.controller.http

import javax.inject.Inject

import com.rever.domain.GetTestRequest
import com.rever.service.NotificationService
import com.twitter.finatra.http.Controller

/**
  * @author phuonglam created on 9/19/16.
  */
class NotificationController @Inject()(notificationService: NotificationService) extends Controller {
  get("/") { request: GetTestRequest =>
    response.ok("Hello moto!")
  }


}
