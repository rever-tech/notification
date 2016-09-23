package ms.notification.repository.elasticsearch

import javax.inject.Inject

import com.twitter.util.Future
import ms.notification.domain.NotificationInfo
import ms.notification.repository.NotificationRepository

/**
  * Created by phuonglam on 9/23/16.
  **/
case class ESNotificationRepository @Inject()(esClient: ESClient, index: String, esType: String) extends NotificationRepository {



  override def create(notifyId: String, notificationInfo: NotificationInfo): Future[Boolean] = ???

  override def update(notifyId: String, notificationInfo: NotificationInfo): Future[Boolean] = ???

  override def get(notifyId: String): Future[NotificationInfo] = ???

  override def findAll(user: String, fromSender: Option[String], notifyType: Option[String], page: Int, size: Int): Seq[NotificationInfo] = ???

  override def delete(notifyId: String): Future[Boolean] = ???

  override def numUnread(user: String, fromSender: Option[String], notifyType: Option[String]): Int = ???

  override def findUnread(user: String, fromSender: Option[String], notifyType: Option[String], page: Int, size: Int): Seq[NotificationInfo] = ???
}
