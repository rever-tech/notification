package com.rever.repository

/**
  * Created by phuonglam on 9/22/16.
  */
trait NotificationRepository {
  def create(id: String, map: Map[String, Any])
  def update(id: String, map: Map[String, Any])
  def delete(id: String)
  def read(id: String): Map[String, Any]
}
