package ms.notification.repository

import com.twitter.util.Future
import ms.notification.domain.NotificationInfo

/**
 * Created by phuonglam on 9/22/16.
 **/
trait NotificationRepository {
  def create(infos: Seq[NotificationInfo]): Future[Seq[String]]

  def delete(notifyId: String): Future[Boolean]

  def get(notifyId: String): Future[NotificationInfo]

  def findAll(
    user: String,
    fromSender: Option[String] = None,
    notifyType: Option[String] = None,
    page: Int = 1,
    size: Int = 10
  ): Future[Seq[NotificationInfo]]

  def findUnread(
    user: String,
    fromSender: Option[String] = None,
    notifyType: Option[String] = None,
    page: Int = 1,
    size: Int = 10
  ): Future[Seq[NotificationInfo]]

  def numUnread(
    user: String,
    fromSender: Option[String] = None,
    notifyType: Option[String] = None
  ): Future[Long]

  def markRead(user: String, notifyId: String): Future[Boolean]

  def markReadAll(
    user: String,
    fromSender: Option[String] = None,
    notifyType: Option[String] = None
  ): Future[Boolean]

  def markUnread(user: String, notifyId: String): Future[Boolean]
}