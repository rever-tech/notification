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
    "send successful" in {
      Assertions.assert(client.sendNotification(notification).isDone)
    }
    "getNumUnread successful" in {
      Assertions.assertResult(1)(client.getNumUnread(user = receiver1).value)
    }
    "getUnread successful" in {
      Assertions.assertResult(1)(client.getUnread(receiver1).value.size)
    }
    "getNotification successful" in {
      Assertions.assertResult(1)(client.getNotification(receiver1).value.size)
    }
    "markRead successful" in {
      val notify = client.getUnread(receiver1).value.head
      Assertions.assert(client.markRead(receiver1, notify.notifyId.get).isDone)
      Assertions.assertResult(0)(client.getNumUnread(user = receiver1).value)
    }
    "markUnread successful" in {
      val notify = client.getNotification(receiver1).value.head
      Assertions.assert(client.markUnread(receiver1, notify.notifyId.get).isDone)
      Assertions.assertResult(1)(client.getNumUnread(user = receiver1).value)
    }
  }





}
