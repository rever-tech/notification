package ms.notification.domain

/**
  * Created by phuonglam on 9/23/16.
  **/
case class NotificationInfo(notifyId: Option[String] = None,
                            sender: String,
                            receiver: Seq[String],
                            data: String,
                            notifyType: Option[String] = None,
                            createdTime: Option[Long] = None,
                            isRead: Option[Boolean] = None
                           ) {
}
