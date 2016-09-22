package com.rever.repository

/**
  * Created by phuonglam on 9/22/16.
  */
trait SearchRepository {
  def search(sender: Option[String] = None,
             receiver: Option[String] = None,
             notifyType: Option[String] = None,
             isRead: Option[Boolean] = None,
             page: Int = 0,
             size: Int = 10): Seq[Map[String, Any]]
}
