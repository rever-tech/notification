package ms.notification.service

import javax.inject.Inject

import com.twitter.util.Future
import ms.notification.domain.NotificationInfo
import ms.notification.domain.thrift.Notification
import ms.notification.repository.NotificationRepository

/**
 * Created by phuonglam on 9/25/16.
 **/
case class NotificationServiceImpl @Inject()(notifyRepo: NotificationRepository) extends NotificationService {

  override def sendNotification(notification: Notification): Future[Seq[String]] = {
    val infos = for (receiver <- notification.receiver) yield {
      NotificationInfo(
        sender = notification.sender,
        receiver = receiver,
        data = notification.data,
        notifyType = notification.notifyType
      )
    }
    notifyRepo.createMulti(infos)
  }

  override def getNumUnread(
    user: String,
    fromSender: Option[String] = None,
    notifyType: Option[String] = None
  ): Future[Long] = notifyRepo.numUnread(user, fromSender, notifyType)

  override def markRead(user: String, notifyId: String): Future[Boolean] = notifyRepo.markRead(user, notifyId)

  override def markReadAll(
    user: String,
    fromSender: Option[String] = None,
    notifyType: Option[String] = None
  ): Future[Boolean] = notifyRepo.markReadAll(user, notifyType)

  override def markUnread(user: String, notifyId: String): Future[Boolean] = notifyRepo.markUnread(user, notifyId)

  override def getUnread(
    user: String,
    fromSender: Option[String] = None,
    notifyType: Option[String] = None,
    page: Int = 1,
    size: Int = 10
  ): Future[Seq[Notification]] = notifyRepo.findUnread(user, fromSender, notifyType, page, size).map(info => info)

  override def getNotification(
    user: String,
    fromSender: Option[String] = None,
    notifyType: Option[String] = None,
    page: Int = 1,
    size: Int = 10
  ): Future[Seq[Notification]] = notifyRepo.findAll(user, fromSender, notifyType, page, size).map(info => info)


  implicit def SeqInfo2T(infos: Seq[NotificationInfo]): Seq[Notification] = {
    infos.map(info => {
      Notification(
        sender = info.sender,
        receiver = Seq(info.receiver),
        data = info.data,
        createdTime = info.createdTime,
        notifyType = info.notifyType,
        isRead = info.isRead
      )
    })
  }
}
