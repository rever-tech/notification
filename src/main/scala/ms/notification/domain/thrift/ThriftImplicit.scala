package ms.notification.domain.thrift

import ms.notification.domain.NotificationInfo

/**
  * Created by phuonglam on 9/19/16.
  */
object ThriftImplicit {

  implicit def T2Info(notification: Notification): NotificationInfo = NotificationInfo(sender = notification.sender, receiver = notification.receiver, data = notification.data)
  implicit def Info2T(info: NotificationInfo): Notification = Notification(sender = info.sender, receiver = info.receiver, data = info.data)
}
