package ms.notification.service

import com.twitter.util.Future
import ms.notification.domain.thrift.Notification

/**
 * @author phuonglam
 *         created on 9/19/16.
 */

trait NotificationService {

  def sendNotification(notification: Notification): Future[Seq[String]]

  def getNumUnread(user: String, notifyType: Option[String] = None, fromSender: Option[String] = None): Future[Long]

  def markRead(user: String, notifyId: String): Future[Boolean]

  def markReadAll(user: String, notifyType: Option[String] = None, fromSender: Option[String] = None): Future[Boolean]

  def markUnread(user: String, notifyId: String): Future[Boolean]

  def getUnread(
    user: String,
    fromSender: Option[String] = None,
    notifyType: Option[String] = None,
    page: Int = 1,
    size: Int = 10
  ): Future[Seq[Notification]]

  def getNotification(
    user: String,
    fromSender: Option[String] = None,
    notifyType: Option[String] = None,
    page: Int = 1,
    size: Int = 10
  ): Future[Seq[Notification]]
}