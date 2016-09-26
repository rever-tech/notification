package ms.notification.controller.thrift

import javax.inject.Inject

import com.twitter.finagle.Service
import com.twitter.finatra.thrift.Controller
import com.twitter.inject.Logging
import com.twitter.util.Future
import ms.notification.service.TNotificationService.GetUnread.{Result, Args}
import ms.notification.service.TNotificationService._
import ms.notification.service.{TNotificationService, NotificationService}

/**
  * Created by phuonglam on 9/19/16.
  */
class NotificationController @Inject()(notifyService: NotificationService) extends Controller with TNotificationService.BaseServiceIface with Logging {
  override val sendNotification= handle(SendNotification) { args: SendNotification.Args => {
    notifyService.sendNotification(args.notification)
  }
  }

  override val getNumUnread= handle(GetNumUnread) { args: GetNumUnread.Args => {
    notifyService.getNumUnread(
      user = args.user,
      notifyType = args.notifyType,
      fromSender = args.fromSender
    )
  }
  }

  override val markRead= handle(MarkRead) { args: MarkRead.Args => {
    notifyService.markRead(args.user, args.notifyId)
  }
  }

  override val markReadAll= handle(MarkReadAll) { args: MarkReadAll.Args => {
    notifyService.markReadAll(
      user = args.user,
      notifyType = args.notifyType,
      fromSender = args.fromSender
    )
  }
  }

  override val markUnread= handle(MarkUnread) { args: MarkUnread.Args => {
    notifyService.markUnread(args.user, args.notifyId)
  }
  }

  override val getUnread= handle(GetUnread) { args: GetUnread.Args=> {
    notifyService.getUnread(
      user = args.user,
      notifyType = args.notifyType,
      fromSender = args.fromSender,
      page = args.page match {
        case Some(s) => s
        case None => 1
      },
      size = args.size match {
        case Some(s) => s
        case None => 10
      }
    )
  }
  }

  override val getNotification= handle(GetNotification) { args: GetNotification.Args => {
    notifyService.getNotification(
      user = args.user,
      notifyType = args.notifyType,
      fromSender = args.fromSender,
      page = args.page match {
        case Some(s) => s
        case None => 1
      },
      size = args.size match {
        case Some(s) => s
        case None => 10
      }
    )
  }
  }

  override val ping: Service[Ping.Args, Ping.Result] = handle(Ping) {args: Ping.Args => {
    Future("pong")
  }
  }
}
