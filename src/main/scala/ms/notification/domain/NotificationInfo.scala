package ms.notification.domain

import org.elasticsearch.common.xcontent.XContentBuilder

/**
  * Created by phuonglam on 9/23/16.
  **/
case class NotificationInfo(notifyId: Option[String] = None,
                            sender: String,
                            receiver: String,
                            data: String,
                            notifyType: Option[String] = None,
                            createdTime: Option[Long] = None,
                            updatedTime: Option[Long] = None,
                            isRead: Option[Boolean] = None
                           ) {
}
