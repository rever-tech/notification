package ms.notification.repository

import com.twitter.util.Future
import ms.notification.domain.NotificationInfo

/**
  * Created by phuonglam on 9/22/16.
  */
trait NotificationRepository {
  def create(notifyId: String, notificationInfo: NotificationInfo): Future[Boolean]

  def update(notifyId: String, notificationInfo: NotificationInfo): Future[Boolean]

  def delete(notifyId: String): Future[Boolean]

  def get(notifyId: String): Future[NotificationInfo]

  def findAll(user: String,
              fromSender: Option[String] = None,
              notifyType: Option[String] = None,
              page: Int = 1,
              size: Int = 10): Seq[NotificationInfo]

  def findUnread(user: String,
                 fromSender: Option[String] = None,
                 notifyType: Option[String] = None,
                 page: Int = 1,
                 size: Int = 10): Seq[NotificationInfo]

  def numUnread(user: String, fromSender: Option[String] = None, notifyType: Option[String] = None): Int

}