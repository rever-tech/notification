package ms.notification.controller.http

import javax.inject.Inject

import com.twitter.finatra.http.Controller
import ms.notification.domain.GetTestRequest
import ms.notification.service.NotificationService

/**
  * @author phuonglam created on 9/19/16.
  */
class NotificationController @Inject()(notificationService: NotificationService) extends Controller {
  get("/") { request: GetTestRequest =>
    response.ok("Hello moto!")
  }


}
