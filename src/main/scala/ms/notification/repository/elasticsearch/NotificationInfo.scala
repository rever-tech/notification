package ms.notification.repository.elasticsearch

import org.elasticsearch.common.xcontent.{XContentFactory, XContentBuilder}

/**
  * Created by phuonglam on 9/23/16.
  **/
object NotificationInfo {

  val F_Sender = "sender"
  val F_Receiver = "receiver"
  val F_Data = "data"
  val F_NotifyType = "notify_type"
  val F_CreatedTime = "created_time"
  val F_IsRead = "is_read"

  def getMapping(esType: String): XContentBuilder = {
    XContentFactory.jsonBuilder
      .startObject(esType)
      .startObject("properties")
        .startObject(F_Sender).field("type", "string").field("index", "not_analyzed").endObject
        .startObject(F_Receiver).field("type", "string").field("index", "not_analyzed").endObject
        .startObject(F_Data).field("type", "string").field("index", "no").endObject
        .startObject(F_NotifyType).field("type", "string").field("index", "not_analyzed").endObject
        .startObject(F_CreatedTime).field("type", "long").endObject
        .startObject(F_IsRead).field("type", "boolean").endObject
      .endObject
      .endObject
  }
}