package com.rever.service

import com.rever.domain.thrift.Notification
import com.twitter.util.Future

/**
  * @author phuonglam 
  * created on 9/19/16.
  */

trait NotificationService {

  def sendNotification(notification: Notification): Future[String]
  def getNumUnread(user: String, notifyType: Option[String] = None): Future[Int]
  def getUnread(user: String, notifyType: Option[String] = None, page: Option[Int] = None, size: Option[Int] = None): Future[Seq[Notification]]
  def markRead(user: String, notifyId: String)
  def markReadAll(user: String, notifyType: Option[String] = None)
  def markUnread(user: String, notifyId: String)
  def getNotification(user: String, page: Option[Int] = None, size: Option[Int] = None, notifyType: Option[String] = None, sender: Option[String] = None): Future[Seq[Notification]]
}