package ms.notification.domain

import ms.notification.domain.thrift.Notification

/**
 * Created by phuonglam on 9/28/16.
 **/
case class PostNotificationRequest (
  sender: String,
  receiver: List[String],
  data: String,
  notifyType: Option[String]) {

}

object PostNotificationRequestImplicit {
  implicit def Request2Notify(request: PostNotificationRequest): Notification = {
    Notification(
      sender = request.sender,
      receiver = request.receiver,
      data = request.data,
      notifyType = request.notifyType
    )
  }
}