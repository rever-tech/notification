package ms.notification.controller

import com.twitter.finatra.http.EmbeddedHttpServer
import com.twitter.finatra.thrift.ThriftClient
import com.twitter.inject.server.FeatureTest
import com.twitter.util.Future
import ms.notification.Server
import ms.notification.domain.thrift.Notification
import ms.notification.service.TNotificationService
import org.scalatest.Assertions

/**
  * Created by phuonglam on 9/22/16.
  */
class NotificationControllerTest extends FeatureTest {
  override protected val server = new EmbeddedHttpServer(twitterServer = new Server) with ThriftClient
  val client = server.thriftClient[TNotificationService[Future]](clientId = "1")

  val sender = "phuonglt"
  val receiver1 = "duydb"
  val receiver2 = "sangdn"
  val sendData = "notification-data"
  val notifyType = "test"
  val notification = Notification(data = sendData,sender = sender,receiver = Seq(receiver1),notifyType=Some(notifyType))

  "[Thrift] test flow send notification 1 to 1" should {
    var notifyId: Seq[String] = Seq()
    "send successful" in {
      notifyId = client.sendNotification(notification).value
      Assertions.assert(notifyId.nonEmpty)
    }
    "getNumUnread successful" in {
      Assertions.assert(client.getNumUnread(user = receiver1).value > 0)
    }
    "getUnread successful" in {
      Assertions.assert(client.getUnread(receiver1).value.nonEmpty)
    }
    "getNotification successful" in {
      Assertions.assert(client.getNotification(receiver1).value.nonEmpty)
    }
    "markRead successful" in {
      Assertions.assert(client.markRead(receiver1, notifyId.head).value)
    }
    "markUnread successful" in {
      Assertions.assert(client.markUnread(receiver1, notifyId.head).value)
    }
  }





}
