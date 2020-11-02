package ms.notification.controller.http

import javax.inject.Inject

import com.twitter.finatra.http.Controller
import ms.notification.domain._
import ms.notification.domain.PostNotificationRequestImplicit._
import ms.notification.service.NotificationService

/**
  * @author phuonglam created on 9/19/16.
  */
class NotificationController @Inject()(notifyService: NotificationService) extends Controller {
  post("/notifications") {request: PostNotificationRequest => {
    notifyService.sendNotification(request)
  }}
}
