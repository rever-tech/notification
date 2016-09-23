package ms.notification.repository

import ms.notification.domain.NotificationInfo

/**
  * Created by phuonglam on 9/22/16.
  */
trait NotificationRepository {
  def create(id: String, notificationInfo: NotificationInfo)
  def update(id: String, notificationInfo: NotificationInfo)
  def delete(id: String)
  def read(id: String): NotificationInfo

  def searchUnread(receiver: String,
                   sender: String = null,
                   notifyType: String = null,
                   page: Int = 1,
                   size: Int = 10): Seq[NotificationInfo]

  def search(receiver: String,
             sender: String = null,
             notifyType: String = null,
             page: Int = 1,
             size: Int = 10): Seq[NotificationInfo]

}